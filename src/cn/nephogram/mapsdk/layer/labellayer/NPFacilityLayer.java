package cn.nephogram.mapsdk.layer.labellayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.annotation.SuppressLint;
import android.content.Context;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.entity.NPPictureMarkerSymbol;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

@SuppressLint("UseSparseArrays")
public class NPFacilityLayer extends GraphicsLayer {
	static final String TAG = NPFacilityLayer.class.getSimpleName();
	private Context context;
	private NPLabelGroupLayer groupLayer;

	Map<Integer, NPPictureMarkerSymbol> allFacilitySymbols = new HashMap<Integer, NPPictureMarkerSymbol>();
	Map<Integer, NPPictureMarkerSymbol> allHighlightFacilitySymbols = new HashMap<Integer, NPPictureMarkerSymbol>();

	Map<Integer, List<NPFacilityLabel>> groupedFacilityLabelDict = new HashMap<Integer, List<NPFacilityLabel>>();
	Map<String, NPFacilityLabel> facilityLabelDict = new HashMap<String, NPFacilityLabel>();
	Map<Graphic, Integer> graphicGidDict = new HashMap<Graphic, Integer>();

	private NPRenderingScheme renderingScheme;

	public NPFacilityLayer(Context context, NPLabelGroupLayer groupLayer,
			NPRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.groupLayer = groupLayer;
		this.renderingScheme = renderingScheme;

		getFacilitySymbols();
	}

	public void updateLabels(List<NPLabelBorder> array) {
		updateLabelBorders(array);
		updateLabelState();
	}

	private void updateLabelBorders(List<NPLabelBorder> array) {
		for (NPFacilityLabel fl : facilityLabelDict.values()) {
			Point screenPoint = groupLayer.getMapView().toScreenPoint(
					fl.getPosition());
			NPLabelBorder border = NPLabelBorderCalculator
					.getFacilityLabelBorder(screenPoint);

			boolean isOverlapping = false;
			for (NPLabelBorder visibleBorder : array) {
				if (NPLabelBorder.CheckIntersect(border, visibleBorder)) {
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
		for (NPFacilityLabel fl : facilityLabelDict.values()) {
			if (fl.isHidden()) {
				Symbol symbol = null;
				updateGraphic(graphicGidDict.get(fl.getFacilityGraphic()),
						symbol);
			} else {
				updateGraphic(graphicGidDict.get(fl.getFacilityGraphic()),
						fl.getCurrentSymbol());
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
					List<NPFacilityLabel> array = new ArrayList<NPFacilityLabel>();
					groupedFacilityLabelDict.put(categoryID, array);
				}

				NPFacilityLabel fLabel = new NPFacilityLabel(categoryID, pos);
				fLabel.setFacilityGraphic(graphic);
				fLabel.setNormalFacilitySymbol(allFacilitySymbols
						.get(categoryID));
				fLabel.setHighlightedFaciltySymbol(allHighlightFacilitySymbols
						.get(categoryID));
				fLabel.setHighlighted(false);

				List<NPFacilityLabel> array = groupedFacilityLabelDict
						.get(categoryID);
				array.add(fLabel);

				String poiID = (String) graphic
						.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
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
			List<NPFacilityLabel> array = groupedFacilityLabelDict.get(key);

			if (key == categoryID) {
				for (NPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (NPFacilityLabel fl : array) {
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
			List<NPFacilityLabel> array = groupedFacilityLabelDict.get(key);
			if (categoryIDs.contains(key)) {
				for (NPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getHighlightedFaciltySymbol());
				}
			} else {
				for (NPFacilityLabel fl : array) {
					fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
				}
			}
		}
		updateLabelState();
	}

	public void showAllFacilities() {
		for (List<NPFacilityLabel> array : groupedFacilityLabelDict.values()) {
			for (NPFacilityLabel fl : array) {
				fl.setCurrentSymbol(fl.getNormalFacilitySymbol());
			}
		}
		updateLabelState();
	}

	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return new ArrayList<Integer>(groupedFacilityLabelDict.keySet());
	}

	public NPPoi getPoiWithPoiID(String pid) {
		NPPoi result = null;
		NPFacilityLabel fl = facilityLabelDict.get(pid);
		Graphic graphic = fl.getFacilityGraphic();
		if (graphic != null) {
			result = new NPPoi(
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
					graphic.getGeometry(),
					(Integer) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
					POI_LAYER.POI_FACILITY);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (facilityLabelDict.containsKey(poiID)) {
			NPFacilityLabel fl = facilityLabelDict.get(poiID);
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
				NPPictureMarkerSymbol psNormal = new NPPictureMarkerSymbol(
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
				NPPictureMarkerSymbol psHighlighted = new NPPictureMarkerSymbol(
						context.getResources().getDrawable(
								resourceIDHighlighted));
				psHighlighted.setWidth(26);
				psHighlighted.setHeight(26);
				allHighlightFacilitySymbols.put(colorID, psHighlighted);
			}
		}
	}

}
