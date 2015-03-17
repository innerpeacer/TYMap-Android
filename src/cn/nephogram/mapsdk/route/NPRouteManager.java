package cn.nephogram.mapsdk.route;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.util.Log;
import cn.nephogram.mapsdk.NPMapEnvironment;

import com.esri.core.geometry.Point;
import com.esri.core.io.UserCredentials;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.RouteTask;
import com.esri.core.tasks.na.StopGraphic;

/**
 * 路径管理类
 */
public class NPRouteManager {
	static final String TAG = NPRouteManager.class.getSimpleName();

	Point startPoint;
	Point endPoint;

	// private RoutingTask routingTask;
	// private RoutingParameters routingTaskParams;
	// private RoutingResult routingResult;

	private RouteTask routeTask;
	private RouteParameters routeParams;
	private RouteResult routeResult;

	private Exception mException;
	private Handler mHandler = new Handler();
	private Runnable mRouteResultBack = new Runnable() {

		@Override
		public void run() {
			Log.i(TAG, "Route Result back");
			if (routeResult == null) {
				notifyRouteSolvingFailed(mException);
			} else {
				Route route = routeResult.getRoutes().get(0);
				notifyRouteSolved(route.getRouteGraphic());
			}

		}
	};

	/**
	 * 路径管理类的实例化方法
	 * 
	 * @param url
	 *            路径服务URL
	 * @param credential
	 *            用户访问验证
	 * 
	 */
	public NPRouteManager(String url, UserCredentials credential) {
		Log.i(TAG, "url:" + url);
		try {
			routeTask = RouteTask.createOnlineRouteTask(url, credential);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Thread t = new Thread() {
			public void run() {
				try {
					routeParams = routeTask
							.retrieveDefaultRouteTaskParameters();
					Log.i(TAG, "RouteParams:" + routeParams);
				} catch (Exception e) {
					notifyRetrieveDefaultRouteTaskParameterFailed(e);
					e.printStackTrace();
				}
			};
		};
		t.start();

	}

	/**
	 * 请求路径规划，在代理方法获取规划结果
	 * 
	 * @param start
	 *            路径规划起点
	 * @param end
	 *            路径规划终点
	 */
	public void requestRoute(final Point start, final Point end) {
		if (routeParams == null) {
			NPRouteException e = new NPRouteException(
					"route parameters from server not fetched");
			notifyRouteSolvingFailed(e);
			return;
		}

		routeResult = null;
		mException = null;

		Thread t = new Thread() {
			public void run() {
				NAFeaturesAsFeature faf = new NAFeaturesAsFeature();
				StopGraphic p1 = new StopGraphic(start);
				StopGraphic p2 = new StopGraphic(end);
				faf.setFeatures(new Graphic[] { p1, p2 });
				faf.setCompressedRequest(true);

				routeParams.setStops(faf);
				routeParams.setOutSpatialReference(NPMapEnvironment
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

		// routeParams.sets
	}

	private List<NPRouteManagerListener> listeners = new ArrayList<NPRouteManager.NPRouteManagerListener>();

	/**
	 * 添加路径管理监听接口
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addRouteManagerListener(NPRouteManagerListener listener) {
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
	public void removeRouteManagerListener(NPRouteManagerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyRetrieveDefaultRouteTaskParameterFailed(Exception e) {
		for (NPRouteManagerListener listener : listeners) {
			listener.didFailRetrieveDefaultRouteTaskParametersWithError(this, e);
		}
	}

	private void notifyRouteSolved(Graphic route) {
		for (NPRouteManagerListener listener : listeners) {
			listener.didSolveRouteWithResult(this, route);
		}
	}

	private void notifyRouteSolvingFailed(Exception e) {
		for (NPRouteManagerListener listener : listeners) {
			listener.didFailSolveRouteWithError(this, e);
		}
	}

	/**
	 * 路径管理监听接口
	 */
	public interface NPRouteManagerListener {
		/**
		 * 获取默认参数失败回调方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param e
		 *            异常信息
		 */
		void didFailRetrieveDefaultRouteTaskParametersWithError(
				NPRouteManager routeManager, Exception e);

		/**
		 * 解决路径规划返回方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param routeResultGraphic
		 *            路径规划结果
		 */
		void didSolveRouteWithResult(NPRouteManager routeManager,
				Graphic routeResultGraphic);

		/**
		 * 路径规划失败回调方法
		 * 
		 * @param routeManager
		 *            路径管理实例
		 * @param e
		 *            异常信息
		 */
		void didFailSolveRouteWithError(NPRouteManager routeManager, Exception e);
	}

	/**
	 * 路径异常类
	 * 
	 */
	public class NPRouteException extends Exception {
		private static final long serialVersionUID = 614393163181656501L;

		public NPRouteException() {
			super();
			// super("Failed to fetch route parameters from server");
		}

		public NPRouteException(String msg) {
			super(msg);
		}
	}
}
