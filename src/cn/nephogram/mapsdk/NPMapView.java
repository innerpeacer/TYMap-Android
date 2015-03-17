package cn.nephogram.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import cn.nephogram.data.NPAssetsManager;
import cn.nephogram.data.NPFileManager;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.layer.NPAssetLayer;
import cn.nephogram.mapsdk.layer.NPFacilityLayer;
import cn.nephogram.mapsdk.layer.NPFloorLayer;
import cn.nephogram.mapsdk.layer.NPLabelLayer;
import cn.nephogram.mapsdk.layer.NPRoomLayer;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_TYPE;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;

public class NPMapView extends MapView implements OnSingleTapListener {

	private static final long serialVersionUID = 7475014088599931549L;

	static final String TAG = NPMapView.class.getSimpleName();

	static final String GRAPHIC_ATTRIBUTE_GEO_ID = "GEO_ID";
	static final String GRAPHIC_ATTRIBUTE_POI_ID = "POI_ID";
	static final String GRAPHIC_ATTRIBUTE_FLOOR_ID = "FLOOR_ID";
	static final String GRAPHIC_ATTRIBUTE_BUILDING_ID = "BUILDING_ID";
	static final String GRAPHIC_ATTRIBUTE_NAME = "NAME";
	static final String GRAPHIC_ATTRIBUTE_CATEGORY_ID = "CATEGORY_ID";
	static final String GRAPHIC_ATTRIBUTE_FLOOR = "FLOOR";

	private static final int DEFAULT_TOLERANCE = 5;

	private Context context;

	private String buildingID;
	private NPMapInfo currentMapInfo;

	private NPFloorLayer floorLayer;
	private NPRoomLayer roomLayer;
	private NPAssetLayer assetLayer;

	private NPFacilityLayer facilityLayer;
	private NPLabelLayer labelLayer;

	/**
	 * 是否从assets目录读取地图文件，默认为true
	 */
	public static boolean useAsset = true;

	private List<NPMapViewListenser> listeners = new ArrayList<NPMapView.NPMapViewListenser>();

	private boolean highlightPoiOnSelection = true;

	// =====================================
	public NPMapView(Context context) {
		super(context);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(0x88888888, Color.TRANSPARENT, 0, 0);
	}

	public NPMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(0x88888888, Color.TRANSPARENT, 0, 0);
	}

	/**
	 * 地图初始化方法
	 * 
	 * @param renderingScheme
	 *            地图渲染方案
	 */
	public void init(NPRenderingScheme renderingScheme) {
		Log.i(TAG, "init");

		SpatialReference sr = NPMapEnvironment.defaultSpatialReference();

		floorLayer = new NPFloorLayer(context, renderingScheme, sr, null);
		addLayer(floorLayer);

		roomLayer = new NPRoomLayer(context, renderingScheme, sr, null);
		addLayer(roomLayer);
		roomLayer.setSelectionColor(renderingScheme
				.getDefaultHighlightFillSymbol().getColor());

		assetLayer = new NPAssetLayer(context, renderingScheme, sr, null);
		addLayer(assetLayer);

		facilityLayer = new NPFacilityLayer(context, renderingScheme, sr, null);
		addLayer(facilityLayer);

		labelLayer = new NPLabelLayer(context, sr, null);
		addLayer(labelLayer);

		setOnSingleTapListener(this);
	}

	/**
	 * 切换楼层方法
	 * 
	 * @param info
	 *            目标楼层的地图信息
	 */
	public void setFloor(NPMapInfo info) {
		if (currentMapInfo != null
				&& info.getMapID().equalsIgnoreCase(currentMapInfo.getMapID())) {
			return;
		}

		currentMapInfo = info;

		floorLayer.removeAll();
		roomLayer.removeAll();
		assetLayer.removeAll();
		facilityLayer.removeAll();
		labelLayer.removeAll();

		if (useAsset) {
			floorLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
					.getFloorFilePath(info.getMapID()));
			roomLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
					.getRoomFilePath(info.getMapID()));
			assetLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
					.getAssetFilePath(info.getMapID()));
			facilityLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
					.getFacilityFilePath(info.getMapID()));
			labelLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
					.getLabelFilePath(info.getMapID()));
		} else {
			floorLayer.loadContentsFromFileWithInfo(NPFileManager
					.getFloorFilePath(info.getMapID()));
			roomLayer.loadContentsFromFileWithInfo(NPFileManager
					.getRoomFilePath(info.getMapID()));
			assetLayer.loadContentsFromFileWithInfo(NPFileManager
					.getAssetFilePath(info.getMapID()));
			facilityLayer.loadContentsFromFileWithInfo(NPFileManager
					.getFacilityFilePath(info.getMapID()));
			labelLayer.loadContentsFromFileWithInfo(NPFileManager
					.getLabelFilePath(info.getMapID()));
		}

		Envelope envelope = new Envelope(info.getMapExtent().getXmin(), info
				.getMapExtent().getYmin(), info.getMapExtent().getXmax(), info
				.getMapExtent().getYmax());
		setExtent(envelope);

	}

	/**
	 * 当前显示的建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 当前显示的建筑ID
	 */
	public void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public NPMapInfo getCurrentMapInfo() {
		return currentMapInfo;
	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public void setCurrentMapInfo(NPMapInfo currentMapInfo) {
		this.currentMapInfo = currentMapInfo;
	}

	/**
	 * 在POI被点选时是否高亮显示，默认为NO
	 */
	public boolean isHighlightPoiOnSelection() {
		return highlightPoiOnSelection;
	}

	/**
	 * 在POI被点选时是否高亮显示，默认为NO
	 */
	public void setHighlightPoiOnSelection(boolean highlightPoiOnSelection) {
		this.highlightPoiOnSelection = highlightPoiOnSelection;
	}

	/**
	 * 清除高亮显示的POI
	 */
	public void clearSelection() {
		roomLayer.clearSelection();
		// assetLayer.clearSelection();
		facilityLayer.clearSelection();
	}

	/**
	 * 返回当前楼层的所有公共设施类型
	 * 
	 * @return 公共设施类型数组:[Integer]
	 */
	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return facilityLayer.getAllFacilityCategoryIDOnCurrentFloor();
	}

	/**
	 * 显示当前楼层的所有公共设施
	 */
	public void showAllFacilitiesOnCurrentFloor() {
		facilityLayer.showAllFacilities();
	}

	/**
	 * 显示当前楼层的特定类型公共设施
	 * 
	 * @param categoryID
	 *            公共设施类型ID
	 */
	public void showFacilityOnCurrentWithCategory(int categoryID) {
		facilityLayer.showFacilityWithCategory(categoryID);
	}

	@Override
	public void onSingleTap(float x, float y) {
		clearSelection();

		Point p = toMapPoint(x, y);
		notifyClickAtPoint(p);
		//
		// if (_highlightPOIOnSelection) {
		// [self highlightPoiFeature:features];
		// }

		if (listeners.size() > 0) {
			List<NPPoi> poiList = extractSelectedPoi(x, y);
			// Log.i(TAG, "PoiList: " + poiList.size());
			if (poiList.size() > 0) {
				notifyPoiSelected(poiList);
			}
		}

		if (highlightPoiOnSelection) {
			highlightPoiFeature(x, y);
		}

	}

	private void highlightPoiFeature(float x, float y) {
		int[] facilityIDs = facilityLayer
				.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		if (facilityIDs != null && facilityIDs.length > 0) {
			facilityLayer.setSelectedGraphics(facilityIDs, true);
			return;
		}

		// int[] assetIDs = assetLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		// if (assetIDs != null && assetIDs.length > 0) {
		// assetLayer.setSelectedGraphics(assetIDs, true);
		// return;
		// }

		int[] roomIDs = roomLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		if (roomIDs != null && roomIDs.length > 0) {
			roomLayer.setSelectedGraphics(roomIDs, true);
			return;
		}
	}

	private List<NPPoi> extractSelectedPoi(float x, float y) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();
		{
			int[] facilityIDs = facilityLayer.getGraphicIDs(x, y,
					DEFAULT_TOLERANCE);
			if (facilityIDs != null && facilityIDs.length > 0) {
				for (int gid : facilityIDs) {
					Graphic g = facilityLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_TYPE.POI_FACILITY);
					poiList.add(poi);
				}
			}
		}

		{
			int[] roomIDs = roomLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
			if (roomIDs != null && roomIDs.length > 0) {
				for (int gid : roomIDs) {
					Graphic g = roomLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_TYPE.POI_ROOM);
					poiList.add(poi);
				}
			}
		}

		{
			int[] assetIDs = assetLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
			if (assetIDs != null && assetIDs.length > 0) {
				for (int gid : assetIDs) {
					Graphic g = assetLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_TYPE.POI_ASSET);
					poiList.add(poi);
				}
			}
		}

		return poiList;
	}

	/**
	 * 地图事件接口
	 */
	public interface NPMapViewListenser {
		/**
		 * 地图点选事件回调方法
		 * 
		 * @param mappoint
		 *            点击事件的地图坐标
		 */
		void onClickAtPoint(Point mappoint);

		/**
		 * 地图POI选中事件回调
		 * 
		 * @param mapView
		 *            地图视图
		 * @param array
		 *            选中的POI数组:[NPPoi]
		 */
		void onPoiSelected(List<NPPoi> poiList);
	}

	/**
	 * 添加地图事件监听
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addMapListener(NPMapViewListenser listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * 移除地图事件监听
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void removeMapListener(NPMapViewListenser listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyPoiSelected(List<NPPoi> poiList) {
		for (NPMapViewListenser listener : listeners) {
			listener.onPoiSelected(poiList);
		}
	}

	private void notifyClickAtPoint(Point mappoint) {
		for (NPMapViewListenser listener : listeners) {
			listener.onClickAtPoint(mappoint);
		}
	}

}
