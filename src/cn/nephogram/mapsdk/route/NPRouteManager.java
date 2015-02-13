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

	public NPRouteManager(String url, UserCredentials credentials) {
		Log.i(TAG, "url:" + url);
		try {
			routeTask = RouteTask.createOnlineRouteTask(url, credentials);
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

	public void requestRoute(final Point start, final Point end) {
		if (routeParams == null) {
			CARouteException e = new CARouteException(
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

	private List<CARouteManagerListener> listeners = new ArrayList<NPRouteManager.CARouteManagerListener>();

	public void addRouteManagerListener(CARouteManagerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeRouteManagerListener(CARouteManagerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyRetrieveDefaultRouteTaskParameterFailed(Exception e) {
		for (CARouteManagerListener listener : listeners) {
			listener.didFailRetrieveDefaultRouteTaskParametersWithError(this, e);
		}
	}

	private void notifyRouteSolved(Graphic route) {
		for (CARouteManagerListener listener : listeners) {
			listener.didSolveRouteWithResult(this, route);
		}
	}

	private void notifyRouteSolvingFailed(Exception e) {
		for (CARouteManagerListener listener : listeners) {
			listener.didFailSolveRouteWithError(this, e);
		}
	}

	public interface CARouteManagerListener {
		void didFailRetrieveDefaultRouteTaskParametersWithError(
				NPRouteManager manager, Exception e);

		void didSolveRouteWithResult(NPRouteManager manager, Graphic route);

		void didFailSolveRouteWithError(NPRouteManager manager, Exception e);
	}

	public class CARouteException extends Exception {
		private static final long serialVersionUID = 614393163181656501L;

		public CARouteException() {
			super();
			// super("Failed to fetch route parameters from server");
		}

		public CARouteException(String msg) {
			super(msg);
		}
	}
}
