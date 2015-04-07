package cn.nephogram.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import cn.nephogram.datamanager.NPFileManager;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.NPAreaAnalysis;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.poi.NPPoi;

import com.esri.core.geometry.Point;

public class NephogramMapActivity extends BaseMapViewActivity {
	static final String TAG = NephogramMapActivity.class.getSimpleName();
	NPAreaAnalysis areaAnalysis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		areaAnalysis = new NPAreaAnalysis(NPFileManager.getAOIJsonPath());
		Log.i(TAG, "Area Count: " + areaAnalysis.getAreaCount());
		// mapView.setHighlightPoiOnSelection(true);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(NPMapView mapView, Point mappoint) {

		// NPPoi poi = mapView.extractRoomPoiOnCurrentFloor(mappoint.getX(),
		// mappoint.getY());
		//
		// Log.i(TAG, poi + "\n");
		// if (poi != null) {
		// mapView.highlightPoi(poi);
		// }
		//
		// Log.i(TAG, mapView.getScale() + "");

		List<NPPoi> poiArray = areaAnalysis.extractAOIs(mappoint.getX(),
				mappoint.getY());
		Log.i(TAG, poiArray.size() + " area extracted");
		Log.i(TAG, poiArray + "");

	}

	@Override
	public void onPoiSelected(NPMapView mapView, List<NPPoi> poiList) {
		// Log.i(TAG, "onPoiSelected: ");
		// for (NPPoi poi : poiList) {
		// Log.i(TAG, poi + "\n");
		// }

	}
}
