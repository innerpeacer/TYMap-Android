package com.ty.mapproject.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewMode;
import com.ty.mapsdk.poi.TYPoi;

public class MapActivity extends BaseMapViewActivity {
	static final String TAG = MapActivity.class.getSimpleName();

	GraphicsLayer graphicsLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView.setHighlightPoiOnSelection(true);

		graphicsLayer = new GraphicsLayer();
		mapView.addLayer(graphicsLayer);

		mapView.setMapMode(TYMapViewMode.TYMapViewModeFollowing);

		// List<NPBrand> allBrands = NPBrand.parseAllBrands(currentBuilding);
		// Log.i(TAG, allBrands.toString());
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

		graphicsLayer.removeAll();
		graphicsLayer.addGraphic(new Graphic(mappoint, new SimpleMarkerSymbol(
				Color.GREEN, 5, STYLE.CIRCLE)));

		List<Integer> allFacilitiesIDs = mapView
				.getAllFacilityCategoryIDOnCurrentFloor();

		if (index > allFacilitiesIDs.size()) {
			index = 0;
		}

		if (index == allFacilitiesIDs.size()) {
			mapView.showAllFacilitiesOnCurrentFloor();
			index++;
		} else if (allFacilitiesIDs.size() > 0) {
			Log.i(TAG,
					"Current Index: " + index + ", "
							+ allFacilitiesIDs.get(index));
			mapView.showFacilityOnCurrentWithCategory(allFacilitiesIDs
					.get(index++));
		}
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}
}
