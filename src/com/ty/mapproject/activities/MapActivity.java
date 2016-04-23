package com.ty.mapproject.activities;

import java.util.List;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mapView.setHighlightPoiOnSelection(true);
		// mapView.setLabelOverlapDetectingEnabled(false);

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
		Log.i(TAG, "MapScale: " + mapView.getScale());

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);

	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}
}
