package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.ty.mapsdk.TYPoi.POI_LAYER;

class IPRoomLayer extends GraphicsLayer {
	static final String TAG = IPRoomLayer.class.getSimpleName();

	Context context;
	private TYRenderingScheme renderingScheme;

	private Map<String, Graphic> roomDict = new HashMap<String, Graphic>();
	private Map<String, Integer> roomGidDict = new HashMap<String, Integer>();

	public IPRoomLayer(Context context, TYRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;
		setRenderer(createRoomRenderer());
	}

	private Renderer createRoomRenderer() {
		UniqueValueRenderer roomRenderer = new UniqueValueRenderer();
		List<UniqueValue> uvInfo = new ArrayList<UniqueValue>();

		for (Integer colorID : renderingScheme.getFillSymbolDictionary()
				.keySet()) {
			SimpleFillSymbol sfs = renderingScheme.getFillSymbolDictionary()
					.get(colorID);
			UniqueValue uv = new UniqueValue();
			uv.setSymbol(sfs);
			uv.setValue(new Integer[] { colorID });
			uvInfo.add(uv);
		}
		roomRenderer.setDefaultSymbol(renderingScheme.getDefaultFillSymbol());

		roomRenderer.setField1("COLOR");
		roomRenderer.setUniqueValueInfos(uvInfo);
		return roomRenderer;
	}

	public void loadContents(FeatureSet set) {
		removeAll();
		roomDict.clear();
		roomGidDict.clear();

		if (set != null) {
			Graphic[] graphics = set.getGraphics();

			if (graphics != null && graphics.length > 0) {
				int[] gids = addGraphics(graphics);

				for (int i = 0; i < graphics.length; ++i) {
					String poiID = (String) graphics[i]
							.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
					roomDict.put(poiID, graphics[i]);
					roomGidDict.put(poiID, gids[i]);
				}
			}
		}
	}

	// public void loadContentsFromFileWithInfo(String path) {
	// removeAll();
	// roomDict.clear();
	// roomGidDict.clear();
	// JsonFactory factory = new JsonFactory();
	//
	// try {
	//
	// JsonParser parser = factory.createJsonParser(new File(path));
	// FeatureSet set = FeatureSet.fromJson(parser);
	//
	// Graphic[] graphics = set.getGraphics();
	//
	// if (graphics != null && graphics.length > 0) {
	// int[] gids = addGraphics(graphics);
	//
	// for (int i = 0; i < graphics.length; ++i) {
	// String poiID = (String) graphics[i]
	// .getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
	// roomDict.put(poiID, graphics[i]);
	// roomGidDict.put(poiID, gids[i]);
	// }
	// }
	//
	// } catch (JsonParseException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public TYPoi getPoiWithPoiID(String pid) {
		TYPoi result = null;
		Graphic graphic = roomDict.get(pid);

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
					POI_LAYER.POI_ROOM);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (roomGidDict.containsKey(poiID)) {
			int gid = roomGidDict.get(poiID);
			setSelectedGraphics(new int[] { gid }, true);
		}
	}

	public TYPoi extractPoiOnCurrentFloor(double x, double y) {
		TYPoi poi = null;

		for (String poiID : roomDict.keySet()) {
			Graphic graphic = roomDict.get(poiID);
			if (GeometryEngine.contains(graphic.getGeometry(), new Point(x, y),
					getSpatialReference())) {
				poi = new TYPoi(
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
						POI_LAYER.POI_ROOM);
				break;
			}
		}

		return poi;
	}
}
