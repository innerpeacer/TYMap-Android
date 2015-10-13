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

	public void setRenderScheme(TYRenderingScheme rs) {
		this.renderingScheme = rs;
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

	public void loadContents(Graphic[] graphics) {
		removeAll();
		roomDict.clear();
		roomGidDict.clear();

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

	public TYPoi getPoiWithPoiID(String pid) {
		TYPoi result = null;
		Graphic graphic = roomDict.get(pid);

		if (graphic != null) {

			int categoryID;
			Object categoryObj = graphic
					.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID);
			if (categoryObj.getClass() == String.class) {
				categoryID = Integer.parseInt((String) categoryObj);
			} else {
				categoryID = (Integer) categoryObj;
			}

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
					graphic.getGeometry(), categoryID, POI_LAYER.POI_ROOM);
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
		// Log.i(TAG, "extractPoiOnCurrentFloor");

		TYPoi poi = null;

		for (String poiID : roomDict.keySet()) {
			Graphic graphic = roomDict.get(poiID);
			if (GeometryEngine.contains(graphic.getGeometry(), new Point(x, y),
					getSpatialReference())) {

				int categoryID;
				Object categoryObj = graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID);
				if (categoryObj.getClass() == String.class) {
					categoryID = Integer.parseInt((String) categoryObj);
				} else {
					categoryID = (Integer) categoryObj;
				}

				String geoID = (String) graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_GEO_ID);
				String floorID = (String) graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID);
				String buildingID = (String) graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID);

				String name = null;
				Object nameObject = graphic
						.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_NAME);
				if (nameObject.getClass() == String.class) {
					name = (String) nameObject;
				}

				poi = new TYPoi(geoID, poiID, floorID, buildingID, name,
						graphic.getGeometry(), categoryID, POI_LAYER.POI_ROOM);
				break;
			}
		}

		return poi;
	}
}
