package com.ty.mapproject.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYDirectionalHint;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYPictureMarkerSymbol;
import com.ty.mapsdk.TYPoi;
import com.ty.mapsdk.TYRouteManager;
import com.ty.mapsdk.TYRouteManager.TYRouteManagerListener;
import com.ty.mapsdk.TYRoutePart;
import com.ty.mapsdk.TYRouteResult;

public class MapRouteActivity extends BaseMapViewActivity implements
		OnClickListener, TYRouteManagerListener {
	static final String TAG = MapRouteActivity.class.getSimpleName();

	Point currentPoint;
	TYLocalPoint startPoint;
	TYLocalPoint endPoint;

	TYRouteManager routeManager;
	boolean isRouting;
	TYRouteResult routeResult;

	TYRoutePart currentRoutePart;
	List<TYDirectionalHint> routeGuides;

	GraphicsLayer hintLayer;

	private Button startButton;
	private Button endButton;
	private Button requestRouteButton;
	private Button resetButton;

	TextView routeHintLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initMapSettings();
		initSymbols();

		routeManager = new TYRouteManager(currentBuilding,
				TYMapEnvironment.defaultUserCredentials(), mapInfos);
		routeManager.addRouteManagerListener(this);

		endPoint = new TYLocalPoint(1779.204079, 581.868337, 7);
		startPoint = new TYLocalPoint(1917, 558, 7);
	}

	@Override
	public void didSolveRouteWithResult(TYRouteManager routeManager,
			TYRouteResult rs) {
		Log.i(TAG, "didSolveRouteWithResult");
		hintLayer.removeAll();

		routeResult = rs;
		mapView.setRouteResult(rs);
		mapView.setRouteStart(startPoint);
		mapView.setRouteEnd(endPoint);

		mapView.showRouteResultOnCurrentFloor();

		List<TYRoutePart> routePartArray = routeResult
				.getRoutePartsOnFloor(currentMapInfo.getFloorNumber());
		if (routePartArray != null && routePartArray.size() > 0) {
			currentRoutePart = routePartArray.get(0);
		}

		if (currentRoutePart != null) {
			routeGuides = routeResult.getRouteDirectionalHint(currentRoutePart);
		}
	}

	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		if (isRouting) {
			mapView.showRouteResultOnCurrentFloor();
		}
	};

	@Override
	public void didFailSolveRouteWithError(TYRouteManager routeManager,
			Exception e) {
		Log.i(TAG, "didFailSolveRouteWithError");
	}

	@Override
	public void didRetrieveDefaultRouteTaskParameters(
			TYRouteManager routeManager) {
		Log.i(TAG, "didRetrieveDefaultRouteTaskParameters");

	}

	@Override
	public void didFailRetrieveDefaultRouteTaskParametersWithError(
			TYRouteManager routeManager, Exception e) {

	}

	private void initMapSettings() {
		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		// mapView.setOnSingleTapListener(this);
	}

	private void initSymbols() {
		TYPictureMarkerSymbol startSymbol = new TYPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.start));
		startSymbol.setWidth(34);
		startSymbol.setHeight(43);
		startSymbol.setOffsetX(0);
		startSymbol.setOffsetY(22);
		mapView.setStartSymbol(startSymbol);

		TYPictureMarkerSymbol endSymbol = new TYPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.end));
		endSymbol.setWidth(34);
		endSymbol.setHeight(43);
		endSymbol.setOffsetX(0);
		endSymbol.setOffsetY(22);
		mapView.setEndSymbol(endSymbol);

		TYPictureMarkerSymbol switchSymbol = new TYPictureMarkerSymbol(
				getResources().getDrawable(R.drawable.nav_exit));
		switchSymbol.setWidth(37);
		switchSymbol.setHeight(37);
		mapView.setSwitchSymbol(switchSymbol);
	}

	private void setStartPoint() {
		if (currentPoint == null) {
			return;
		}

		startPoint = new TYLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
	}

	private void setEndPoint() {
		if (currentPoint == null) {
			return;
		}

		endPoint = new TYLocalPoint(currentPoint.getX(), currentPoint.getY(),
				currentMapInfo.getFloorNumber());
		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);
	}

	private void requestRoute() {
		Log.i(TAG, "requestRoute");

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

		routeHintLabel = (TextView) findViewById(R.id.routeHintLabel);
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

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		super.onClickAtPoint(mapView, mappoint);

		// Point mappoint = mapView.toMapPoint(x, y);

		currentPoint = mappoint;

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 5,
				com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE);
		hintLayer.removeAll();
		hintLayer.addGraphic(new Graphic(mappoint, sms));

		Log.i(TAG, "Click: " + mappoint.getX() + ", " + mappoint.getY());

		TYLocalPoint localPoint = new TYLocalPoint(mappoint.getX(),
				mappoint.getY(), currentMapInfo.getFloorNumber());

		if (routeResult != null) {

			boolean isDeviating = routeResult.isDeviatingFromRoute(localPoint,
					10.0);
			if (isDeviating) {
				Toast.makeText(getBaseContext(), "已经偏离导航线，重新规划！",
						Toast.LENGTH_LONG).show();
				routeManager.requestRoute(localPoint, endPoint);
			} else {
				mapView.showRemainingRouteResultOnCurrentFloor(localPoint);
			}

			// NPRoutePart nearestPart = routeResult
			// .getNearestRoutePart(localPoint);
			// if (nearestPart != currentRoutePart) {
			// currentRoutePart = nearestPart;
			// routeGuides = routeResult
			// .getRouteDirectionalHint(currentRoutePart);
			// }
			//
			// if (routeGuides != null && routeGuides.size() > 0) {
			// NPDirectionalHint hint = routeResult
			// .getDirectionalHintForLocationFromHints(localPoint,
			// routeGuides);
			// mapView.showRouteHint(hint, true);
			//
			// String hintString = "";
			// if (hint.hasLandmark()) {
			// hintString += hint.getLandmarkString();
			// }
			// hintString += hint.getDirectionString();
			//
			// routeHintLabel.setText(hintString);
			// }
		}
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		super.onPoiSelected(mapView, poiList);
	}

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {
		super.mapViewDidZoomed(mapView);

		if (isRouting) {
			mapView.showRouteResultOnCurrentFloor();
		}
	}

}
