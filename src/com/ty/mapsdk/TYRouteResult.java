package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.ty.mapdata.TYLocalPoint;

/**
 * 路径规划结果
 */
@SuppressLint("UseSparseArrays")
public class TYRouteResult {

	private List<TYRoutePart> allRoutePartArray = new ArrayList<TYRoutePart>();
	private Map<Integer, List<TYRoutePart>> allFloorRoutePartDict = new HashMap<Integer, List<TYRoutePart>>();

	/**
	 * 路径结果的构造函数，一般不需要直接调用，由导航管理类调用生成
	 * 
	 * @param routePartArray
	 *            路径段数组
	 */
	public TYRouteResult(List<TYRoutePart> routePartArray) {
		allRoutePartArray.addAll(routePartArray);

		for (int i = 0; i < routePartArray.size(); ++i) {
			TYRoutePart rp = routePartArray.get(i);
			int floor = rp.getMapInfo().getFloorNumber();

			if (!allFloorRoutePartDict.containsKey(floor)) {
				List<TYRoutePart> array = new ArrayList<TYRoutePart>();
				allFloorRoutePartDict.put(floor, array);
			}

			List<TYRoutePart> array = allFloorRoutePartDict.get(floor);
			array.add(rp);
		}
	}

	/**
	 * 判断位置位置点是否偏离导航线
	 * 
	 * @param point
	 *            目标位置点
	 * @param distance
	 *            判断是否偏离的阈值
	 * 
	 * @return 是否偏离导航线
	 */
	public boolean isDeviatingFromRoute(TYLocalPoint point, double distance) {
		boolean isDeviating = true;

		int floor = point.getFloor();
		Point pos = new Point(point.getX(), point.getY());

		List<TYRoutePart> rpArray = allFloorRoutePartDict.get(floor);
		if (rpArray != null && rpArray.size() > 0) {
			for (TYRoutePart rp : rpArray) {

				Proximity2DResult pr = GeometryEngine.getNearestCoordinate(
						rp.getRoute(), pos, false);
				Point nearestPoint = pr.getCoordinate();

				double nearestDistance = GeometryEngine.distance(pos,
						nearestPoint, null);
				if (nearestDistance <= distance) {
					isDeviating = false;
					return isDeviating;
				}
			}
		}
		return isDeviating;
	}

	/**
	 * 获取距离目标位置点最近的路径段
	 * 
	 * @param location
	 *            目标位置点
	 * 
	 * @return 最近的路径段
	 */
	public TYRoutePart getNearestRoutePart(TYLocalPoint location) {
		TYRoutePart result = null;

		int floor = location.getFloor();
		double nearestDistance = Double.MAX_VALUE;

		Point pos = new Point(location.getX(), location.getY());

		List<TYRoutePart> rpArray = allFloorRoutePartDict.get(floor);
		if (rpArray != null && rpArray.size() > 0) {
			for (TYRoutePart rp : rpArray) {
				Proximity2DResult pr = GeometryEngine.getNearestCoordinate(
						rp.getRoute(), pos, false);
				Point nearestPoint = pr.getCoordinate();

				double distance = GeometryEngine.distance(pos, nearestPoint,
						null);
				if (distance < nearestDistance) {
					nearestDistance = distance;
					result = rp;
				}
			}
		}
		return result;
	}

	/**
	 * 获取目标楼层的所有路径段
	 * 
	 * @param floor
	 *            目标楼层
	 * 
	 * @return 路径段数组
	 */
	public List<TYRoutePart> getRoutePartsOnFloor(int floor) {
		return allFloorRoutePartDict.get(floor);
	}

	/**
	 * 从路径段数组中获取特定索引的路径段
	 * 
	 * @param index
	 *            目标段索引
	 * 
	 * @return 目标段
	 */
	public TYRoutePart getRoutePart(int index) {
		return allRoutePartArray.get(index);
	}

	/**
	 * 获取目标路径段的导航提示
	 * 
	 * @param rp
	 *            目标路径段
	 * 
	 * @return 目标路径段的导航提示
	 */
	public List<TYDirectionalHint> getRouteDirectionalHint(TYRoutePart rp) {
		List<TYDirectionalHint> result = new ArrayList<TYDirectionalHint>();

		IPHPLandmarkManager landmarkManager = IPHPLandmarkManager.sharedManager();
		landmarkManager.loadLandmark(rp.getMapInfo());

		Polyline line = processPolyline(rp.getRoute());

		double currentAngle = TYDirectionalHint.INITIAL_EMPTY_ANGLE;

		if (line != null) {
			int numPoints = line.getPointCount();

			for (int i = 0; i < numPoints - 1; ++i) {
				Point p0 = line.getPoint(i);
				Point p1 = line.getPoint(i + 1);

				TYLocalPoint lp = new TYLocalPoint(p0.getX(), p0.getY(), rp
						.getMapInfo().getFloorNumber());
				TYLandmark landmark = landmarkManager.searchLandmark(lp, 10);

				TYDirectionalHint ds = new TYDirectionalHint(p0, p1,
						currentAngle);
				currentAngle = ds.getCurrentAngle();
				ds.setRoutePart(rp);

				if (landmark != null) {
					ds.setLandmark(landmark);
				}

				result.add(ds);
			}
		}

		return result;
	}

	/**
	 * 从一组导航提示中获取与目标位置点对应的导航提示
	 * 
	 * @param location
	 *            目标位置点
	 * @param directions
	 *            目标导航提示数组
	 * 
	 * @return 与目标位置点对应的导航提示
	 */
	public TYDirectionalHint getDirectionalHintForLocationFromHints(
			TYLocalPoint location, List<TYDirectionalHint> directions) {

		if (directions.size() < 1) {
			return null;
		}

		Polyline line = null;

		for (TYDirectionalHint hint : directions) {
			if (line == null) {
				line = new Polyline();
				line.startPath(hint.getStartPoint());
			} else {
				line.lineTo(hint.getStartPoint());
			}
		}

		TYDirectionalHint lastHint = directions.get(directions.size() - 1);
		if (lastHint != null && line != null) {
			line.lineTo(lastHint.getEndPoint());
		}

		Point pos = new Point(location.getX(), location.getY());
		Proximity2DResult pr = GeometryEngine.getNearestCoordinate(line, pos,
				false);
		int index = pr.getVertexIndex();
		return directions.get(index);
	}

	private static final double DISTANCE_THREHOLD = 5.0;
	private static final double ANGLE_THREHOLD = 10.0;

	private Polyline processPolyline(Polyline polyline) {
		if (polyline.getPointCount() <= 2) {
			return polyline;
		}

		Polyline result = null;

		double currentAngle = 10000;

		int numPoints = polyline.getPointCount();
		for (int i = 0; i < numPoints - 1; ++i) {
			Point p0 = polyline.getPoint(i);
			Point p1 = polyline.getPoint(i + 1);

			double distance = GeometryEngine.distance(p0, p1, null);
			if (distance < DISTANCE_THREHOLD) {
				continue;
			}

			IPRTRouteVector2 v = new IPRTRouteVector2(p1.getX() - p0.getX(),
					p1.getY() - p0.getY());
			double angle = v.getAngle();

			if (Math.abs(currentAngle - angle) > ANGLE_THREHOLD) {
				currentAngle = angle;
				if (result == null) {
					result = new Polyline();
					result.startPath(p0);
				} else {
					result.lineTo(p0);
				}
			}
		}

		if (result != null) {
			result.lineTo(polyline.getPoint(polyline.getPointCount() - 1));
		}

		return result;
	}

	/**
	 * 获取一组折线的子折线
	 * 
	 * @param originalLine
	 *            原折线
	 * @param start
	 *            子折线起点
	 * @param end
	 *            子折线终点
	 * 
	 * @return 目标子折线
	 */
	public static Polyline getSubPolyline(Polyline originalLine, Point start,
			Point end) {
		int startIndex = -1;
		int endIndex = -1;
		int numPoints = (int) originalLine.getPointCount();
		for (int i = 0; i < numPoints; i++) {
			Point p = originalLine.getPoint(i);

			if (start.getX() == p.getX() && start.getY() == p.getY()) {
				startIndex = i;
			}

			if (end.getX() == p.getX() && end.getY() == p.getY()) {
				endIndex = i;
				break;
			}
		}

		if (startIndex == -1 || endIndex == -1) {
			return null;
		}

		Polyline result = new Polyline();
		for (int i = startIndex; i <= endIndex; i++) {
			Point p = originalLine.getPoint(i);
			if (i == startIndex) {
				result.startPath(p);
			} else {
				result.lineTo(p);
			}
		}
		return result;
	}
}
