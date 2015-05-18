package cn.nephogram.mapsdk.layer.structurelayer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.datamanager.NPMapFileManager;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.GroupLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;

public class NPStructureGroupLayer extends GroupLayer {

	private NPFloorLayer floorLayer;
	private NPRoomLayer roomLayer;
	private GraphicsLayer roomHighlightLayer;
	private NPAssetLayer assetLayer;

	public NPStructureGroupLayer(Context context,
			NPRenderingScheme renderingScheme, SpatialReference sr,
			Envelope envelope) {
		super();

		floorLayer = new NPFloorLayer(context, renderingScheme, sr, envelope);
		addLayer(floorLayer);

		roomLayer = new NPRoomLayer(context, renderingScheme, sr, null);
		addLayer(roomLayer);

		roomHighlightLayer = new GraphicsLayer();
		roomHighlightLayer.setRenderer(new SimpleRenderer(renderingScheme
				.getDefaultHighlightFillSymbol()));
		addLayer(roomHighlightLayer);

		assetLayer = new NPAssetLayer(context, renderingScheme, sr, null);
		addLayer(assetLayer);

	}

	public void removeGraphicsFromSublayers() {
		floorLayer.removeAll();
		roomLayer.removeAll();
		roomHighlightLayer.removeAll();
		assetLayer.removeAll();

	}

	public void loadFloorContentFromFileWithInfo(NPMapInfo info) {
		floorLayer.loadContentsFromFileWithInfo(NPMapFileManager
				.getFloorFilePath(info));
	}

	public void loadRoomContentFromFileWithInfo(NPMapInfo info) {
		roomLayer.loadContentsFromFileWithInfo(NPMapFileManager
				.getRoomFilePath(info));
	}

	public void loadAssetContentFromFileWithInfo(NPMapInfo info) {
		assetLayer.loadContentsFromFileWithInfo(NPMapFileManager
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

	public NPPoi getPoiWithPoiID(String pid, POI_LAYER layer) {
		NPPoi result = null;
		switch (layer) {
		case POI_ROOM:
			result = roomLayer.getPoiWithPoiID(pid);
			break;

		default:
			break;
		}
		return result;
	}

	public void highlightPoi(NPPoi poi) {
		switch (poi.getLayer()) {
		case POI_ROOM:
			NPPoi roomPoi = roomLayer.getPoiWithPoiID(poi.getPoiID());
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

	public List<NPPoi> extractSelectedPoi(float x, float y, int tolerance) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();

		poiList.addAll(extractSelectedRoomPoi(x, y, tolerance));
		poiList.addAll(extractSelectedAssetPoi(x, y, tolerance));

		return poiList;
	}

	private List<NPPoi> extractSelectedRoomPoi(float x, float y, int tolerance) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();

		int[] roomIDs = roomLayer.getGraphicIDs(x, y, tolerance);
		if (roomIDs != null && roomIDs.length > 0) {
			for (int gid : roomIDs) {
				Graphic g = roomLayer.getGraphic(gid);
				NPPoi poi = new NPPoi(
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ROOM);
				poiList.add(poi);
			}
		}

		return poiList;
	}

	private List<NPPoi> extractSelectedAssetPoi(float x, float y, int tolerance) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();

		int[] assetIDs = assetLayer.getGraphicIDs(x, y, tolerance);
		if (assetIDs != null && assetIDs.length > 0) {
			for (int gid : assetIDs) {
				Graphic g = assetLayer.getGraphic(gid);
				NPPoi poi = new NPPoi(
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ASSET);
				poiList.add(poi);
			}
		}

		return poiList;

	}

	public NPPoi extractRoomPoiOnCurrentFloor(double x, double y) {
		return roomLayer.extractPoiOnCurrentFloor(x, y);
	}

}
