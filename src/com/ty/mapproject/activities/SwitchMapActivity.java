package com.ty.mapproject.activities;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.ty.mapdata.TYBuilding;
import com.ty.mapproject.R;
import com.ty.mapproject.settings.TYUserDefaults;
import com.ty.mapsdk.TYBuildingManager;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewListenser;
import com.ty.mapsdk.TYPoi;

public class SwitchMapActivity extends Activity implements TYMapViewListenser {
	static final String TAG = SwitchMapActivity.class.getSimpleName();

	TYMapView mapView;
	ListView floorListView;

	TYBuilding currentBuilding;
	List<TYMapInfo> mapInfos;
	TYMapInfo currentMapInfo;

	int currentFloorIndex;
	int contentViewID;

	GraphicsLayer graphicsLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TYMapEnvironment.initMapEnvironment();
		TYUserDefaults pref = new TYUserDefaults(this);
		String cityID = pref.getDefaultCityID();
		String buildingID = pref.getDefaultBuildingID();

		currentBuilding = TYBuildingManager.parseBuildingFromFilesById(this,
				cityID, buildingID);
		mapInfos = TYMapInfo.parseMapInfoFromFiles(this, cityID, buildingID);

		setContentView(R.layout.activity_building_map_view);
		mapView = (TYMapView) findViewById(R.id.map);
		mapView.setEsriLogoVisible(false);
		initMapLayout();

		graphicsLayer = new GraphicsLayer();
		mapView.addLayer(graphicsLayer);
		mapView.setAllowRotationByPinch(true);
	}

	private void initMapLayout() {
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

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " + mappoint.getY());
		Log.i(TAG, "Center: " + mapView.getCenter().getX() + ", "
				+ mapView.getCenter().getY());
		Log.i(TAG, "MapScale: " + mapView.getScale());

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		Log.i(TAG, "onFinishLoadingFloor：" + mapInfo);
		mapView.setExtent(new Envelope(mapInfo.getMapExtent().getXmin(),
				mapInfo.getMapExtent().getYmin(), mapInfo.getMapExtent()
						.getXmax(), mapInfo.getMapExtent().getYmax()));
		double width = 0.06; // 6cm
		mapView.setMinScale(mapInfo.getMapSize().getX() / width);
		mapView.setMaxScale(6 / width);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Switch");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Log.i(TAG, "0");
			startSwitchActiviy();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	void startSwitchActiviy() {
		Intent intent = new Intent();
		intent.setClass(SwitchMapActivity.this, AllBuildingListAcitivity.class);
		startActivityForResult(intent, 123);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String cityID = data.getStringExtra("cityID");
		String buidlingID = data.getStringExtra("buildingID");

		if (!currentBuilding.getBuildingID().equals(buidlingID)) {
			switchBuilding(cityID, buidlingID);
		}
	}

	void switchBuilding(String cityID, String buildingID) {
		currentBuilding = TYBuildingManager.parseBuildingFromFilesById(this,
				cityID, buildingID);
		mapInfos = TYMapInfo.parseMapInfoFromFiles(this, cityID, buildingID);

		// floorListView = (ListView) findViewById(R.id.list_floor);

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
		mapView.switchBuilding(currentBuilding, dict.get("UserID"),
				dict.get("License"));
		mapView.setFloor(currentMapInfo);
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
	}

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {
	}
}
