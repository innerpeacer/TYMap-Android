package com.ty.mapsdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.swig.IPXGeosCoordinate;
import com.ty.mapsdk.swig.IPXGeosGeometryFactory;
import com.ty.mapsdk.swig.IPXGeosLineString;
import com.ty.mapsdk.swig.IPXGeosPoint;
import com.ty.mapsdk.swig.IPXRouteNetworkDBAdapter;
import com.ty.mapsdk.swig.IPXRouteNetworkDataset;

/**
 * 离线路径管理类
 */
public class TYOfflineRouteManager {
	static final String TAG = TYOfflineRouteManager.class.getSimpleName();

	Point startPoint;
	Point endPoint;

	IPXRouteNetworkDataset networkDataset;
	IPXGeosGeometryFactory factory;
	private TYRouteResult routeResult;

	private IPRoutePointConverter routePointConverter;
	List<TYMapInfo> allMapInfoArray = new ArrayList<TYMapInfo>();

	/***
	 * 离线路径管理类的构造函数
	 * 
	 * @param building
	 *            目标建筑
	 * @param mapInfoArray
	 *            目标建筑的所有楼层信息
	 */
	public TYOfflineRouteManager(TYBuilding building,
			List<TYMapInfo> mapInfoArray) {
		allMapInfoArray.addAll(mapInfoArray);
		TYMapInfo info = allMapInfoArray.get(0);
		routePointConverter = new IPRoutePointConverter(info.getMapExtent(),
				building.getOffset());

		String dbPath = getRouteDBPath(building);
		IPXRouteNetworkDBAdapter db = new IPXRouteNetworkDBAdapter(dbPath);
		db.open();
		networkDataset = db.readRouteNetworkDataset();
		db.close();

		Log.i(TAG, networkDataset.toString());

		factory = new IPXGeosGeometryFactory();
	}

	private String getRouteDBPath(TYBuilding building) {
		String dbName = String.format("%s_Route.db", building.getBuildingID());
		return new File(TYMapEnvironment.getDirectoryForBuilding(building),
				dbName).toString();
	}

	/**
	 * 请求路径规划，在代理方法获取规划结果
	 * 
	 * @param start
	 *            路径规划起点
	 * @param end
	 *            路径规划终点
	 */
	public void requestRoute(final TYLocalPoint start, final TYLocalPoint end) {
		startPoint = routePointConverter.getRoutePointFromLocalPoint(start);
		endPoint = routePointConverter.getRoutePointFromLocalPoint(end);
		// Log.i(TAG, "Start: " + startPoint);
		// Log.i(TAG, "End: " + endPoint);
		routeResult = null;

		IPXGeosCoordinate startCoord = new IPXGeosCoordinate();
		startCoord.setX(startPoint.getX());
		startCoord.setY(startPoint.getY());

		IPXGeosCoordinate endCoord = new IPXGeosCoordinate();
		endCoord.setX(endPoint.getX());
		endCoord.setY(endPoint.getY());

		IPXGeosPoint gStart = factory.createPoint(startCoord);
		IPXGeosPoint gEnd = factory.createPoint(endCoord);

		IPXGeosLineString line = networkDataset.getShorestPath(gStart, gEnd);
		// Log.i(TAG, line.getNumPoints() + " point in path");
		Polyline polyline = convertLineString(line);

		if (polyline == null) {
			notifyDidFailSolveRoute(new TYRouteException("没有从起点到终点的路径！"));
		} else {
			routeResult = processAgsPolyline(polyline);
			notifyDidSolveRoute(routeResult);
		}

	}

	private Polyline convertLineString(IPXGeosLineString line) {
		if (line == null || line.getNumPoints() == 0) {
			return null;
		}

		Polyline result = new Polyline();
		for (int i = 0; i < line.getNumPoints(); i++) {
			IPXGeosCoordinate coord = line.getCoordinateN(i);
			IPXGeosPoint p = factory.createPoint(coord);
			if (i == 0) {
				result.startPath(p.getX(), p.getY());
			} else {
				result.lineTo(p.getX(), p.getY());
			}
		}
		return result;
	}

	private TYRouteResult processAgsPolyline(Polyline routeLine) {
		List<List<TYLocalPoint>> pointArray = new ArrayList<List<TYLocalPoint>>();
		List<Integer> floorArray = new ArrayList<Integer>();

		int currentFloor = 0;
		List<TYLocalPoint> currentArray = null;

		int pathNum = (int) routeLine.getPathCount();
		if (pathNum > 0) {
			int num = routeLine.getPathSize(0);
			for (int i = 0; i < num; i++) {
				Point p = routeLine.getPoint(i);
				TYLocalPoint lp = routePointConverter
						.getLocalPointFromRoutePoint(p);
				boolean isValid = routePointConverter.checkPointValidity(lp);
				if (isValid) {
					// Log.i(TAG, "Floor: " + lp.getFloor());
					if (lp.getFloor() == 6) {
						Log.i(TAG, lp.toString());
					}

					if (lp.getFloor() != currentFloor) {
						currentFloor = lp.getFloor();
						currentArray = new ArrayList<TYLocalPoint>();
						pointArray.add(currentArray);
						floorArray.add(currentFloor);
					}
					currentArray.add(lp);
				}
			}
		}

		if (floorArray.size() < 1) {
			return null;
		}

		// Log.i(TAG, floorArray + "");

		List<TYRoutePart> routePartArray = new ArrayList<TYRoutePart>();
		for (int i = 0; i < floorArray.size(); i++) {
			int floor = floorArray.get(i);
			Polyline line = new Polyline();

			List<TYLocalPoint> pArray = pointArray.get(i);
			if (pArray.size() < 2) {
				continue;
			}

			for (int j = 0; j < pArray.size(); ++j) {
				TYLocalPoint lp = pArray.get(j);
				if (j == 0) {
					line.startPath(new Point(lp.getX(), lp.getY()));
				} else {
					line.lineTo(lp.getX(), lp.getY());
				}
			}

			TYMapInfo info = TYMapInfo.searchMapInfoFromArray(allMapInfoArray,
					floor);
			TYRoutePart rp = new TYRoutePart(line, info);
			routePartArray.add(rp);
		}

		int routePartNum = (int) routePartArray.size();
		for (int i = 0; i < routePartNum; i++) {
			TYRoutePart rp = routePartArray.get(i);
			if (i > 0) {
				rp.setPreviousPart(routePartArray.get(i - 1));
			}

			if (i < routePartNum - 1) {
				rp.setNextPart(routePartArray.get(i + 1));
			}

			rp.setPartIndex(i);
		}

		// Log.i(TAG, routePartArray.size() + "");
		return new TYRouteResult(routePartArray);
	}

	private List<TYOfflineRouteManagerListener> listeners = new ArrayList<TYOfflineRouteManager.TYOfflineRouteManagerListener>();

	/**
	 * 添加路径管理监听接口
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addRouteManagerListener(TYOfflineRouteManagerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * 移除路径管理监听接口
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void removeRouteManagerListener(
			TYOfflineRouteManagerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyDidSolveRoute(TYRouteResult route) {
		for (TYOfflineRouteManagerListener listener : listeners) {
			listener.didSolveRouteWithResult(this, route);
		}
	}

	private void notifyDidFailSolveRoute(Exception e) {
		for (TYOfflineRouteManagerListener listener : listeners) {
			listener.didFailSolveRouteWithError(this, e);
		}
	}

	/**
	 * 离线路径管理监听接口
	 */
	public interface TYOfflineRouteManagerListener {

		/**
		 * 解决路径规划返回方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param routeResult
		 *            路径规划结果
		 */
		void didSolveRouteWithResult(TYOfflineRouteManager routeManager,
				TYRouteResult routeResult);

		/**
		 * 路径规划失败回调方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param e
		 *            异常信息
		 */
		void didFailSolveRouteWithError(TYOfflineRouteManager routeManager,
				Exception e);
	}

	/**
	 * 路径异常类
	 * 
	 */
	public class TYRouteException extends Exception {
		private static final long serialVersionUID = 614393163181656501L;

		public TYRouteException() {
			super();
		}

		public TYRouteException(String msg) {
			super(msg);
		}
	}
}
