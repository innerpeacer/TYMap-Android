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
import cn.nephogram.mapsdk.NPPictureSymbol;
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
	private static String ROUTE_TASK_URL = "http://121.40.16.26:6080/arcgis/rest/services/002100002/NAServer/Route";

	Point currentPoint;
	NPLocalPoint startPoint;
	NPLocalPoint endPoint;

	NPRouteManager routeManager;
	NPRouteLayer routeLayer;

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

		// routeManager = new CARouteManager(ROUTE_TASK_URL,
		// CloudMapEnvironment.defaultUserCredentials());
		routeManager = new NPRouteManager(ROUTE_TASK_URL,
				NPMapEnvironment.defaultUserCredentials(), mapInfos);
		routeManager.addRouteManagerListener(this);
		routeSymbol = createRouteSymbol();
	}

	@Override
	public void didSolveRouteWithResult(NPRouteManager routeManager,
			NPRouteResult rs) {
		Log.i(TAG, "didSolveRouteWithResult");
		// routeLayer.addGraphic(new Graphic(routeResultGraphic.getGeometry(),
		// createRouteSymbol()));

		// routeLayer.addGraphic(new Graphic(routeResultGraphic.getGeometry(),
		// routeSymbol));

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
			}
		}
	}

	@Override
	protected void changedToFloor(int index) {
		super.changedToFloor(index);
		if (isRouting) {
			showRouteResultOnCurrentFloor();
		}
	}

	@Override
	public void didFailSolveRouteWithError(NPRouteManager routeManager,
			Exception e) {

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
		startLayer = new GraphicsLayer();
		mapView.addLayer(startLayer);

		endLayer = new GraphicsLayer();
		mapView.addLayer(endLayer);

		routeLayer = new NPRouteLayer();
		mapView.addLayer(routeLayer);

		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		mapView.setOnSingleTapListener(this);
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

		NPPictureSymbol psStart = new NPPictureSymbol(getResources()
				.getDrawable(R.drawable.green_pushpin));
		psStart.setWidth(36);
		psStart.setHeight(36);
		psStart.setOffsetX(9);
		psStart.setOffsetY(16);

		startLayer.addGraphic(new Graphic(currentPoint, psStart));
	}

	private void setEndPoint() {
		endPoint = new NPLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		endLayer.removeAll();

		NPPictureSymbol psEnd = new NPPictureSymbol(getResources().getDrawable(
				R.drawable.red_pushpin));
		psEnd.setWidth(36);
		psEnd.setHeight(36);
		psEnd.setOffsetX(9);
		psEnd.setOffsetY(16);

		endLayer.addGraphic(new Graphic(currentPoint, psEnd));
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
