package com.ty.mapsdk.layer.labellayer;

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
import com.ty.mapsdk.TYMapType;
import com.ty.mapsdk.TYRenderingScheme;
import com.ty.mapsdk.entity.TYPictureMarkerSymbol;
import com.ty.mapsdk.poi.TYPoi;
import com.ty.mapsdk.poi.TYPoi.POI_LAYER;

@SuppressLint("UseSparseArrays")
public class TYFacilityLayer extends GraphicsLayer {
	static final String TAG = TYFacilityLayer.class.getSimpleName();
	private Context context;
	private TYLabelGroupLayer groupLayer;

	Map<Integer, TYPictureMarkerSymbol> allFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();
	Map<Integer, TYPictureMarkerSymbol> allHighlightFacilitySymbols = new HashMap<Integer, TYPictureMarkerSymbol>();

	// Map<Integer, List<NPFacilityLabel>> groupedFacilityLabelDict = new
	// HashMap<Integer, List<NPFacilityLabel>>();
	// Map<String, NPFacilityLabel> facilityLabelDict = new HashMap<String,
	// NPFacilityLabel>();
	Map<Integer, List<TYFacilityLabel>> groupedFacilityLabelDict = new ConcurrentHashMap<Integer, List<TYFacilityLabel>>();
	Map<String, TYFacilityLabel> facilityLabelDict = new ConcurrentHashMap<String, TYFacilityLabel>();

	Map<Graphic, Integer> graphicGidDict = new HashMap<Graphic, Integer>();

	private TYRenderingScheme renderingScheme;

	public TYFacilityLayer(Context context, TYLabelGroupLayer groupLayer,
			TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.groupLayer = groupLayer;
		this.renderingScheme = renderingScheme;

		getFacilitySymbols();
	}

	public void updateLabels(List<TYLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private synchronized void updateLabelBorders(List<TYLabelBorder> array) {
		for (TYFacilityLabel fl : facilityLabelDict.values()) {
			Point screenPoint = groupLayer.getMapView().toScreenPoint(
					fl.getPosition());
			if (screenPoint == null) {
				continue;
			}

			TYLabelBorder border = TYLabelBorderCalculator
					.getFacilityLabelBorder(screenPoint);

			boolean isOverlapping = false;
			for (TYLabelBorder visibleBorder : array) {
				if (TYLabelBorder.CheckIntersect(border, visibleBorder)) {
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
		for (TYFacilityLabel fl : facilityLabelDict.values()) {
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
				Integer categoryID = (Integer) graphic
						.getAttributeValue("COLOR");
				if (categoryID == null) {
					continue;
				}

				Point pos = (Point) graphic.getGeometry();

				if (!groupedFacilityLabelDict.keySet().contains(categoryID)) {
					List<TYFacilityLabel> array = new ArrayList<TYFacilityLabel>();
					groupedFacilityLabelDict.put(categoryID, array);
				}

				TYFacilityLabel fLabel = new TYFacilityLabel(categoryID, pos);
				fLabel.setFacilityGraphic(graphic);
				fLabel.setNormalFacilitySymbol(allFacilitySymbols
						.get(categoryID));
				fLabel.setHighlightedFaciltySymbol(allHighlightFacilitySymbols
						.get(categoryID));
				fLabel.setHighlighted(false);

				List<TYFacilityLabel> array = groupedFacilityLabelDict
						.get(categoryID);
				array.add(fLabel);

				String poiID = (String) graphic
						.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_POI_ID);
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
			List<TYFacilityLabel> array = groupedFacilityLabelDict.get(key);

			if (key == categoryID) {
				for (TYFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (TYFacilityLabel fl : array) {
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
			List<TYFacilityLabel> array = groupedFacilityLabelDict.get(key);
			if (categoryIDs.contains(key)) {
				for (TYFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (TYFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
				}
			}
		}
		updateLabelState();
	}

	public void showAllFacilities() {
		for (List<TYFacilityLabel> array : groupedFacilityLabelDict.values()) {
			for (TYFacilityLabel fl : array) {
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
		TYFacilityLabel fl = facilityLabelDict.get(pid);
		Graphic graphic = fl.getFacilityGraphic();
		if (graphic != null) {
			result = new TYPoi(
					(String) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
					(String) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_POI_ID),
					(String) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
					(String) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
					(String) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_NAME),
					graphic.getGeometry(),
					(Integer) graphic
							.getAttributeValue(TYMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
					POI_LAYER.POI_FACILITY);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (facilityLabelDict.containsKey(poiID)) {
			TYFacilityLabel fl = facilityLabelDict.get(poiID);
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
