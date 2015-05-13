package cn.nephogram.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.route.NPRouteLayer;
import cn.nephogram.mapsdk.route.NPRouteManager;
import cn.nephogram.mapsdk.route.NPRouteManager.NPRouteManagerListener;
import cn.nephogram.mapsdk.route.NPRouteResult;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleLineSymbol.STYLE;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

public class NephogramMapRouteActivity extends BaseMapViewActivity implements
		OnClickListener, OnSingleTapListener, NPRouteManagerListener {
	static final String TAG = NephogramMapRouteActivity.class.getSimpleName();

	private static final long serialVersionUID = 5847083145291274344L;
	// private static String ROUTE_TASK_URL =
	// "http://121.40.16.26:6080/arcgis/rest/services/002100002/NAServer/Route";

	Point currentPoint;
	NPLocalPoint startPoint;
	NPLocalPoint endPoint;

	NPRouteManager routeManager;
	NPRouteLayer routeLayer;

	NPPictureMarkerSymbol startSymbol;
	NPPictureMarkerSymbol endSymbol;
	NPPictureMarkerSymbol switchSymbol;

	boolean isRouting;
	NPRouteResult routeResult;

	Symbol routeSymbol;

	GraphicsLayer hintLayer;
	GraphicsLayer startLayer;
	GraphicsLayer endLayer;

	private Button startButton;
	private Button endButton;
	private Button requestRouteButton;
	private Button resetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initMapSettings();
		initSymbols();

		routeManager = new NPRouteManager(currentBuilding,
				NPMapEnvironment.defaultUserCredentials(), mapInfos);
		routeManager.addRouteManagerListener(this);
		routeSymbol = createRouteSymbol();
	}

	@Override
	public void didSolveRouteWithResult(NPRouteManager routeManager,
			NPRouteResult rs) {
		Log.i(TAG, "didSolveRouteWithResult");

		hintLayer.removeAll();

		routeResult = rs;
		showRouteResultOnCurrentFloor();
	}

	private void showRouteResultOnCurrentFloor() {
		routeLayer.removeAll();

		if (routeResult != null) {
			int floor = currentMapInfo.getFloorNumber();
			Log.i(TAG, "Current Floor: " + currentMapInfo.getFloorName());

			Polyline line = routeResult.getRouteOnFloor(floor);
			if (line != null) {
				routeLayer.addGraphic(new Graphic(line, routeSymbol));

				if (routeResult.isFirstFloor(floor)
						&& routeResult.isLastFloor(floor)) {
					Log.i(TAG, "Same Floor");
					return;
				}

				if (routeResult.isFirstFloor(floor)
						&& !routeResult.isLastFloor(floor)) {
					Point p = routeResult.getLastPointOnFloor(floor);
					if (p != null) {
						routeLayer.addGraphic(new Graphic(p, switchSymbol));
					}
					return;

				}

				if (!routeResult.isFirstFloor(floor)
						&& routeResult.isLastFloor(floor)) {
					Point p = routeResult.getFirstPointOnFloor(floor);
					if (p != null) {
						routeLayer.addGraphic(new Graphic(p, switchSymbol));
					}
					return;
				}

				if (!routeResult.isFirstFloor(floor)
						&& !routeResult.isLastFloor(floor)) {
					Point fp = routeResult.getFirstPointOnFloor(floor);
					Point lp = routeResult.getLastPointOnFloor(floor);
					if (fp != null) {
						routeLayer.addGraphic(new Graphic(fp, switchSymbol));
					}

					if (lp != null) {
						routeLayer.addGraphic(new Graphic(lp, switchSymbol));
					}
					return;
				}
			}
		}
	}

	@Override
	protected void changedToFloor(int index) {
		startLayer.removeAll();
		endLayer.removeAll();
		routeLayer.removeAll();

		super.changedToFloor(index);
		// if (isRouting) {
		// showRouteResultOnCurrentFloor();
		// }
	}

	public void onFinishLoadingFloor(NPMapView mapView, NPMapInfo mapInfo) {
		if (startPoint != null
				&& startPoint.getFloor() == mapInfo.getFloorNumber()) {
			startLayer.addGraphic(new Graphic(new Point(startPoint.getX(),
					startPoint.getY()), startSymbol));
		}

		if (endPoint != null && endPoint.getFloor() == mapInfo.getFloorNumber()) {
			endLayer.addGraphic(new Graphic(new Point(endPoint.getX(), endPoint
					.getY()), startSymbol));
		}

		if (isRouting) {
			showRouteResultOnCurrentFloor();
		}
	};

	@Override
	public void didFailSolveRouteWithError(NPRouteManager routeManager,
			Exception e) {

	}

	@Override
	public void didRetrieveDefaultRouteTaskParameters(
			NPRouteManager routeManager) {

	}

	@Override
	public void didFailRetrieveDefaultRouteTaskParametersWithError(
			NPRouteManager routeManager, Exception e) {

	}

	private CompositeSymbol createRouteSymbol() {
		CompositeSymbol cs = new CompositeSymbol();

		SimpleLineSymbol sls1 = new SimpleLineSymbol(Color.argb(255, 41, 147,
				207), 7, STYLE.SOLID);
		cs.add(sls1);

		SimpleLineSymbol sls2 = new SimpleLineSymbol(Color.argb(255, 43, 198,
				255), 6, STYLE.SOLID);
		cs.add(sls2);
		return cs;
	}

	private void initMapSettings() {
		routeLayer = new NPRouteLayer();
		mapView.addLayer(routeLayer);

		startLayer = new GraphicsLayer();
		mapView.addLayer(startLayer);

		endLayer = new GraphicsLayer();
		mapView.addLayer(endLayer);

		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		mapView.setOnSingleTapListener(this);
	}

	private void initSymbols() {
		startSymbol = new NPPictureMarkerSymbol(getResources().getDrawable(
				R.drawable.start));
		startSymbol.setWidth(34);
		startSymbol.setHeight(43);
		startSymbol.setOffsetX(0);
		startSymbol.setOffsetY(22);

		endSymbol = new NPPictureMarkerSymbol(getResources().getDrawable(
				R.drawable.end));
		endSymbol.setWidth(34);
		endSymbol.setHeight(43);
		endSymbol.setOffsetX(0);
		endSymbol.setOffsetY(22);

		switchSymbol = new NPPictureMarkerSymbol(getResources().getDrawable(
				R.drawable.nav_exit));
		switchSymbol.setWidth(37);
		switchSymbol.setHeight(37);

	}

	@Override
	public void onSingleTap(float x, float y) {
		Point mappoint = mapView.toMapPoint(x, y);

		currentPoint = mappoint;

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 5,
				com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE);
		hintLayer.removeAll();
		hintLayer.addGraphic(new Graphic(mappoint, sms));
	}

	private void setStartPoint() {
		startPoint = new NPLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		startLayer.removeAll();
		startLayer.addGraphic(new Graphic(currentPoint, startSymbol));
	}

	private void setEndPoint() {
		endPoint = new NPLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		endLayer.removeAll();
		endLayer.addGraphic(new Graphic(currentPoint, endSymbol));
	}

	private void requestRoute() {
		// "需要两个点请求路径!"
		if (startPoint == null || endPoint == null) {
			Toast.makeText(getBaseContext(), "需要两个点请求路径！", Toast.LENGTH_LONG)
					.show();
			return;
		}
		routeLayer.removeAll();

		routeResult = null;
		isRouting = true;

		routeManager.requestRoute(startPoint, endPoint);
	}

	private void reset() {
		startLayer.removeAll();
		endLayer.removeAll();
		hintLayer.removeAll();
		routeLayer.removeAll();

		startPoint = null;
		endPoint = null;
		currentPoint = null;

		isRouting = false;
		routeResult = null;
	}

	private void initLayout() {
		startButton = (Button) findViewById(R.id.btn_set_start);
		startButton.setOnClickListener(this);

		endButton = (Button) findViewById(R.id.btn_set_end);
		endButton.setOnClickListener(this);

		requestRouteButton = (Button) findViewById(R.id.btn_request_route);
		requestRouteButton.setOnClickListener(this);

		resetButton = (Button) findViewById(R.id.btn_reset);
		resetButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_start:
			setStartPoint();
			break;

		case R.id.btn_set_end:
			setEndPoint();
			break;

		case R.id.btn_request_route:
			requestRoute();
			break;

		case R.id.btn_reset:
			reset();
			break;
		default:
			break;
		}
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_route;
	}

	@Override
	public void onClickAtPoint(NPMapView mapView, Point mappoint) {
		super.onClickAtPoint(mapView, mappoint);
	}

	@Override
	public void onPoiSelected(NPMapView mapView, List<NPPoi> poiList) {
		super.onPoiSelected(mapView, poiList);
	}
}
