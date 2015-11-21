package com.ty.mapproject.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYOfflineRouteManager;
import com.ty.mapsdk.TYOfflineRouteManager.TYOfflineRouteManagerListener;
import com.ty.mapsdk.TYPictureMarkerSymbol;
import com.ty.mapsdk.TYPoi;
import com.ty.mapsdk.TYRoutePart;
import com.ty.mapsdk.TYRouteResult;

public class MapOfflineRouteActivity extends BaseMapViewActivity implements
		TYOfflineRouteManagerListener {
	static final String TAG = MapOfflineRouteActivity.class.getSimpleName();

	Point currentPoint;
	TYLocalPoint startPoint;
	TYLocalPoint endPoint;

	TYOfflineRouteManager offlineRouteManager;
	boolean isRouting;
	TYRouteResult routeResult;
	GraphicsLayer hintLayer;

	boolean isRouteManagerReady = false;

	boolean useClickForSnapRoute = false;
	boolean useClickForChoosingPoint = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initMapSettings();
		initSymbols();

		new Thread(new Runnable() {
			@Override
			public void run() {
				offlineRouteManager = new TYOfflineRouteManager(
						currentBuilding, mapInfos);
				offlineRouteManager
						.addRouteManagerListener(MapOfflineRouteActivity.this);
				isRouteManagerReady = true;
			}
		}).start();

	}

	@Override
	public void didSolveRouteWithResult(TYOfflineRouteManager routeManager,
			TYRouteResult rs) {
		Log.i(TAG, "TYOfflineRouteManager.didSolveRouteWithResult()");
		Log.i(TAG, rs + "");
		hintLayer.removeAll();

		routeResult = rs;
		mapView.setRouteResult(rs);
		mapView.setRouteStart(startPoint);
		mapView.setRouteEnd(endPoint);

		mapView.showRouteResultOnCurrentFloor();
	}

	@Override
	public void didFailSolveRouteWithError(TYOfflineRouteManager routeManager,
			Exception e) {
		Log.i(TAG, "TYOfflineRouteManager.didFailSolveRouteWithError");

	}

	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		if (isRouting) {
			List<TYRoutePart> parts = routeResult.getRoutePartsOnFloor(mapInfo
					.getFloorNumber());
			if (parts != null && parts.size() > 0) {
				TYRoutePart rp = parts.get(0);
				mapView.setExtent(rp.getRoute());
				// mapView.checkLabels();
			}

			mapView.showRouteResultOnCurrentFloor();
		}
	};

	private void initMapSettings() {
		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);
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

	private void requestRoute() {
		// Log.i(TAG, "requestRoute");

		if (!isRouteManagerReady) {
			return;
		}

		if (startPoint == null || endPoint == null) {
			// Toast.makeText(getBaseContext(), "需要两个点请求路径！", Toast.LENGTH_LONG)
			// .show();
			return;
		}
		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);

		mapView.resetRouteLayer();

		routeResult = null;
		isRouting = true;

		Log.i(TAG, startPoint.getX() + "");
		Log.i(TAG, startPoint.getY() + "");

		// startPoint = new TYLocalPoint(13402386.8918, 4287405.314801339, 1);
		// endPoint = new TYLocalPoint(13402418.04687074, 4287369.954955366, 1);

		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);
		offlineRouteManager.requestRoute(startPoint, endPoint);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_offline_route;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		super.onClickAtPoint(mapView, mappoint);
		currentPoint = mappoint;

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 5,
				com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE);
		hintLayer.removeAll();
		hintLayer.addGraphic(new Graphic(mappoint, sms));

		Log.i(TAG, "Click: " + mappoint.getX() + ", " + mappoint.getY());

		TYLocalPoint localPoint = new TYLocalPoint(mappoint.getX(),
				mappoint.getY(), currentMapInfo.getFloorNumber());

		if (useClickForChoosingPoint) {
			endPoint = startPoint;
			startPoint = localPoint;
			requestRoute();
		}

		if (useClickForSnapRoute) {
			// mapView.showRemainingRouteResultOnCurrentFloor(localPoint);
			mapView.showPassedAndRemainingRouteResultOnCurrentFloor(localPoint);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Set Point for Navigation");
		menu.add(1, 1, 1, "Snap to Route");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			useClickForChoosingPoint = true;
			useClickForSnapRoute = false;
			break;

		case 1:
			useClickForChoosingPoint = false;
			useClickForSnapRoute = true;
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
