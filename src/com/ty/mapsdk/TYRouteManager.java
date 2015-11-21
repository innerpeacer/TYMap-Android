package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;

/**
 * 路径管理类
 */
@SuppressLint("UseSparseArrays")
public class TYRouteManager {
	static final String TAG = TYRouteManager.class.getSimpleName();

	Point startPoint;
	Point endPoint;

	private RouteTask routeTask;
	private RouteParameters routeParams;
	private RouteResult routeResult;

	private IPRoutePointConverter routePointConverter;
	List<TYMapInfo> allMapInfoArray = new ArrayList<TYMapInfo>();

	private Exception mException;
	private Handler mHandler = new Handler();
	private Runnable mRouteResultBack = new Runnable() {

		@Override
		public void run() {
			if (routeResult == null) {
				notifyDidFailSolveRoute(mException);
			} else {
				Route route = routeResult.getRoutes().get(0);
				if (route != null) {
					TYRouteResult result = processRouteResultV2(route);

					if (result == null) {
						return;
					}
					notifyDidSolveRoute(result);
				}
			}
		}
	};

	/**
	 * 路径管理类的构造函数
	 * 
	 * @param building
	 *            目标建筑
	 * @param credential
	 *            用户访问验证
	 * @param mapInfoArray
	 *            目标建筑的所有楼层信息
	 */
	public TYRouteManager(TYBuilding building, UserCredentials credential,
			List<TYMapInfo> mapInfoArray) {

		allMapInfoArray.addAll(mapInfoArray);
		TYMapInfo info = allMapInfoArray.get(0);

		routePointConverter = new IPRoutePointConverter(info.getMapExtent(),
				building.getOffset());

		try {
			routeTask = RouteTask.createOnlineRouteTask(building.getRouteURL(),
					credential);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Thread t = new Thread() {
			public void run() {
				try {
					routeParams = routeTask
							.retrieveDefaultRouteTaskParameters();
					notifyDidRetrieveDefaultRouteTaskParameters();

				} catch (Exception e) {
					notifyDidFailRetrieveDefaultRouteTaskParameter(e);
					e.printStackTrace();
				}
			};
		};
		t.start();
	}

	private TYRouteResult processRouteResultV2(Route r) {
		List<List<TYLocalPoint>> pointArray = new ArrayList<List<TYLocalPoint>>();
		List<Integer> floorArray = new ArrayList<Integer>();

		Polyline routeLine = (Polyline) r.getRouteGraphic().getGeometry();

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
					Log.i(TAG, "Floor: " + lp.getFloor());
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

		Log.i(TAG, floorArray + "");

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

		Log.i(TAG, routePartArray.size() + "");

		return new TYRouteResult(routePartArray);
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
		if (routeParams == null) {
			TYRouteException e = new TYRouteException(
					"route parameters from server not fetched");
			notifyDidFailSolveRoute(e);
			return;
		}

		startPoint = routePointConverter.getRoutePointFromLocalPoint(start);
		endPoint = routePointConverter.getRoutePointFromLocalPoint(end);

		Log.i(TAG, "Start: " + startPoint);
		Log.i(TAG, "End: " + endPoint);

		routeResult = null;
		mException = null;

		Thread t = new Thread() {
			public void run() {
				NAFeaturesAsFeature faf = new NAFeaturesAsFeature();
				StopGraphic p1 = new StopGraphic(startPoint);
				StopGraphic p2 = new StopGraphic(endPoint);
				faf.setFeatures(new Graphic[] { p1, p2 });
				faf.setCompressedRequest(true);

				routeParams.setStops(faf);
				routeParams.setOutSpatialReference(TYMapEnvironment
						.defaultSpatialReference());

				try {
					routeResult = routeTask.solve(routeParams);
					mHandler.post(mRouteResultBack);
				} catch (Exception e) {
					mException = e;
					mHandler.post(mRouteResultBack);
					e.printStackTrace();
				}

			};
		};
		t.start();
	}

	private List<TYRouteManagerListener> listeners = new ArrayList<TYRouteManager.TYRouteManagerListener>();

	/**
	 * 添加路径管理监听接口
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addRouteManagerListener(TYRouteManagerListener listener) {
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
	public void removeRouteManagerListener(TYRouteManagerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyDidRetrieveDefaultRouteTaskParameters() {
		for (TYRouteManagerListener listener : listeners) {
			listener.didRetrieveDefaultRouteTaskParameters(this);
		}
	}

	private void notifyDidFailRetrieveDefaultRouteTaskParameter(Exception e) {
		for (TYRouteManagerListener listener : listeners) {
			listener.didFailRetrieveDefaultRouteTaskParametersWithError(this, e);
		}
	}

	private void notifyDidSolveRoute(TYRouteResult route) {
		for (TYRouteManagerListener listener : listeners) {
			listener.didSolveRouteWithResult(this, route);
		}
	}

	private void notifyDidFailSolveRoute(Exception e) {
		for (TYRouteManagerListener listener : listeners) {
			listener.didFailSolveRouteWithError(this, e);
		}
	}

	/**
	 * 路径管理监听接口
	 */
	public interface TYRouteManagerListener {
		/**
		 * 路径规划获取默认参数回调
		 * 
		 * @param routeManager
		 *            路径管理实例
		 */
		void didRetrieveDefaultRouteTaskParameters(TYRouteManager routeManager);

		/**
		 * 获取默认参数失败回调方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param e
		 *            异常信息
		 */
		void didFailRetrieveDefaultRouteTaskParametersWithError(
				TYRouteManager routeManager, Exception e);

		/**
		 * 解决路径规划返回方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param routeResult
		 *            路径规划结果
		 */
		void didSolveRouteWithResult(TYRouteManager routeManager,
				TYRouteResult routeResult);

		/**
		 * 路径规划失败回调方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param e
		 *            异常信息
		 */
		void didFailSolveRouteWithError(TYRouteManager routeManager, Exception e);
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
