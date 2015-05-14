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
import cn.nephogram.mapsdk.route.NPRouteManager;
import cn.nephogram.mapsdk.route.NPRouteManager.NPRouteManagerListener;
import cn.nephogram.mapsdk.route.NPRouteResult;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

public class NephogramMapRouteActivity extends BaseMapViewActivity implements
		OnClickListener, OnSingleTapListener, NPRouteManagerListener {
	static final String TAG = NephogramMapRouteActivity.class.getSimpleName();

	private static final long serialVersionUID = 5847083145291274344L;

	Point currentPoint;
	NPLocalPoint startPoint;
	NPLocalPoint endPoint;

	NPRouteManager routeManager;
	boolean isRouting;
	NPRouteResult routeResult;

	GraphicsLayer hintLayer;

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
	}

	@Override
	public void didSolveRouteWithResult(NPRouteManager routeManager,
			NPRouteResult rs) {
		Log.i(TAG, "didSolveRouteWithResult");
		hintLayer.removeAll();

		routeResult = rs;
		mapView.setRouteResult(rs);
		mapView.setRouteStart(startPoint);
		mapView.setRouteEnd(endPoint);

		mapView.showRouteResultOnCurrentFloor();
	}

	public void onFinishLoadingFloor(NPMapView mapView, NPMapInfo mapInfo) {
		if (isRouting) {
			mapView.showRouteResultOnCurrentFloor();
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

	private void initMapSettings() {
		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		mapView.setOnSingleTapListener(this);
	}

	private void initSymbols() {
		NPPictureMarkerSymbol startSymbol = new NPPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.start));
		startSymbol.setWidth(34);
		startSymbol.setHeight(43);
		startSymbol.setOffsetX(0);
		startSymbol.setOffsetY(22);
		mapView.setStartSymbol(startSymbol);

		NPPictureMarkerSymbol endSymbol = new NPPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.end));
		endSymbol.setWidth(34);
		endSymbol.setHeight(43);
		endSymbol.setOffsetX(0);
		endSymbol.setOffsetY(22);
		mapView.setEndSymbol(endSymbol);

		NPPictureMarkerSymbol switchSymbol = new NPPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.nav_exit));
		switchSymbol.setWidth(37);
		switchSymbol.setHeight(37);
		mapView.setSwitchSymbol(switchSymbol);
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
		if (currentPoint == null) {
			return;
		}

		startPoint = new NPLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
	}

	private void setEndPoint() {
		if (currentPoint == null) {
			return;
		}

		endPoint = new NPLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);
	}

	private void requestRoute() {
		if (startPoint == null || endPoint == null) {
			Toast.makeText(getBaseContext(), "需要两个点请求路径！", Toast.LENGTH_LONG)
					.show();
			return;
		}

		mapView.resetRouteLayer();

		routeResult = null;
		isRouting = true;

		routeManager.requestRoute(startPoint, endPoint);
	}

	private void reset() {
		hintLayer.removeAll();

		startPoint = null;
		endPoint = null;
		currentPoint = null;

		mapView.resetRouteLayer();
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
