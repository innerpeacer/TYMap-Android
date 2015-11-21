package com.ty.mapproject.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewMode;

public class PathCalibrationActivity extends BaseMapViewActivity {

	GraphicsLayer testLayer;
	GraphicsLayer hintLayer;
	GraphicsLayer pathLayer = null;

	// IPPathCalibration pathCalibration = null;
	Point testLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// mapView.setHighlightPoiOnSelection(false);

		pathLayer = new GraphicsLayer();
		mapView.addLayer(pathLayer);

		testLayer = new GraphicsLayer();
		mapView.addLayer(testLayer);

		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		mapView.setMapMode(TYMapViewMode.TYMapViewModeFollowing);
		mapView.setAllowRotationByPinch(true);
		mapView.setPathCalibrationEnabled(true);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_path_calibration;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " + mappoint.getY());
		Log.i(TAG, "Center: " + mapView.getCenter().getX() + ", "
				+ mapView.getCenter().getY());

		hintLayer.removeAll();
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5,
				STYLE.CIRCLE);
		hintLayer.addGraphic(new Graphic(mappoint, sms));

		testLocation = mapView.getCalibratedPoint(mappoint);
		mapView.showLocation(new TYLocalPoint(testLocation.getX(), testLocation
				.getY(), currentMapInfo.getFloorNumber()));

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);

		// pathCalibration = new IPPathCalibration(mapInfo);

		// pathLayer.removeAll();
		// SimpleFillSymbol sfs = new SimpleFillSymbol(Color.YELLOW);
		// pathLayer
		// .addGraphic(new Graphic(pathCalibration.getUnionPolygon(), sfs));
		// SimpleLineSymbol sls = new SimpleLineSymbol(Color.RED, 2);
		// pathLayer.addGraphic(new Graphic(pathCalibration.getUnionPath(),
		// sls));
	}
}
