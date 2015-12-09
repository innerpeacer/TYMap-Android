package com.ty.mapproject.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewMode;
import com.ty.mapsdk.TYPoi;

public class MapActivity extends BaseMapViewActivity {
	static final String TAG = MapActivity.class.getSimpleName();

	GraphicsLayer graphicsLayer;

	// ==== Test Parking ========
	String[] targetParkingSpaces = { "00100003B0210266", "00100003B0210281",
			"00100003B0210258", "00100003B0210262", "00100003B0210279",
			"00100003B0210265", "00100003B0210263", "00100003B0210280",
			"00100003B0210260", "00100003B0210275", "00100003B0210286",
			"00100003B0210274", "00100003B0210273", "00100003B0210285",
			"00100003B0210271", "00100003B0210268", "00100003B0210290",
			"00100003B0210269" };
	List<String> occupiedParkingSpaces = new ArrayList<String>();
	List<String> availableParkingSpaces = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Random random = new Random();
		for (String poiID : targetParkingSpaces) {
			boolean status = random.nextBoolean();
			if (status) {
				occupiedParkingSpaces.add(poiID);
			} else {
				availableParkingSpaces.add(poiID);
			}
		}

		mapView.setHighlightPoiOnSelection(true);
		mapView.setLabelOverlapDetectingEnabled(false);

		graphicsLayer = new GraphicsLayer();
		mapView.addLayer(graphicsLayer);

		mapView.setMapMode(TYMapViewMode.TYMapViewModeFollowing);
		mapView.setAllowRotationByPinch(true);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {

		Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " + mappoint.getY());
		Log.i(TAG, "Center: " + mapView.getCenter().getX() + ", "
				+ mapView.getCenter().getY());

		// graphicsLayer.removeAll();
		// graphicsLayer.addGraphic(new Graphic(mappoint, new
		// SimpleMarkerSymbol(
		// Color.GREEN, 5, STYLE.CIRCLE)));
		//
		// List<Integer> allFacilitiesIDs = mapView
		// .getAllFacilityCategoryIDOnCurrentFloor();
		//
		// if (index > allFacilitiesIDs.size()) {
		// index = 0;
		// }
		//
		// if (index == allFacilitiesIDs.size()) {
		// mapView.showAllFacilitiesOnCurrentFloor();
		// index++;
		// } else if (allFacilitiesIDs.size() > 0) {
		// Log.i(TAG,
		// "Current Index: " + index + ", "
		// + allFacilitiesIDs.get(index));
		// mapView.showFacilityOnCurrentWithCategory(allFacilitiesIDs
		// .get(index++));
		// }
	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);

		mapView.showOccupiedAndAvailableParkingSpaces(occupiedParkingSpaces,
				availableParkingSpaces);
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}
}
