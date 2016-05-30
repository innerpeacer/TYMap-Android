package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.ty.mapsdk.TYPoi.POI_LAYER;

@SuppressLint("UseSparseArrays")
class IPLAFacilityLayer extends GraphicsLayer {
	static final String TAG = IPLAFacilityLayer.class.getSimpleName();
	private Context context;
	private IPLALabelGroupLayer groupLayer;

	Map<Integer, TYPictureMarkerSymbol> allFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();
	Map<Integer, TYPictureMarkerSymbol> allHighlightFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();

	Map<Integer, List<IPLBFacilityLabel>> groupedFacilityLabelDict = new ConcurrentHashMap<Integer, List<IPLBFacilityLabel>>();
	Map<String, IPLBFacilityLabel> facilityLabelDict = new ConcurrentHashMap<String, IPLBFacilityLabel>();

	Map<Graphic, Integer> graphicGidDict = new HashMap<Graphic, Integer>();

	private TYRenderingScheme renderingScheme;

	public IPLAFacilityLayer(Context context, IPLALabelGroupLayer groupLayer,
			TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.groupLayer = groupLayer;
		this.renderingScheme = renderingScheme;
		getFacilitySymbols();
	}

	public void setRenderScheme(TYRenderingScheme rs) {
		this.renderingScheme = rs;
		allFacilitySymbols.clear();
		allHighlightFacilitySymbols.clear();
		getFacilitySymbols();
	}

	public void updateLabels(List<IPLBLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<IPLBLabelBorder> array) {
		if (groupLayer.getMapView().isLabelOverlapDetectingEnabled()) {
			for (IPLBFacilityLabel fl : facilityLabelDict.values()) {
				Point screenPoint = groupLayer.getMapView().toScreenPoint(
						fl.getPosition());
				if (screenPoint == null) {
					continue;
				}

				IPLBLabelBorder border = IPLBLabelBorderCalculator
						.getFacilityLabelBorder(screenPoint);

				boolean isOverlapping = false;
				for (IPLBLabelBorder visibleBorder : array) {
					if (IPLBLabelBorder.CheckIntersect(border, visibleBorder)) {
						isOverlapping = true;
						break;
					}
				}

				if (isOverlapping) {
					fl.setHidden(true);
				} else {
					fl.setHidden(false);
					array.add(border);
				}
			}
		} else {
			for (IPLBFacilityLabel fl : facilityLabelDict.values()) {
				fl.setHidden(false);
			}
		}
	}

	private void updateLabelState() {
		for (IPLBFacilityLabel fl : facilityLabelDict.values()) {
			if (fl.isHidden()) {
				Symbol symbol = null;
				Integer gid = graphicGidDict.get(fl.getFacilityGraphic());
				if (gid != null) {
					updateGraphic(gid, symbol);
				}
			} else {
				Integer gid = graphicGidDict.get(fl.getFacilityGraphic());
				if (gid != null) {
					updateGraphic(gid, fl.getCurrentSymbol());
				}

			}
		}
	}

	public void loadContents(Graphic[] graphics) {
		removeAll();

		groupedFacilityLabelDict.clear();
		facilityLabelDict.clear();
		graphicGidDict.clear();

		for (Graphic graphic : graphics) {

			Integer categoryID;
			Object categoryObj = graphic.getAttributeValue("COLOR");
			if (categoryObj.getClass() == String.class) {
				categoryID = Integer.parseInt((String) categoryObj);
			} else {
				categoryID = (Integer) categoryObj;
			}

			if (categoryID == null) {
				continue;
			}

			Point pos = (Point) graphic.getGeometry();

			if (!groupedFacilityLabelDict.keySet().contains(categoryID)) {
				List<IPLBFacilityLabel> array = new ArrayList<IPLBFacilityLabel>();
				groupedFacilityLabelDict.put(categoryID, array);
			}

			IPLBFacilityLabel fLabel = new IPLBFacilityLabel(categoryID, pos);
			fLabel.setFacilityGraphic(graphic);
			fLabel.setNormalFacilitySymbol(allFacilitySymbols.get(categoryID));
			fLabel.setHighlightedFaciltySymbol(allHighlightFacilitySymbols
					.get(categoryID));
			fLabel.setHighlighted(false);

			List<IPLBFacilityLabel> array = groupedFacilityLabelDict
					.get(categoryID);
			array.add(fLabel);

			String poiID = (String) graphic
					.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
			facilityLabelDict.put(poiID, fLabel);

			int gid = addGraphic(graphic);
			graphicGidDict.put(graphic, gid);

		}
	}

	public void showFacilityWithCategory(int categoryID) {
		Iterator<Integer> iter = groupedFacilityLabelDict.keySet().iterator();
		while (iter.hasNext()) {
			int key = iter.next();
			List<IPLBFacilityLabel> array = groupedFacilityLabelDict.get(key);

			if (key == categoryID) {
				for (IPLBFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (IPLBFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
				}
			}
		}
		updateLabelState();
	}

	public void showFacilityWithCategories(List<Integer> categoryIDs) {
		Iterator<Integer> iter = groupedFacilityLabelDict.keySet().iterator();
		while (iter.hasNext()) {
			int key = iter.next();
			List<IPLBFacilityLabel> array = groupedFacilityLabelDict.get(key);
			if (categoryIDs.contains(key)) {
				for (IPLBFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (IPLBFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
				}
			}
		}
		updateLabelState();
	}

	public void showAllFacilities() {
		for (List<IPLBFacilityLabel> array : groupedFacilityLabelDict.values()) {
			for (IPLBFacilityLabel fl : array) {
				fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
			}
		}
		updateLabelState();
	}

	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return new ArrayList<Integer>(groupedFacilityLabelDict.keySet());
	}

	public TYPoi getPoiWithPoiID(String pid) {
		TYPoi result = null;
		IPLBFacilityLabel fl = facilityLabelDict.get(pid);
		Graphic graphic = fl.getFacilityGraphic();
		if (graphic != null) {

			int categoryID;
			Object categoryObj = graphic
					.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID);
			if (categoryObj.getClass() == String.class) {
				categoryID = Integer.parseInt((String) categoryObj);
			} else {
				categoryID = (Integer) categoryObj;
			}

			result = new TYPoi(
					(String) graphic
							.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
					(String) graphic
							.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
					(String) graphic
							.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
					(String) graphic
							.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
					(String) graphic
							.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_NAME),
					graphic.getGeometry(), categoryID, POI_LAYER.POI_FACILITY);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (facilityLabelDict.containsKey(poiID)) {
			IPLBFacilityLabel fl = facilityLabelDict.get(poiID);
			fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
		}

		updateLabelState();
	}

	private void getFacilitySymbols() {
		Map<Integer, String> iconDict = renderingScheme
				.getIconSymbolDictionary();

		Iterator<Integer> iter = iconDict.keySet().iterator();
		while (iter.hasNext()) {
			Integer colorID = iter.next();
			String icon = iconDict.get(colorID);

			{
				String iconNormal = icon + "_normal";
				int resourceIDNormal = context.getResources().getIdentifier(
						iconNormal, "drawable", context.getPackageName());
				TYPictureMarkerSymbol psNormal = new TYPictureMarkerSymbol(
						context.getResources().getDrawable(resourceIDNormal));
				psNormal.setWidth(26);
				psNormal.setHeight(26);
				allFacilitySymbols.put(colorID, psNormal);
			}
			{
				String iconHighlighted = icon + "_highlighted";
				int resourceIDHighlighted = context.getResources()
						.getIdentifier(iconHighlighted, "drawable",
								context.getPackageName());
				TYPictureMarkerSymbol psHighlighted = new TYPictureMarkerSymbol(
						context.getResources().getDrawable(
								resourceIDHighlighted));
				psHighlighted.setWidth(26);
				psHighlighted.setHeight(26);
				allHighlightFacilitySymbols.put(colorID, psHighlighted);
			}
		}

	}

}
