package cn.nephogram.mapsdk.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import cn.nephogram.data.NPLocalPoint;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;

@SuppressLint("UseSparseArrays")
public class NPRouteResult {

	private List<NPRoutePart> allRoutePartArray = new ArrayList<NPRoutePart>();
	private Map<Integer, List<NPRoutePart>> allFloorRoutePartDict = new HashMap<Integer, List<NPRoutePart>>();

	public NPRouteResult(List<NPRoutePart> routePartArray) {
		allRoutePartArray.addAll(routePartArray);

		for (int i = 0; i < routePartArray.size(); ++i) {
			NPRoutePart rp = routePartArray.get(i);
			int floor = rp.getMapInfo().getFloorNumber();

			if (!allFloorRoutePartDict.containsKey(floor)) {
				List<NPRoutePart> array = new ArrayList<NPRoutePart>();
				allFloorRoutePartDict.put(floor, array);
			}

			List<NPRoutePart> array = allFloorRoutePartDict.get(floor);
			array.add(rp);
		}
	}

	public boolean isDeviatingFromRoute(NPLocalPoint point, double distance) {
		boolean isDeviating = true;

		int floor = point.getFloor();
		Point pos = new Point(point.getX(), point.getY());

		List<NPRoutePart> rpArray = allFloorRoutePartDict.get(floor);
		if (rpArray != null && rpArray.size() > 0) {
			for (NPRoutePart rp : rpArray) {

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

	public NPRoutePart getNearestRoutePart(NPLocalPoint location) {
		NPRoutePart result = null;

		int floor = location.getFloor();
		double nearestDistance = Double.MAX_VALUE;

		Point pos = new Point(location.getX(), location.getY());

		List<NPRoutePart> rpArray = allFloorRoutePartDict.get(floor);
		if (rpArray != null && rpArray.size() > 0) {
			for (NPRoutePart rp : rpArray) {
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

	public List<NPRoutePart> getRoutePartsOnFloor(int floor) {
		return allFloorRoutePartDict.get(floor);
	}

	public NPRoutePart getRoutePart(int index) {
		return allRoutePartArray.get(index);
	}

	public List<NPDirectionalHint> getRouteDirectionalHint(NPRoutePart rp) {
		List<NPDirectionalHint> result = new ArrayList<NPDirectionalHint>();

		NPLandmarkManager landmarkManager = NPLandmarkManager.sharedManager();
		landmarkManager.loadLandmark(rp.getMapInfo());

		Polyline line = processPolyline(rp.getRoute());

		double currentAngle = NPDirectionalHint.INITIAL_EMPTY_ANGLE;

		if (line != null) {
			int numPoints = line.getPointCount();

			for (int i = 0; i < numPoints - 1; ++i) {
				Point p0 = line.getPoint(i);
				Point p1 = line.getPoint(i + 1);

				NPLocalPoint lp = new NPLocalPoint(p0.getX(), p0.getY(), rp
						.getMapInfo().getFloorNumber());
				NPLandmark landmark = landmarkManager.searchLandmark(lp, 10);

				NPDirectionalHint ds = new NPDirectionalHint(p0, p1,
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

	public NPDirectionalHint getDirectionalHintForLocationFromHints(
			NPLocalPoint location, List<NPDirectionalHint> directions) {

		if (directions.size() < 1) {
			return null;
		}

		Polyline line = null;

		for (NPDirectionalHint hint : directions) {
			if (line == null) {
				line = new Polyline();
				line.startPath(hint.getStartPoint());
			} else {
				line.lineTo(hint.getStartPoint());
			}
		}

		NPDirectionalHint lastHint = directions.get(directions.size() - 1);
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

	public Polyline processPolyline(Polyline polyline) {
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

			Vector2 v = new Vector2(p1.getX() - p0.getX(), p1.getY()
					- p0.getY());
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
