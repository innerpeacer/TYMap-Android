package com.ty.mapproject.activities;

import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYPictureMarkerSymbol;
import com.ty.mapsdk.TYPoi;

public class MapActivity extends BaseMapViewActivity {
	static final String TAG = MapActivity.class.getSimpleName();

	// RelativeLayout layout;
	GraphicsLayer graphicsLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		coverView = (View) findViewById(R.id.coverView);
		// layout = (RelativeLayout) findViewById(R.id.rootLayout);

		mapView.setHighlightPoiOnSelection(false);
		// mapView.setLabelOverlapDetectingEnabled(false);

		graphicsLayer = new GraphicsLayer();
		mapView.addLayer(graphicsLayer);

		// mapView.setMapMode(TYMapViewMode.TYMapViewModeFollowing);
		// mapView.setMapMode(TYMapViewMode.TYMapViewModeDefault);

		mapView.setAllowRotationByPinch(true);

		// coverView.setAlpha(0.0f);

		TYPictureMarkerSymbol pms = new TYPictureMarkerSymbol(getResources()
				.getDrawable(R.drawable.location_arrow));
		pms.setWidth(40.0f);
		pms.setHeight(40.0f);
		mapView.setLocationSymbol(pms);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;
	float angle = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		// Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " +
		// mappoint.getY());
		// Log.i(TAG, "Center: " + mapView.getCenter().getX() + ", "
		// + mapView.getCenter().getY());
		// Log.i(TAG, "MapScale: " + mapView.getScale());
		// Log.i(TAG, "Resolution: " + mapView.getResolution());

		TYPoi poi = mapView.extractRoomPoiOnCurrentFloor(mappoint.getX(),
				mappoint.getY());
		if (poi != null) {
			// System.out.println(poi.getCategoryID());
			// System.out.println(poi.getPoiID());
			mapView.highlightPoi(poi);
		}
		// Log.i(TAG, poi.toString());

		angle += 30;
		mapView.processDeviceRotation(angle);
		mapView.showLocation(new TYLocalPoint(mappoint.getX(), mappoint.getY(),
				currentMapInfo.getFloorNumber()));

		TYPictureMarkerSymbol pms = new TYPictureMarkerSymbol(getResources()
				.getDrawable(R.drawable.location_arrow));
		pms.setWidth(40.0f);
		pms.setHeight(40.0f);
		pms.setAngle(angle);

		graphicsLayer.removeAll();
		graphicsLayer.addGraphic(new Graphic(new Point(mappoint.getX(),
				mappoint.getY() + 2), pms));
	}

	View coverView;

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				coverView.setAlpha(0.0f);
			}
		});
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		mapView.highlightPois(poiList);
	}
}
