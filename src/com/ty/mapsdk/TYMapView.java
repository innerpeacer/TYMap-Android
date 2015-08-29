package com.ty.mapsdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.MarkerSymbol;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYPoi.POI_LAYER;

public class TYMapView extends MapView implements OnSingleTapListener,
		OnPanListener, OnZoomListener, OnStatusChangedListener {

	private static final long serialVersionUID = 7475014088599931549L;

	static final String TAG = TYMapView.class.getSimpleName();

	private static final int DEFAULT_TOLERANCE = 5;

	private Context context;

	private boolean isMapInitlized = false;

	private TYBuilding building;
	private TYMapInfo currentMapInfo;

	private Map<String, Graphic[]> mapDataDictionary = new HashMap<String, Graphic[]>();
	private Map<String, Map<String, Graphic[]>> mapDataCache = new HashMap<String, Map<String, Graphic[]>>();

	private IPStructureGroupLayer structureGroupLayer;
	private IPLabelGroupLayer labelGroupLayer;

	private IPRouteLayer routeLayer;
	private IPAnimatedRouteArrowLayer animatedRouteArrowLayer;
	private IPRouteHintLayer routeHintLayer;

	private IPLocationLayer locationLayer;

	private Envelope initialEnvelope;

	private TYMapViewMode mapViewMode = TYMapViewMode.TYMapViewModeDefault;
	private double currentDeviceHeading = 0;

	private List<TYMapViewListenser> listeners = new ArrayList<TYMapView.TYMapViewListenser>();

	private boolean highlightPoiOnSelection = false;

	private double lastRotationAngle = 0.0;

	private Map<String, IPBrand> allBrandDict = new HashMap<String, IPBrand>();

	// =====================================
	public TYMapView(Context context) {
		super(context);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(Color.WHITE, Color.TRANSPARENT, 0, 0);
	}

	public TYMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(Color.WHITE, Color.TRANSPARENT, 0, 0);
	}

	/**
	 * 地图初始化方法
	 * 
	 * @param renderingScheme
	 *            地图渲染方案
	 * @param buliding
	 *            地图显示的目标建筑
	 */
	public void init(TYRenderingScheme renderingScheme, TYBuilding buliding) {
		// Log.i(TAG, "init");
		this.building = buliding;

		List<IPBrand> brandArray = IPBrand.parseAllBrands(buliding);
		for (IPBrand brand : brandArray) {
			allBrandDict.put(brand.getPoiID(), brand);
		}

		SpatialReference sr = TYMapEnvironment.defaultSpatialReference();

		structureGroupLayer = new IPStructureGroupLayer(context,
				renderingScheme, sr, null);
		addLayer(structureGroupLayer);

		labelGroupLayer = new IPLabelGroupLayer(context, this, renderingScheme,
				sr);
		labelGroupLayer.setBrandDict(allBrandDict);
		addLayer(labelGroupLayer);

		routeLayer = new IPRouteLayer(this);
		addLayer(routeLayer);

		routeHintLayer = new IPRouteHintLayer(context);
		addLayer(routeHintLayer);

		animatedRouteArrowLayer = new IPAnimatedRouteArrowLayer(context, this);
		addLayer(animatedRouteArrowLayer);

		locationLayer = new IPLocationLayer();
		addLayer(locationLayer);

		setOnSingleTapListener(this);
		setOnPanListener(this);
		setOnZoomListener(this);
		setOnStatusChangedListener(this);
	}

	private boolean isSwitching = false;
	private boolean isInterupted = false;
	private boolean isBlocking = false;

	/**
	 * 切换楼层方法
	 * 
	 * @param info
	 *            目标楼层的地图信息
	 */
	public void setFloor(final TYMapInfo info) {

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date invalidDate = dateFormat.parse("2015-10-11");

			Date now = new Date();
			boolean isInvalid = now.after(invalidDate);
			if (isInvalid) {
				Toast.makeText(getContext(), "抱歉，SDK已过期", Toast.LENGTH_LONG)
						.show();
				return;
			}
		} catch (ParseException e) {
			return;
		}

		if (currentMapInfo != null
				&& info.getMapID().equalsIgnoreCase(currentMapInfo.getMapID())) {
			return;
		}

		if (isSwitching) {
			isInterupted = true;
		}

		while (isBlocking) {

		}

		isSwitching = true;
		isBlocking = true;

		currentMapInfo = info;

		structureGroupLayer.removeGraphicsFromSublayers();
		labelGroupLayer.removeGraphicsFromSublayers();

		routeLayer.removeAll();
		routeHintLayer.removeAll();
		animatedRouteArrowLayer.stopShowingArrow();

		locationLayer.removeAll();

		if (!mapDataCache.containsKey(currentMapInfo.getMapID())) {
			long loadStart = System.currentTimeMillis();
			Map<String, Graphic[]> mapData = IPFeatureSetParser
					.parseMapDataFile(IPMapFileManager
							.getMapDataPath(currentMapInfo));
			long loadEnd = System.currentTimeMillis();
			Log.i(TAG, "Load Time: " + (loadEnd - loadStart) / 1000.0f);

			mapDataCache.put(currentMapInfo.getMapID(), mapData);
		}
		mapDataDictionary = mapDataCache.get(currentMapInfo.getMapID());

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (!isInterupted) {
					structureGroupLayer.loadFloorContent(mapDataDictionary
							.get("floor"));
				}

				if (!isInterupted) {
					structureGroupLayer.loadRoomContent(mapDataDictionary
							.get("room"));

				}

				if (!isInterupted) {
					structureGroupLayer.loadAssetContent(mapDataDictionary
							.get("asset"));
				}

				if (!isInterupted) {
					labelGroupLayer.loadFacilityContents(mapDataDictionary
							.get("facility"));
				}

				if (!isInterupted) {
					labelGroupLayer.loadLabelContents(mapDataDictionary
							.get("label"));
				}

				if (initialEnvelope == null) {
					initialEnvelope = new Envelope(info.getMapExtent()
							.getXmin(), info.getMapExtent().getYmin(), info
							.getMapExtent().getXmax(), info.getMapExtent()
							.getYmax());
					setExtent(initialEnvelope);
					double width = 0.06; // 6cm
					setMinScale(info.getMapSize().getX() / width);
					setMaxScale(6 / width);
				}

				if (!isInterupted) {
					while (!isMapInitlized) {
					}
					labelGroupLayer.updateLabels();
					notifyFinishLoadingFloor(TYMapView.this, currentMapInfo);
				}

				if (isInterupted) {
					structureGroupLayer.removeGraphicsFromSublayers();
					labelGroupLayer.removeGraphicsFromSublayers();

					locationLayer.removeAll();
				}

				isSwitching = false;
				isInterupted = false;
				isBlocking = false;
			}
		}).start();

	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public TYMapInfo getCurrentMapInfo() {
		return currentMapInfo;
	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public void setCurrentMapInfo(TYMapInfo currentMapInfo) {
		this.currentMapInfo = currentMapInfo;
	}

	/**
	 * 在地图显示当前楼层的导航路径
	 */
	public void showRouteResultOnCurrentFloor() {
		List<Polyline> linesToShow = routeLayer
				.showRouteResultOnFloor(currentMapInfo.getFloorNumber());
		if (linesToShow != null && linesToShow.size() > 0) {
			Log.i(TAG, "Show Route Arrows");
			// routeArrowLayer.showRouteArrows(linesToShow);
			animatedRouteArrowLayer.showRouteArrows(linesToShow);
		}
	}

	/**
	 * 在地图显示当前楼层当前位置的剩余路径，结合定位结果，移除已经经过的路径部分
	 * 
	 * @param lp
	 *            当前位置
	 */
	public void showRemainingRouteResultOnCurrentFloor(TYLocalPoint lp) {
		List<Polyline> linesToShow = routeLayer
				.showRemainingRouteResultOnFloor(
						currentMapInfo.getFloorNumber(), lp);
		if (linesToShow != null && linesToShow.size() > 0) {
			animatedRouteArrowLayer.showRouteArrows(linesToShow);
		}
	}

	/**
	 * 显示导航提示对应的路径段
	 * 
	 * @param ds
	 *            目标路径提示
	 * @param isCentered
	 *            是否移动地图将路径提示段居中
	 */
	public void showRouteHint(TYDirectionalHint ds, boolean isCentered) {
		TYRouteResult routeResult = routeLayer.getRouteResult();
		if (routeResult != null) {
			Polyline currentLine = ds.getRoutePart().getRoute();
			Polyline subLine = TYRouteResult.getSubPolyline(currentLine,
					ds.getStartPoint(), ds.getEndPoint());
			routeHintLayer.showRouteHint(subLine);

			if (isCentered) {
				Point center = new Point((ds.getStartPoint().getX() + ds
						.getEndPoint().getX()) * 0.5, (ds.getStartPoint()
						.getY() + ds.getEndPoint().getY()) * 0.5);
				centerAt(center, true);
			}
		}
	}

	/**
	 * 在地图当前楼层显示起点符号
	 * 
	 * @param start
	 *            起点位置
	 */
	public void showRouteStartSymbolOnCurrentFloor(TYLocalPoint start) {
		routeLayer.showStartSymbol(start);
	}

	/**
	 * 在地图当前楼层显示终点符号
	 * 
	 * @param end
	 *            终点位置
	 */
	public void showRouteEndSymbolOnCurrentFloor(TYLocalPoint end) {
		routeLayer.showEndSymbol(end);
	}

	/**
	 * 设置导航结果
	 * 
	 * @param rs
	 *            导航结果
	 */
	public void setRouteResult(TYRouteResult rs) {
		routeLayer.setRouteResult(rs);
	}

	/**
	 * 设置导航线的起点符号
	 * 
	 * @param pms
	 *            起点标识符号
	 */
	public void setStartSymbol(TYPictureMarkerSymbol pms) {
		routeLayer.setStartSymbol(pms);
	}

	/**
	 * 设置导航线的终点符号
	 * 
	 * @param pms
	 *            终点标识符号
	 */
	public void setEndSymbol(TYPictureMarkerSymbol pms) {
		routeLayer.setEndSymbol(pms);
	}

	/**
	 * 设置跨层导航切换点符号
	 * 
	 * @param pms
	 *            切换点标识符号
	 */
	public void setSwitchSymbol(TYPictureMarkerSymbol pms) {
		routeLayer.setSwitchSymbol(pms);
	}

	/**
	 * 设置导航起点
	 * 
	 * @param start
	 *            导航起点
	 */
	public void setRouteStart(TYLocalPoint start) {
		routeLayer.setStartPoint(start);
	}

	/**
	 * 设置导航终点
	 * 
	 * @param end
	 *            导航终点
	 */
	public void setRouteEnd(TYLocalPoint end) {
		routeLayer.setEndPoint(end);
	}

	/**
	 * 重置导航层，移除显示的结果，并将导航结果清空
	 */
	public void resetRouteLayer() {
		routeLayer.reset();
		routeHintLayer.removeAll();
		animatedRouteArrowLayer.stopShowingArrow();
	}

	/**
	 * 清除导航层，只在地图上移除相关显示的结果
	 */
	public void clearRouteLayer() {
		routeLayer.removeAll();
		routeHintLayer.removeAll();
		animatedRouteArrowLayer.stopShowingArrow();
	}

	/**
	 * 检测标签，对重叠标签进行处理
	 */
	public void checkLabels() {
		labelGroupLayer.updateLabels();
	}

	/**
	 * 设置定位点符号，用于标识定位结果
	 * 
	 * @param markerSymbol
	 *            定位点符号
	 */
	public void setLocationSymbol(MarkerSymbol markerSymbol) {
		locationLayer.setLcoationSymbol(markerSymbol);
	}

	/**
	 * 在地图上显示定位结果
	 * 
	 * @param location
	 *            定位结果坐标点
	 */
	public void showLocation(TYLocalPoint location) {
		locationLayer.removeAll();
		if (currentMapInfo.getFloorNumber() == location.getFloor()) {
			Point pos = new Point(location.getX(), location.getY());
			locationLayer.showLocation(pos, currentDeviceHeading,
					building.getInitAngle(), mapViewMode);
		}
	}

	/**
	 * 在地图上移除定位结果
	 */
	public void removeLocation() {
		locationLayer.removeLocation();
	}

	/**
	 * 处理设备旋转事件
	 * 
	 * @param newHeading
	 *            设备方向角
	 */
	public void processDeviceRotation(double newHeading) {
		currentDeviceHeading = newHeading;
		locationLayer.updateDeviceHeading(newHeading, building.getInitAngle(),
				mapViewMode);

		switch (mapViewMode) {
		case TYMapViewModeDefault:

			break;

		case TYMapViewModeFollowing:
			setRotationAngle(building.getInitAngle() + currentDeviceHeading,
					false);
			break;

		default:
			break;
		}

		if (Math.abs(lastRotationAngle - getRotationAngle()) > 30) {
			labelGroupLayer.updateLabels();
			lastRotationAngle = getRotationAngle();
		}
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
		structureGroupLayer.clearSelection();
		labelGroupLayer.clearSelection();
	}

	/**
	 * 以屏幕坐标为单位平移x、y距离
	 * 
	 * @param x
	 *            x平移距离
	 * @param y
	 *            y平移距离
	 * @param animated
	 *            是否使用动画
	 */
	void translateInScreenUnit(double x, double y, boolean animated) {
		Point centerScreen = toScreenPoint(getCenter());
		Point newCenterScreen = new Point(centerScreen.getX() - x,
				centerScreen.getY() - y);
		Point newCenter = toMapPoint(newCenterScreen);
		centerAt(newCenter, animated);
	}

	/**
	 * 以地图坐标为单位平移x、y距离
	 * 
	 * @param x
	 *            x平移距离
	 * @param y
	 *            y平移距离
	 * @param animated
	 *            是否使用动画
	 */
	void translateInMapUnit(double x, double y, boolean animated) {
		Point center = getCenter();
		Point newCenter = new Point(center.getX() - x, center.getY() - y);
		centerAt(newCenter, animated);
	}

	/**
	 * 移动地图将特定坐标限定在特定屏幕范围内
	 * 
	 * @param location
	 *            目标点地图坐标
	 * @param range
	 *            目标屏幕范围
	 * @param animated
	 *            是否使用动画
	 */
	public void restrictLocation(Point location, Rect range, boolean animated) {
		Point locationOnScreen = toScreenPoint(location);

		if (range.contains((int) locationOnScreen.getX(),
				(int) locationOnScreen.getY())) {
			return;
		}

		double xOffset = 0;
		double yOffset = 0;

		if (locationOnScreen.getX() < range.left) {
			xOffset = range.left - locationOnScreen.getX();
		}

		if (locationOnScreen.getX() > range.right) {
			xOffset = range.right - locationOnScreen.getX();
		}

		if (locationOnScreen.getY() < range.bottom) {
			yOffset = range.bottom - locationOnScreen.getY();
		}

		if (locationOnScreen.getY() > range.top) {
			yOffset = range.top - locationOnScreen.getY();
		}

		translateInScreenUnit(xOffset, yOffset, animated);
	}

	/**
	 * 设置地图模式
	 * 
	 * @param mode
	 *            目标地图模式，包括默认模式和跟随模式
	 */
	public void setMapMode(TYMapViewMode mode) {
		mapViewMode = mode;
		switch (mapViewMode) {
		case TYMapViewModeDefault:
			setAllowRotationByPinch(true);
			setRotationAngle(0);
			break;

		case TYMapViewModeFollowing:
			setAllowRotationByPinch(false);
			break;

		default:
			break;
		}
	}

	/**
	 * 返回当前楼层的所有公共设施类型
	 * 
	 * @return 公共设施类型数组:[Integer]
	 */
	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return labelGroupLayer.getAllFacilityCategoryIDOnCurrentFloor();
	}

	/**
	 * 显示当前楼层的所有公共设施
	 */
	public void showAllFacilitiesOnCurrentFloor() {
		labelGroupLayer.showAllFacilities();
	}

	/**
	 * 显示当前楼层的特定类型公共设施
	 * 
	 * @param categoryID
	 *            公共设施类型ID
	 */
	public void showFacilityOnCurrentWithCategory(int categoryID) {
		labelGroupLayer.showFacilityWithCategory(categoryID);
	}

	/**
	 * 获取当前楼层下特定子层特定poiID的信息
	 * 
	 * @param pid
	 *            poiID
	 * @param layer
	 *            目标子层
	 * 
	 * @return poi信息
	 */
	public TYPoi getPoiOnCurrentFloorWithPoiID(String pid, POI_LAYER layer) {
		TYPoi result = null;

		switch (layer) {
		case POI_ROOM:
			result = structureGroupLayer.getPoiWithPoiID(pid, layer);
			break;

		case POI_FACILITY:
			result = labelGroupLayer.getPoiWithPoiID(pid, layer);
			break;

		default:
			break;
		}

		return result;
	}

	/**
	 * 高亮显示POI
	 * 
	 * @param poi
	 *            目标poi 目标poi至少包含poiID和layer信息，当前支持ROOM和FACILITY高亮
	 */
	public void highlightPoi(TYPoi poi) {
		switch (poi.getLayer()) {
		case POI_ROOM:
			structureGroupLayer.highlightPoi(poi);
			break;

		case POI_FACILITY:
			labelGroupLayer.highlightPoi(poi);
			break;

		default:
			break;
		}
	}

	/**
	 * 高亮显示一组POI
	 * 
	 * @param poiList
	 *            目标poi数组
	 */
	public void highlightPois(List<TYPoi> poiList) {
		for (TYPoi poi : poiList) {
			highlightPoi(poi);
		}
	}

	/**
	 * 根据坐标x和y提取当前楼层的ROOM POI
	 * 
	 * @param x
	 *            坐标x
	 * @param y
	 *            坐标y
	 * 
	 * @return ROOM POI
	 */
	public TYPoi extractRoomPoiOnCurrentFloor(double x, double y) {
		return structureGroupLayer.extractRoomPoiOnCurrentFloor(x, y);
	}

	@Override
	public void onSingleTap(float x, float y) {
		// Log.i(TAG, "onSingleTap");

		clearSelection();

		Point p = toMapPoint(x, y);
		notifyClickAtPoint(this, p);

		if (listeners.size() > 0) {
			List<TYPoi> poiList = extractSelectedPoi(x, y);
			if (poiList.size() > 0) {
				notifyPoiSelected(this, poiList);
			}
		}

		if (highlightPoiOnSelection) {
			highlightPoiFeature(x, y);
		}
	}

	@Override
	public void postPointerMove(float fromx, float fromy, float tox, float toy) {
	}

	@Override
	public void postPointerUp(float fromx, float fromy, float tox, float toy) {
	}

	private long lastTimeCheckCenter = 0;

	private void checkMapCenter() {

		long now = System.currentTimeMillis();
		if (now - lastTimeCheckCenter < 200) {
			return;
		}

		lastTimeCheckCenter = now;

		Point center = getCenter();

		double x = center.getX();
		double y = center.getY();

		double xmax = currentMapInfo.getMapExtent().getXmax();
		double xmin = currentMapInfo.getMapExtent().getXmin();
		double ymax = currentMapInfo.getMapExtent().getYmax();
		double ymin = currentMapInfo.getMapExtent().getYmin();
		if (x <= xmax && x >= xmin && y <= ymax && y >= ymin) {
			return;
		}

		x = (x > xmax ? xmax : (x < xmin ? xmin : x));
		y = (y > ymax ? ymax : (y < ymin ? ymin : y));

		centerAt(new Point(x, y), true);
	}

	@Override
	public void prePointerMove(float fromx, float fromy, float tox, float toy) {
	}

	@Override
	public void prePointerUp(float fromx, float fromy, float tox, float toy) {
		checkMapCenter();
	}

	@Override
	public void postAction(float pivotX, float pivotY, double factor) {
		labelGroupLayer.updateLabels();

		if (listeners.size() > 0) {
			notifyMapDidZoomed(this);
		}
	}

	@Override
	public void preAction(float pivotX, float pivotY, double factor) {

	}

	private void highlightPoiFeature(float x, float y) {
		boolean isHighlighted = false;

		if (!isHighlighted) {
			isHighlighted = labelGroupLayer.highlightPoiFeature(x, y,
					DEFAULT_TOLERANCE);
		}

		if (!isHighlighted) {
			isHighlighted = structureGroupLayer.highlightPoiFeature(x, y,
					DEFAULT_TOLERANCE);
		}
	}

	private List<TYPoi> extractSelectedPoi(float x, float y) {
		// Log.i(TAG, "extractSelectedPoi");
		List<TYPoi> poiList = new ArrayList<TYPoi>();

		poiList.addAll(labelGroupLayer.extractSelectedPoi(x, y,
				DEFAULT_TOLERANCE));
		poiList.addAll(structureGroupLayer.extractSelectedPoi(x, y,
				DEFAULT_TOLERANCE));

		return poiList;
	}

	/**
	 * 地图事件接口
	 */
	public interface TYMapViewListenser {
		/**
		 * 地图点选事件回调方法
		 * 
		 * @param mapView
		 *            地图视图
		 * @param mappoint
		 *            点击事件的地图坐标
		 */
		void onClickAtPoint(TYMapView mapView, Point mappoint);

		/**
		 * 地图POI选中事件回调
		 * 
		 * @param mapView
		 *            地图视图
		 * @param array
		 *            选中的POI数组:[NPPoi]
		 */
		void onPoiSelected(TYMapView mapView, List<TYPoi> poiList);

		/**
		 * 地图楼层加载完成回调
		 * 
		 * @param mapView
		 *            地图视图
		 * @param mapInfo
		 *            加载楼层信息
		 */
		void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo);

		/**
		 * 地图放缩事件回调
		 * 
		 * @param mapView
		 *            地图视图
		 */
		void mapViewDidZoomed(TYMapView mapView);
	}

	/**
	 * 添加地图事件监听
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addMapListener(TYMapViewListenser listener) {
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
	public void removeMapListener(TYMapViewListenser listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		for (TYMapViewListenser listener : listeners) {
			listener.onPoiSelected(mapView, poiList);
		}
	}

	private void notifyClickAtPoint(TYMapView mapView, Point mappoint) {
		for (TYMapViewListenser listener : listeners) {
			listener.onClickAtPoint(mapView, mappoint);
		}
	}

	private void notifyFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		// labelGroupLayer.updateLabels();
		for (TYMapViewListenser listener : listeners) {
			listener.onFinishLoadingFloor(mapView, mapInfo);
		}
	}

	private void notifyMapDidZoomed(TYMapView mapView) {
		for (TYMapViewListenser listener : listeners) {
			listener.mapViewDidZoomed(mapView);
		}
	}

	@Override
	public void pause() {
		super.pause();
		if (animatedRouteArrowLayer != null) {
			animatedRouteArrowLayer.stopShowingArrow();
		}
	}

	/**
	 * 地图模式类型：默认模式和跟随模式
	 */
	public enum TYMapViewMode {
		TYMapViewModeDefault, TYMapViewModeFollowing
	}

	@Override
	public void onStatusChanged(Object source, STATUS status) {
		switch (status) {
		case INITIALIZED:
			isMapInitlized = true;
			break;

		case INITIALIZATION_FAILED:
			break;

		case LAYER_LOADED:
			break;

		case LAYER_LOADING_FAILED:
			break;

		default:
			break;
		}
	}
}
