package cn.nephogram.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import cn.nephogram.map.R;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.poi.NPPoi;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class NephogramMapActivity extends BaseMapViewActivity {
	static final String TAG = NephogramMapActivity.class.getSimpleName();

	GraphicsLayer graphicsLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView.setHighlightPoiOnSelection(true);

		graphicsLayer = new GraphicsLayer();
		mapView.addLayer(graphicsLayer);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(NPMapView mapView, Point mappoint) {

		Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " + mappoint.getY());
		Log.i(TAG, "Center: " + mapView.getCenter().getX() + ", "
				+ mapView.getCenter().getY());

		graphicsLayer.removeAll();
		graphicsLayer.addGraphic(new Graphic(mappoint, new SimpleMarkerSymbol(
				Color.GREEN, 5, STYLE.CIRCLE)));

		// mapView.translateInMapUnit(5, 5, true);
		// mapView.translateInScreenUnit(300, 300, true);

		// Rect rect = new Rect(100, 100, 200, 200);
		// Point testPoint = new Point(1.3523012707863146E7,
		// 3662129.5815833337);

		// mapView.restrictLocation(testPoint, rect, true);

		List<Integer> allFacilitiesIDs = mapView
				.getAllFacilityCategoryIDOnCurrentFloor();

		// Log.i(TAG, allFacilitiesIDs.toString());

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
	public void onPoiSelected(NPMapView mapView, List<NPPoi> poiList) {

	}
}
