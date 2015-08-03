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

class IPStructureGroupLayer extends GroupLayer {

	private IPFloorLayer floorLayer;
	private IPRoomLayer roomLayer;
	private GraphicsLayer roomHighlightLayer;
	private IPAssetLayer assetLayer;

	public IPStructureGroupLayer(Context context,
			TYRenderingScheme renderingScheme, SpatialReference sr,
			Envelope envelope) {
		super();

		floorLayer = new IPFloorLayer(context, renderingScheme, sr, envelope);
		addLayer(floorLayer);

		roomLayer = new IPRoomLayer(context, renderingScheme, sr, null);
		addLayer(roomLayer);

		roomHighlightLayer = new GraphicsLayer();
		roomHighlightLayer.setRenderer(new SimpleRenderer(renderingScheme
				.getDefaultHighlightFillSymbol()));
		addLayer(roomHighlightLayer);

		assetLayer = new IPAssetLayer(context, renderingScheme, sr, null);
		addLayer(assetLayer);

	}

	public void removeGraphicsFromSublayers() {
		floorLayer.removeAll();
		roomLayer.removeAll();
		roomHighlightLayer.removeAll();
		assetLayer.removeAll();

	}

	public void loadFloorContentFromFileWithInfo(TYMapInfo info) {
		floorLayer.loadContentsFromFileWithInfo(IPMapFileManager
				.getFloorFilePath(info));
	}

	public void loadRoomContentFromFileWithInfo(TYMapInfo info) {
		roomLayer.loadContentsFromFileWithInfo(IPMapFileManager
				.getRoomFilePath(info));
	}

	public void loadAssetContentFromFileWithInfo(TYMapInfo info) {
		assetLayer.loadContentsFromFileWithInfo(IPMapFileManager
				.getAssetFilePath(info));
	}

	// public void loadContentsFromFileWithInfo(NPMapInfo info) {
	// floorLayer.loadContentsFromFileWithInfo(NPMapFileManager
	// .getFloorFilePath(info));
	// roomLayer.loadContentsFromFileWithInfo(NPMapFileManager
	// .getRoomFilePath(info));
	// assetLayer.loadContentsFromFileWithInfo(NPMapFileManager
	// .getAssetFilePath(info));
	// }

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
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		poiList.addAll(extractSelectedRoomPoi(x, y, tolerance));
		poiList.addAll(extractSelectedAssetPoi(x, y, tolerance));

		return poiList;
	}

	private List<TYPoi> extractSelectedRoomPoi(float x, float y, int tolerance) {
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		int[] roomIDs = roomLayer.getGraphicIDs(x, y, tolerance);
		if (roomIDs != null && roomIDs.length > 0) {
			for (int gid : roomIDs) {
				Graphic g = roomLayer.getGraphic(gid);
				TYPoi poi = new TYPoi(
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ROOM);
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
				TYPoi poi = new TYPoi(
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
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
