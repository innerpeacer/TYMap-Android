package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.GroupLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.ty.mapsdk.TYPoi.POI_LAYER;

class IPLAStructureGroupLayer extends GroupLayer {
	static final String TAG = IPLAStructureGroupLayer.class.getSimpleName();
	private IPLAFloorLayer floorLayer;
	private IPLARoomLayer roomLayer;
	private GraphicsLayer roomHighlightLayer;
	private IPLAAssetLayer assetLayer;

	public IPLAStructureGroupLayer(Context context,
			TYRenderingScheme renderingScheme, SpatialReference sr,
			Envelope envelope) {
		super();

		floorLayer = new IPLAFloorLayer(context, renderingScheme, sr, envelope);
		addLayer(floorLayer);

		roomLayer = new IPLARoomLayer(context, renderingScheme, sr, null);
		addLayer(roomLayer);

		roomHighlightLayer = new GraphicsLayer();
		roomHighlightLayer.setRenderer(new SimpleRenderer(renderingScheme
				.getDefaultHighlightFillSymbol()));
		addLayer(roomHighlightLayer);

		assetLayer = new IPLAAssetLayer(context, renderingScheme, sr, null);
		addLayer(assetLayer);

	}

	public void setRenderScheme(TYRenderingScheme rs) {
		floorLayer.setRenderScheme(rs);
		roomLayer.setRenderScheme(rs);
		assetLayer.setRenderScheme(rs);
	}

	public void removeGraphicsFromSublayers() {
		floorLayer.removeAll();
		roomLayer.removeAll();
		roomHighlightLayer.removeAll();
		assetLayer.removeAll();

	}

	public void loadFloorContent(Graphic[] graphics) {
		// Log.i(TAG, "loadFloorContent: " + graphics.length);
		floorLayer.loadContents(graphics);
	}

	public void loadRoomContent(Graphic[] graphics) {
		// Log.i(TAG, "loadRoomContent: " + graphics.length);
		roomLayer.loadContents(graphics);
	}

	public void loadAssetContent(Graphic[] graphics) {
		// Log.i(TAG, "loadAssetContent: " + graphics.length);
		assetLayer.loadContents(graphics);
	}

	public void clearSelection() {
		roomLayer.clearSelection();
		roomHighlightLayer.removeAll();

	}

	public TYPoi getPoiWithPoiID(String pid, POI_LAYER layer) {
		TYPoi result = null;
		switch (layer) {
		case POI_ROOM:
			result = roomLayer.getPoiWithPoiID(pid);
			break;

		default:
			break;
		}
		return result;
	}

	public void highlightPoi(TYPoi poi) {
		switch (poi.getLayer()) {
		case POI_ROOM:
			TYPoi roomPoi = roomLayer.getPoiWithPoiID(poi.getPoiID());
			if (poi != null) {
				roomHighlightLayer.addGraphic(new Graphic(
						roomPoi.getGeometry(), null));
			}
			break;

		default:
			break;
		}
	}

	public boolean highlightPoiFeature(float x, float y, int tolerance) {
		// highlightAssetPoiFeature(x, y, tolerance);
		return highlightRoomPoiFeature(x, y, tolerance);
	}

	// private boolean highlightAssetPoiFeature(float x, float y, int tolerance)
	// {
	// int[] assetIDs = assetLayer.getGraphicIDs(x, y, tolerance);
	// if (assetIDs != null && assetIDs.length > 0) {
	// assetLayer.setSelectedGraphics(assetIDs, true);
	// return true;
	// }
	// return false;
	// }

	private boolean highlightRoomPoiFeature(float x, float y, int tolerance) {
		int[] roomIDs = roomLayer.getGraphicIDs(x, y, tolerance);
		if (roomIDs != null && roomIDs.length > 0) {
			for (int gid : roomIDs) {
				Graphic g = roomLayer.getGraphic(gid);
				roomHighlightLayer
						.addGraphic(new Graphic(g.getGeometry(), null));
			}
			return true;
		}

		return false;
	}

	public List<TYPoi> extractSelectedPoi(float x, float y, int tolerance) {
		// Log.i(TAG, "StructureGroupLayer: extractSelectedPoi");
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		poiList.addAll(extractSelectedAssetPoi(x, y, tolerance));
		poiList.addAll(extractSelectedRoomPoi(x, y, tolerance));

		return poiList;
	}

	private List<TYPoi> extractSelectedRoomPoi(float x, float y, int tolerance) {
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		int[] roomIDs = roomLayer.getGraphicIDs(x, y, tolerance);
		if (roomIDs != null && roomIDs.length > 0) {
			for (int gid : roomIDs) {
				Graphic g = roomLayer.getGraphic(gid);

				// 兼容老数据，CategoryID为整数

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
						g.getGeometry(), categoryID, POI_LAYER.POI_ROOM);

				poiList.add(poi);
			}
		}

		return poiList;
	}

	private List<TYPoi> extractSelectedAssetPoi(float x, float y, int tolerance) {
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		int[] assetIDs = assetLayer.getGraphicIDs(x, y, tolerance);
		if (assetIDs != null && assetIDs.length > 0) {
			for (int gid : assetIDs) {
				Graphic g = assetLayer.getGraphic(gid);

				int categoryID;
				Object categoryObj = g
						.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID);
				if (categoryObj.getClass() == String.class) {
					categoryID = Integer.parseInt((String) categoryObj);
				} else {
					categoryID = (Integer) categoryObj;
				}

				String poiName = (String) g
						.getAttributeValue(IPHPMapType.GRAPHIC_ATTRIBUTE_NAME);
				if (poiName == null) {
					continue;
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
						poiName, g.getGeometry(), categoryID,
						POI_LAYER.POI_ASSET);
				poiList.add(poi);
			}
		}

		return poiList;

	}

	public TYPoi extractRoomPoiOnCurrentFloor(double x, double y) {
		return roomLayer.extractPoiOnCurrentFloor(x, y);
	}

}
