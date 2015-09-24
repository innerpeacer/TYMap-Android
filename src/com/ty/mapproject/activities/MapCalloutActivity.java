package com.ty.mapproject.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYPoi;

public class MapCalloutActivity extends BaseMapViewActivity {
	static final String TAG = MapCalloutActivity.class.getSimpleName();

	protected Callout mapCallout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mapCallout = mapView.getCallout();
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_callout;
	}

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		// Log.i(TAG, "onClickAtPoint: " + mappoint);

	}

	private View loadCalloutView(String title, String detail) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.layout_callout, null);
		TextView titleView = (TextView) view.findViewById(R.id.callout_title);
		titleView.setText(title);
		TextView detailView = (TextView) view.findViewById(R.id.callout_detail);
		detailView.setText(detail);
		return view;
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		Log.i(TAG, "onPoiSelected: " + poiList);
		mapCallout.setStyle(R.xml.callout_style);
		if (mapCallout != null && mapCallout.isShowing()) {
			mapCallout.hide();
		}

		if (poiList != null && poiList.size() > 0) {
			TYPoi poi = poiList.get(0);

			Point location;
			if (poi.getGeometry().getClass() == Polygon.class) {
				location = GeometryEngine.getLabelPointForPolygon(
						(Polygon) poi.getGeometry(),
						TYMapEnvironment.defaultSpatialReference());
			} else {
				location = (Point) poi.getGeometry();
			}

			Log.i(TAG, location.toString());

			String title = poi.getName();
			String detail = poi.getPoiID();

			mapCallout.setContent(loadCalloutView(title, detail));
			mapCallout.show(location);
		}

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		// Log.i(TAG, "onFinishLoadingFloor" + mapInfo);
	}

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {
		// Log.i(TAG, "mapViewDidZoomed");

	}
}
