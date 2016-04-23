package com.ty.mapproject.activities;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYCity;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapproject.settings.TYUserDefaults;
import com.ty.mapsdk.TYBuildingManager;
import com.ty.mapsdk.TYCityManager;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewListenser;
import com.ty.mapsdk.TYOfflineRouteManager;
import com.ty.mapsdk.TYOfflineRouteManager.TYOfflineRouteManagerListener;
import com.ty.mapsdk.TYPictureMarkerSymbol;
import com.ty.mapsdk.TYPoi;
import com.ty.mapsdk.TYRouteResult;

public class BuildingMapViewActivity extends Activity implements
		TYMapViewListenser, TYOfflineRouteManagerListener {

	static final String TAG = BaseMapViewActivity.class.getSimpleName();

	TYMapView mapView;
	ListView floorListView;

	TYCity currentCity;
	TYBuilding currentBuilding;
	List<TYMapInfo> mapInfos;
	TYMapInfo currentMapInfo;

	int currentFloorIndex;

	int contentViewID;

	// =========== Route Manager ===================
	Point currentPoint;
	TYLocalPoint startPoint;
	TYLocalPoint endPoint;

	TYOfflineRouteManager offlineRouteManager;
	boolean isRouting;
	TYRouteResult routeResult;
	GraphicsLayer hintLayer;

	boolean isRouteManagerReady = false;

	long startLoadTime;
	long endLoadTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startLoadTime = System.currentTimeMillis();
		Log.i(TAG, "Before Load:" + startLoadTime);

		TYMapEnvironment.initMapEnvironment();

		String cityID = getIntent().getStringExtra("cityID");
		String buildingID = getIntent().getStringExtra("buildingID");

		currentCity = TYCityManager.parseCityFromFilesById(this, cityID);
		currentBuilding = TYBuildingManager.parseBuildingFromFilesById(this,
				cityID, buildingID);
		mapInfos = TYMapInfo.parseMapInfoFromFiles(this, cityID, buildingID);

		Log.i(TAG, currentBuilding.toString());
		Log.i(TAG, mapInfos.toString());

		setContentView(R.layout.activity_building_map_view);
		initMapLayout();
		initSymbols();
		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		// String dbName = String.format("%s_Route.db",
		// currentBuilding.getBuildingID());
		// File file = new File(
		// TYMapEnvironment.getDirectoryForBuilding(currentBuilding),
		// dbName);
		// if (file.exists()) {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// offlineRouteManager = new TYOfflineRouteManager(
		// currentBuilding, mapInfos);
		// offlineRouteManager
		// .addRouteManagerListener(BuildingMapViewActivity.this);
		// isRouteManagerReady = true;
		// }
		// }).start();
		// } else {
		// Toast.makeText(getBaseContext(), "当前建筑暂不支持路径规划！", Toast.LENGTH_LONG)
		// .show();
		// isRouteManagerReady = false;
		// }

	}

	private void initMapLayout() {
		mapView = (TYMapView) findViewById(R.id.map);
		mapView.setEsriLogoVisible(false);

		floorListView = (ListView) findViewById(R.id.list_floor);

		if (mapInfos.size() == 0) {
			floorListView.setVisibility(View.GONE);
			return;
		} else if (mapInfos.size() == 1) {
			floorListView.setVisibility(View.GONE);

			currentMapInfo = mapInfos.get(0);
			currentFloorIndex = 0;
		} else if (mapInfos.size() > 1) {

			currentFloorIndex = 0;
			currentMapInfo = mapInfos.get(0);

			for (int i = 0; i < mapInfos.size(); i++) {
				TYMapInfo info = mapInfos.get(i);
				// if (info.getFloorName().equalsIgnoreCase("F1")) {
				if (info.getFloorNumber() == 7) {
					currentFloorIndex = i;
					currentMapInfo = info;
					break;
				}
				// }
			}

			FloorListAdatper fAdatper = new FloorListAdatper(this);
			fAdatper.setData(mapInfos);
			floorListView.setAdapter(fAdatper);

			floorListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == currentFloorIndex) {
						return;
					}

					changedToFloor(position);
				}
			});
		}

		setTitle(String.format("%s-%s", currentBuilding.getName(),
				currentMapInfo.getFloorName()));

		Map<String, String> dict = LicenseManager
				.getLicenseForBuilding(currentBuilding.getBuildingID());
		mapView.init(currentBuilding, dict.get("UserID"), dict.get("License"));

		mapView.setFloor(currentMapInfo);
		mapView.addMapListener(this);
	}

	protected void changedToFloor(int index) {
		currentFloorIndex = index;
		currentMapInfo = mapInfos.get(index);

		setTitle(String.format("%s-%s", currentBuilding.getName(),
				currentMapInfo.getFloorName()));
		mapView.setFloor(currentMapInfo);
	}

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		currentPoint = mappoint;

		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 5,
				com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE);
		hintLayer.removeAll();
		hintLayer.addGraphic(new Graphic(mappoint, sms));

		Log.i(TAG, "Click: " + mappoint.getX() + ", " + mappoint.getY());

		TYLocalPoint localPoint = new TYLocalPoint(mappoint.getX(),
				mappoint.getY(), currentMapInfo.getFloorNumber());

		endPoint = startPoint;
		startPoint = localPoint;
		requestRoute();
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
		Log.i(TAG, "requestRoute");

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

		offlineRouteManager.requestRoute(startPoint, endPoint);
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}

	@Override
	public void didSolveRouteWithResult(TYOfflineRouteManager routeManager,
			TYRouteResult rs) {
		Log.i(TAG, "TYOfflineRouteManager.didSolveRouteWithResult()");
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
		endLoadTime = System.currentTimeMillis();
		Log.i(TAG, "After Load:" + endLoadTime);
		Log.i(TAG, "Load Time: " + (endLoadTime - startLoadTime) / 1000.0f);
		if (isRouting) {
			mapView.showRouteResultOnCurrentFloor();
		}
	};

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {
		if (isRouting) {
			mapView.showRouteResultOnCurrentFloor();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.unpause();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.pause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Set As Default");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			TYUserDefaults defaults = new TYUserDefaults(getBaseContext());
			defaults.setDefaultBuildingID(currentBuilding.getBuildingID());
			defaults.setDefaultCityID(currentBuilding.getCityID());
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
