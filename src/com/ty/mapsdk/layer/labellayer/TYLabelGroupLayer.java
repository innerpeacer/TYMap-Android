package com.ty.mapsdk.layer.labellayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.esri.android.map.GroupLayer;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.ty.mapsdk.TYMapType;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYRenderingScheme;
import com.ty.mapsdk.datamanager.TYMapFileManager;
import com.ty.mapsdk.entity.TYMapInfo;
import com.ty.mapsdk.poi.TYBrand;
import com.ty.mapsdk.poi.TYPoi;
import com.ty.mapsdk.poi.TYPoi.POI_LAYER;

public class TYLabelGroupLayer extends GroupLayer {
	static final String TAG = TYLabelGroupLayer.class.getSimpleName();

	private TYMapView mapView;

	private TYFacilityLayer facilityLayer;
	private TYTextLabelLayer labelLayer;

	private List<TYLabelBorder> visiableBorders = new ArrayList<TYLabelBorder>();

	public TYLabelGroupLayer(Context context, TYMapView mapView,
			TYRenderingScheme renderingScheme, SpatialReference sr) {
		super();

		this.mapView = mapView;

		facilityLayer = new TYFacilityLayer(context, this, renderingScheme, sr,
				null);
		addLayer(facilityLayer);

		labelLayer = new TYTextLabelLayer(context, this, sr, null);
		addLayer(labelLayer);
	}

	public void setBrandDict(Map<String, TYBrand> dict) {
		labelLayer.setBrandDict(dict);
	}

	public TYMapView getMapView() {
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

	public void loadFacilityContentsFromFileWithInfo(TYMapInfo info) {
		facilityLayer.loadContentsFromFileWithInfo(TYMapFileManager
				.getFacilityFilePath(info));
	}

	public void loadLabelContentsFromFileWithInfo(TYMapInfo info) {
		labelLayer.loadContentsFromFileWithInfo(TYMapFileManager
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

	public TYPoi getPoiWithPoiID(String pid, POI_LAYER layer) {
		TYPoi result = null;
		switch (layer) {
		case POI_FACILITY:
			result = facilityLayer.getPoiWithPoiID(pid);
			break;

		default:
			break;
		}
		return result;
	}

	public void highlightPoi(TYPoi poi) {
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

	public List<TYPoi> extractSelectedPoi(float x, float y, int tolerance) {
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		poiList.addAll(extractSelectedFacilityPoi(x, y, tolerance));

		return poiList;
	}

	private List<TYPoi> extractSelectedFacilityPoi(float x, float y,
			int tolerance) {
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		int[] facilityIDs = facilityLayer.getGraphicIDs(x, y, tolerance);
		if (facilityIDs != null && facilityIDs.length > 0) {
			for (int gid : facilityIDs) {
				Graphic g = facilityLayer.getGraphic(gid);
				TYPoi poi = new TYPoi(
						(String) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_FACILITY);
				poiList.add(poi);
			}
		}
		return poiList;
	}

}
