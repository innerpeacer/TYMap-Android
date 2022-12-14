package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.esri.android.map.GroupLayer;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.ty.mapsdk.TYPoi.POI_LAYER;

class IPLALabelGroupLayer extends GroupLayer {
	static final String TAG = IPLALabelGroupLayer.class.getSimpleName();

	private TYMapView mapView;

	private IPLAFacilityLayer facilityLayer;
	private IPLATextLabelLayer labelLayer;

	private List<IPLBLabelBorder> visiableBorders = new ArrayList<IPLBLabelBorder>();

	public IPLALabelGroupLayer(Context context, TYMapView mapView,
			TYRenderingScheme renderingScheme, SpatialReference sr) {
		super();

		this.mapView = mapView;
		facilityLayer = new IPLAFacilityLayer(context, this, renderingScheme, sr,
				null);
		addLayer(facilityLayer);

		labelLayer = new IPLATextLabelLayer(context, this, sr, null);
		addLayer(labelLayer);
	}

	public void setRenderScheme(TYRenderingScheme rs) {
		facilityLayer.setRenderScheme(rs);
	}

	public void setBrandDict(Map<String, IPHPBrand> dict) {
		labelLayer.setBrandDict(dict);
	}

	public TYMapView getMapView() {
		return mapView;
	}

	public synchronized void updateLabels() {
		// Log.i(TAG, "updateLabels");
		visiableBorders.clear();

		// long now = System.currentTimeMillis();
		facilityLayer.updateLabels(visiableBorders);
		// Log.i(TAG, "facilityLayer.updateLabels:"
		// + (System.currentTimeMillis() - now) / 1000.0f);
		// now = System.currentTimeMillis();
		labelLayer.updateLabels(visiableBorders);
		// Log.i(TAG, "labelLayer.updateLabels:"
		// + (System.currentTimeMillis() - now) / 1000.0f);

	}

	public void removeGraphicsFromSublayers() {
		facilityLayer.removeAll();
		labelLayer.removeAll();
	}

	// public void loadFacilityContents(FeatureSet set) {
	// facilityLayer.loadContents(set);
	// }
	//
	// public void loadLabelContents(FeatureSet set) {
	// labelLayer.loadContents(set);
	// }

	public void loadFacilityContents(Graphic[] graphics) {
		// Log.i(TAG, "loadFacilityContents: " + graphics.length);
		facilityLayer.loadContents(graphics);
	}

	public void loadLabelContents(Graphic[] graphics) {
		// Log.i(TAG, "loadLabelContents: " + graphics.length);
		labelLayer.loadContents(graphics);
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
		// Log.i(TAG, "LabelGroupLayer: extractSelectedPoi");
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

				int categoryID;
				Object categoryObj = g
						.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID);
				if (categoryObj.getClass() == String.class) {
					categoryID = Integer.parseInt((String) categoryObj);
				} else {
					categoryID = (Integer) categoryObj;
				}

				TYPoi poi = new TYPoi(
						(String) g
								.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(), categoryID, POI_LAYER.POI_FACILITY);
				poiList.add(poi);
			}
		}
		return poiList;
	}

}
