package com.ty.mapproject.activities;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.esri.core.geometry.Point;
import com.ty.mapdata.TYBuilding;
import com.ty.mapproject.R;
import com.ty.mapproject.settings.TYUserDefaults;
import com.ty.mapsdk.TYBuildingManager;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewListenser;
import com.ty.mapsdk.TYPictureMarkerSymbol;
import com.ty.mapsdk.TYPoi;

public abstract class BaseMapViewActivity extends Activity implements
		TYMapViewListenser {
	static final String TAG = BaseMapViewActivity.class.getSimpleName();

	TYMapView mapView;
	ListView floorListView;

	TYBuilding currentBuilding;
	List<TYMapInfo> mapInfos;
	TYMapInfo currentMapInfo;

	int currentFloorIndex;

	int contentViewID;

	long startLoadTime;
	long endLoadTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TYMapEnvironment.initMapEnvironment();

		getCurrentBuilding();

		initContentViewID();
		setContentView(contentViewID);

		initMapLayout();
	}

	public abstract void initContentViewID();

	private void getCurrentBuilding() {
		TYUserDefaults pref = new TYUserDefaults(this);
		String cityID = pref.getDefaultCityID();
		String buildingID = pref.getDefaultBuildingID();

		currentBuilding = TYBuildingManager.parseBuildingFromFilesById(this,
				cityID, buildingID);
		mapInfos = TYMapInfo.parseMapInfoFromFiles(this, cityID, buildingID);

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

		TYPictureMarkerSymbol pms = new TYPictureMarkerSymbol(getResources()
				.getDrawable(R.drawable.poi_showcase));
		pms.setWidth(15);
		pms.setHeight(15);
		System.out.println(pms + "");

		mapView.init(currentBuilding, dict.get("UserID"), dict.get("License"));
		// mapView.init(currentBuilding, "ty4e13f85911a44a75",
		// "d1a2d1461c0g31di60feaifff1f012b3");

		mapView.setPointSymbol(pms);

		startLoadTime = System.currentTimeMillis();
		Log.i(TAG, "Before Load:" + startLoadTime);
		mapView.setFloor(currentMapInfo);

		mapView.addMapListener(this);
	}

	protected void changedToFloor(int index) {
		// Log.i(TAG, "changedToFloor: " + index);
		currentFloorIndex = index;
		currentMapInfo = mapInfos.get(index);

		setTitle(String.format("%s-%s", currentBuilding.getName(),
				currentMapInfo.getFloorName()));

		startLoadTime = System.currentTimeMillis();
		Log.i(TAG, "Before Load:" + startLoadTime);
		mapView.setFloor(currentMapInfo);
	}

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {

	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		// Log.i(TAG, "onFinishLoadingFloor???" + mapInfo);
		endLoadTime = System.currentTimeMillis();
		Log.i(TAG, "After Load:" + endLoadTime);
		Log.i(TAG, "Load Time: " + (endLoadTime - startLoadTime) / 1000.0f);

	}

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {

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
}
