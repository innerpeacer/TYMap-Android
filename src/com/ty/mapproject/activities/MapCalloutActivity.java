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

		// 获取地图Callout组件
		mapCallout = mapView.getCallout();
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_callout;
	}

	// 点击地图回调方法
	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		Log.i(TAG, "onClickAtPoint: " + mappoint);
		TYPoi poi = mapView.extractRoomPoiOnCurrentFloor(mappoint.getX(),
				mappoint.getY());
		if (poi == null) {
			Log.i(TAG, "poi: " + null);
		} else {
			Log.i(TAG, "poi: " + poi);
		}
	}

	// 加载自定义弹出框内容
	private View loadCalloutView(String title, String detail) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.layout_callout, null);
		TextView titleView = (TextView) view.findViewById(R.id.callout_title);
		titleView.setText(title);
		TextView detailView = (TextView) view.findViewById(R.id.callout_detail);
		detailView.setText(detail);
		return view;
	}

	// 点击选中POI回调方法
	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		Log.i(TAG, "onPoiSelected: " + poiList.size());
		Log.i(TAG, "" + poiList);

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

			mapCallout.setMaxWidth(1500);
			mapCallout.setMaxHeight(1300);
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
