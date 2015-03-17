package cn.nephogram.activities;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.poi.NPPoi;

import com.esri.core.geometry.Point;

public class NephogramMapActivity extends BaseMapViewActivity {
	static final String TAG = NephogramMapActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(Point mappoint) {
		Log.i(TAG,
				"onClickAtPoint: " + mappoint.getX() + ", " + mappoint.getY());

		List<Integer> cList = mapView.getAllFacilityCategoryIDOnCurrentFloor();

		Log.i(TAG, cList.toString());

		if (index > cList.size()) {
			index = 0;
		}

		if (index == cList.size()) {
			mapView.showAllFacilitiesOnCurrentFloor();
			index++;
		} else {
			mapView.showFacilityOnCurrentWithCategory(cList.get(index++));
		}
	}

	@Override
	public void onPoiSelected(List<NPPoi> poiList) {
		Log.i(TAG, "onPoiSelected: ");
		for (NPPoi poi : poiList) {
			Log.i(TAG, poi + "\n");
		}

	}
}
