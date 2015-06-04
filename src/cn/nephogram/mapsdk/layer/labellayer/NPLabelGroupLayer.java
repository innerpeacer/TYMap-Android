package cn.nephogram.mapsdk.layer.labellayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.datamanager.NPMapFileManager;
import cn.nephogram.mapsdk.poi.NPBrand;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GroupLayer;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;

public class NPLabelGroupLayer extends GroupLayer {
	static final String TAG = NPLabelGroupLayer.class.getSimpleName();

	private NPMapView mapView;

	private NPFacilityLayer facilityLayer;
	private NPTextLabelLayer labelLayer;

	private List<NPLabelBorder> visiableBorders = new ArrayList<NPLabelBorder>();

	public NPLabelGroupLayer(Context context, NPMapView mapView,
			NPRenderingScheme renderingScheme, SpatialReference sr) {
		super();

		this.mapView = mapView;

		facilityLayer = new NPFacilityLayer(context, this, renderingScheme, sr,
				null);
		addLayer(facilityLayer);

		labelLayer = new NPTextLabelLayer(context, this, sr, null);
		addLayer(labelLayer);
	}

	public void setBrandDict(Map<String, NPBrand> dict) {
		labelLayer.setBrandDict(dict);
	}

	public NPMapView getMapView() {
		return mapView;
	}

	public void updateLabels() {
		Log.i(TAG, "updateLabels");
		visiableBorders.clear();

		facilityLayer.updateLabels(visiableBorders);
		labelLayer.updateLabels(visiableBorders);
	}

	public void removeGraphicsFromSublayers() {
		facilityLayer.removeAll();
		labelLayer.removeAll();
	}

	public void loadFacilityContentsFromFileWithInfo(NPMapInfo info) {
		facilityLayer.loadContentsFromFileWithInfo(NPMapFileManager
				.getFacilityFilePath(info));
	}

	public void loadLabelContentsFromFileWithInfo(NPMapInfo info) {
		labelLayer.loadContentsFromFileWithInfo(NPMapFileManager
				.getLabelFilePath(info));
	}

	public void clearSelection() {
		facilityLayer.clearSelection();
		facilityLayer.showAllFacilities();
	}

	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return facilityLayer.getAllFacilityCategoryIDOnCurrentFloor();
	}

	public void showAllFacilities() {
		facilityLayer.showAllFacilities();
	}

	public void showFacilityWithCategory(int categoryID) {
		facilityLayer.showFacilityWithCategory(categoryID);
	}

	public NPPoi getPoiWithPoiID(String pid, POI_LAYER layer) {
		NPPoi result = null;
		switch (layer) {
		case POI_FACILITY:
			result = facilityLayer.getPoiWithPoiID(pid);
			break;

		default:
			break;
		}
		return result;
	}

	public void highlightPoi(NPPoi poi) {
		switch (poi.getLayer()) {
		case POI_FACILITY:
			facilityLayer.highlightPoi(poi.getPoiID());
			break;

		default:
			break;
		}
	}

	public boolean highlightPoiFeature(float x, float y, int tolerance) {
		return highlightFacilityFeature(x, y, tolerance);
	}

	private boolean highlightFacilityFeature(float x, float y, int tolerance) {
		int[] facilityIDs = facilityLayer.getGraphicIDs(x, y, tolerance);
		if (facilityIDs != null && facilityIDs.length > 0) {
			facilityLayer.setSelectedGraphics(facilityIDs, true);
			return true;
		}

		return false;
	}

	public List<NPPoi> extractSelectedPoi(float x, float y, int tolerance) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();

		poiList.addAll(extractSelectedFacilityPoi(x, y, tolerance));

		return poiList;
	}

	private List<NPPoi> extractSelectedFacilityPoi(float x, float y,
			int tolerance) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();

		int[] facilityIDs = facilityLayer.getGraphicIDs(x, y, tolerance);
		if (facilityIDs != null && facilityIDs.length > 0) {
			for (int gid : facilityIDs) {
				Graphic g = facilityLayer.getGraphic(gid);
				NPPoi poi = new NPPoi(
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_FACILITY);
				poiList.add(poi);
			}
		}
		return poiList;
	}

}
