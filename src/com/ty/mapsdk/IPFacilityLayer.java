package com.ty.mapsdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.annotation.SuppressLint;
import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.ty.mapsdk.TYPoi.POI_LAYER;

@SuppressLint("UseSparseArrays")
class IPFacilityLayer extends GraphicsLayer {
	static final String TAG = IPFacilityLayer.class.getSimpleName();
	private Context context;
	private IPLabelGroupLayer groupLayer;

	Map<Integer, TYPictureMarkerSymbol> allFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();
	Map<Integer, TYPictureMarkerSymbol> allHighlightFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();

	// Map<Integer, List<NPFacilityLabel>> groupedFacilityLabelDict = new
	// HashMap<Integer, List<NPFacilityLabel>>();
	// Map<String, NPFacilityLabel> facilityLabelDict = new HashMap<String,
	// NPFacilityLabel>();
	Map<Integer, List<IPFacilityLabel>> groupedFacilityLabelDict = new ConcurrentHashMap<Integer, List<IPFacilityLabel>>();
	Map<String, IPFacilityLabel> facilityLabelDict = new ConcurrentHashMap<String, IPFacilityLabel>();

	Map<Graphic, Integer> graphicGidDict = new HashMap<Graphic, Integer>();

	private TYRenderingScheme renderingScheme;

	public IPFacilityLayer(Context context, IPLabelGroupLayer groupLayer,
			TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.groupLayer = groupLayer;
		this.renderingScheme = renderingScheme;

		getFacilitySymbols();
	}

	public void updateLabels(List<IPLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<IPLabelBorder> array) {
		for (IPFacilityLabel fl : facilityLabelDict.values()) {
			Point screenPoint = groupLayer.getMapView().toScreenPoint(
					fl.getPosition());
			if (screenPoint == null) {
				continue;
			}

			IPLabelBorder border = IPLabelBorderCalculator
					.getFacilityLabelBorder(screenPoint);

			boolean isOverlapping = false;
			for (IPLabelBorder visibleBorder : array) {
				if (IPLabelBorder.CheckIntersect(border, visibleBorder)) {
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
	}

	private void updateLabelState() {
		for (IPFacilityLabel fl : facilityLabelDict.values()) {
			if (fl.isHidden()) {
				Symbol symbol = null;
				Integer gid = graphicGidDict.get(fl.getFacilityGraphic());
				if (gid != null) {
					updateGraphic(gid, symbol);
				}
			} else {
				// updateGraphic(graphicGidDict.get(fl.getFacilityGraphic()),
				// fl.getCurrentSymbol());
				Integer gid = graphicGidDict.get(fl.getFacilityGraphic());
				if (gid != null) {
					updateGraphic(gid, fl.getCurrentSymbol());
				}
			}
		}
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();

		groupedFacilityLabelDict.clear();
		facilityLabelDict.clear();
		graphicGidDict.clear();

		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			for (Graphic graphic : graphics) {
				Integer categoryID = Integer.parseInt((String) graphic
						.getAttributeValue("COLOR"));
				if (categoryID == null) {
					continue;
				}

				Point pos = (Point) graphic.getGeometry();

				if (!groupedFacilityLabelDict.keySet().contains(categoryID)) {
					List<IPFacilityLabel> array = new ArrayList<IPFacilityLabel>();
					groupedFacilityLabelDict.put(categoryID, array);
				}

				IPFacilityLabel fLabel = new IPFacilityLabel(categoryID, pos);
				fLabel.setFacilityGraphic(graphic);
				fLabel.setNormalFacilitySymbol(allFacilitySymbols
						.get(categoryID));
				fLabel.setHighlightedFaciltySymbol(allHighlightFacilitySymbols
						.get(categoryID));
				fLabel.setHighlighted(false);

				List<IPFacilityLabel> array = groupedFacilityLabelDict
						.get(categoryID);
				array.add(fLabel);

				String poiID = (String) graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				facilityLabelDict.put(poiID, fLabel);

				int gid = addGraphic(graphic);
				graphicGidDict.put(graphic, gid);
			}

			// updateLabelState();

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showFacilityWithCategory(int categoryID) {
		Iterator<Integer> iter = groupedFacilityLabelDict.keySet().iterator();
		while (iter.hasNext()) {
			int key = iter.next();
			List<IPFacilityLabel> array = groupedFacilityLabelDict.get(key);

			if (key == categoryID) {
				for (IPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (IPFacilityLabel fl : array) {
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
			List<IPFacilityLabel> array = groupedFacilityLabelDict.get(key);
			if (categoryIDs.contains(key)) {
				for (IPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (IPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
				}
			}
		}
		updateLabelState();
	}

	public void showAllFacilities() {
		for (List<IPFacilityLabel> array : groupedFacilityLabelDict.values()) {
			for (IPFacilityLabel fl : array) {
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
		IPFacilityLabel fl = facilityLabelDict.get(pid);
		Graphic graphic = fl.getFacilityGraphic();
		if (graphic != null) {
			result = new TYPoi(
					(String) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
					(String) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
					(String) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
					(String) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
					(String) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_NAME),
					graphic.getGeometry(),
					(Integer) graphic
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
					POI_LAYER.POI_FACILITY);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (facilityLabelDict.containsKey(poiID)) {
			IPFacilityLabel fl = facilityLabelDict.get(poiID);
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
