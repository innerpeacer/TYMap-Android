//package com.ty.mapproject.activities;
//
//import java.util.List;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.esri.android.map.GraphicsLayer;
//import com.esri.core.geometry.Point;
//import com.esri.core.map.Graphic;
//import com.esri.core.symbol.SimpleMarkerSymbol;
//import com.ty.mapdata.TYLocalPoint;
//import com.ty.mapproject.R;
//import com.ty.mapsdk.TYMapInfo;
//import com.ty.mapsdk.TYMapView;
//import com.ty.mapsdk.TYOfflineRouteManager;
//import com.ty.mapsdk.TYOfflineRouteManager.TYOfflineRouteManagerListener;
//import com.ty.mapsdk.TYPictureMarkerSymbol;
//import com.ty.mapsdk.TYPoi;
//import com.ty.mapsdk.TYRoutePart;
//import com.ty.mapsdk.TYRouteResult;
//
//public class CopyOfMapOfflineRouteActivity extends BaseMapViewActivity
//		implements TYOfflineRouteManagerListener {
//	static final String TAG = CopyOfMapOfflineRouteActivity.class
//			.getSimpleName();
//
//	Point currentPoint;
//
//	TYOfflineRouteManager offlineRouteManager;
//
//	GraphicsLayer hintLayer;
//
//	boolean isRouteManagerReady = false;
//
//	boolean useClickForSnapRoute = false;
//	boolean useClickForChoosingPoint = true;
//
//	// 变量声明
//	// 是否处于导航状态；导航状态置为true，取消导航置为false
//	boolean isRouting;
//	// 导航结果
//	TYRouteResult routeResult;
//	TYLocalPoint startPoint;
//	// 导航终点
//	TYLocalPoint endPoint;
//
//	// 定位结果回调
//	@Override
//	public void didUpdateLocation(TYLocationManager locationManager,
//			TYLocalPoint lp) {
//		// 判断当前处于导航状态，并且导航结果不为空
//		if (isRouting && routeResult != null) {
//			// 利用当前定位位置判断是否偏离路线，第二个参数为偏离阈值，可自行设置
//			boolean isDeviating = routeResult.isDeviatingFromRoute(lp, 5.0f);
//			if (isDeviating) {
//				// 表明已经偏离路线，重新规划
//				// 此时以当前位置为起点重新规划
//				offlineRouteManager.requestRoute(lp, endPoint);
//			}
//		}
//
//		{
//			if (mapView.getCurrentMapInfo().getFloorNumber() != lp.getFloor()) {
//				for (TYMapInfo info : mapInfos) {
//					if (info.getFloorNumber() == lp.getFloor()) {
//						currentMapInfo = info;
//						mapView.setFloor(info);
//						setTitle(String.format("%s-%s",
//								currentBuilding.getName(),
//								currentMapInfo.getFloorName()));
//						break;
//					}
//				}
//			}
//			mapView.showLocation(lp);
//			mapView.centerAt(new Point(lp.getX(), lp.getY()), true);
//			Log.i(TAG, "Location: " + lp);
//		}
//	}
//
//	// 导航结果回调
//	@Override
//	public void didSolveRouteWithResult(TYOfflineRouteManager routeManager,
//			TYRouteResult rs) {
//		// 导航状态置为true
//		isRouting = true;
//		// 保存导航结果
//		routeResult = rs;
//
//		Log.i(TAG, "TYOfflineRouteManager.didSolveRouteWithResult()");
//		Log.i(TAG, rs + "");
//		hintLayer.removeAll();
//
//		routeResult = rs;
//		mapView.setRouteResult(rs);
//		mapView.setRouteStart(startPoint);
//		mapView.setRouteEnd(endPoint);
//
//		mapView.showRouteResultOnCurrentFloor();
//	}
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		initMapSettings();
//		initSymbols();
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				offlineRouteManager = new TYOfflineRouteManager(
//						currentBuilding, mapInfos);
//				offlineRouteManager
//						.addRouteManagerListener(CopyOfMapOfflineRouteActivity.this);
//				isRouteManagerReady = true;
//			}
//		}).start();
//	}
//
//	@Override
//	public void didFailSolveRouteWithError(TYOfflineRouteManager routeManager,
//			Exception e) {
//		Log.i(TAG, "TYOfflineRouteManager.didFailSolveRouteWithError");
//
//	}
//
//	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
//		if (isRouting) {
//			List<TYRoutePart> parts = routeResult.getRoutePartsOnFloor(mapInfo
//					.getFloorNumber());
//			if (parts != null && parts.size() > 0) {
//				TYRoutePart rp = parts.get(0);
//				mapView.setExtent(rp.getRoute());
//				// mapView.checkLabels();
//			}
//			mapView.showRouteResultOnCurrentFloor();
//		}
//	};
//
//	private void initMapSettings() {
//		hintLayer = new GraphicsLayer();
//		mapView.addLayer(hintLayer);
//	}
//
//	private void initSymbols() {
//		TYPictureMarkerSymbol startSymbol = new TYPictureMarkerSymbol(
//				getResources().getDrawable(R.drawable.start));
//		startSymbol.setWidth(34);
//		startSymbol.setHeight(43);
//		startSymbol.setOffsetX(0);
//		startSymbol.setOffsetY(22);
//		mapView.setStartSymbol(startSymbol);
//
//		TYPictureMarkerSymbol endSymbol = new TYPictureMarkerSymbol(
//				getResources().getDrawable(R.drawable.end));
//		endSymbol.setWidth(34);
//		endSymbol.setHeight(43);
//		endSymbol.setOffsetX(0);
//		endSymbol.setOffsetY(22);
//		mapView.setEndSymbol(endSymbol);
//
//		TYPictureMarkerSymbol switchSymbol = new TYPictureMarkerSymbol(
//				getResources().getDrawable(R.drawable.nav_exit));
//		switchSymbol.setWidth(37);
//		switchSymbol.setHeight(37);
//		mapView.setSwitchSymbol(switchSymbol);
//	}
//
//	private void requestRoute() {
//		// Log.i(TAG, "requestRoute");
//
//		if (!isRouteManagerReady) {
//			return;
//		}
//
//		if (startPoint == null || endPoint == null) {
//			return;
//		}
//		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
//		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);
//		mapView.resetRouteLayer();
//
//		routeResult = null;
//		isRouting = true;
//
//		Log.i(TAG, startPoint.getX() + "");
//		Log.i(TAG, startPoint.getY() + "");
//
//		mapView.showRouteStartSymbolOnCurrentFloor(startPoint);
//		mapView.showRouteEndSymbolOnCurrentFloor(endPoint);
//		offlineRouteManager.requestRoute(startPoint, endPoint);
//	}
//
//	@Override
//	public void initContentViewID() {
//		contentViewID = R.layout.activity_map_offline_route;
//	}
//
//	int index = 0;
//
//	@Override
//	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
//		super.onClickAtPoint(mapView, mappoint);
//		currentPoint = mappoint;
//
//		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 5,
//				com.esri.core.symbol.SimpleMarkerSymbol.STYLE.CIRCLE);
//		hintLayer.removeAll();
//		hintLayer.addGraphic(new Graphic(mappoint, sms));
//
//		Log.i(TAG, "Click: " + mappoint.getX() + ", " + mappoint.getY());
//
//		TYLocalPoint localPoint = new TYLocalPoint(mappoint.getX(),
//				mappoint.getY(), currentMapInfo.getFloorNumber());
//
//		if (useClickForChoosingPoint) {
//			endPoint = startPoint;
//			startPoint = localPoint;
//			requestRoute();
//		}
//
//		if (useClickForSnapRoute) {
//			// mapView.showRemainingRouteResultOnCurrentFloor(localPoint);
//			mapView.showPassedAndRemainingRouteResultOnCurrentFloor(localPoint);
//		}
//
//	}
//
//	@Override
//	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
//		super.onPoiSelected(mapView, poiList);
//	}
//
//	@Override
//	public void mapViewDidZoomed(TYMapView mapView) {
//		super.mapViewDidZoomed(mapView);
//		if (isRouting) {
//			mapView.showRouteResultOnCurrentFloor();
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		menu.add(0, 0, 0, "Set Point for Navigation");
//		menu.add(1, 1, 1, "Snap to Route");
//
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case 0:
//			useClickForChoosingPoint = true;
//			useClickForSnapRoute = false;
//			break;
//
//		case 1:
//			useClickForChoosingPoint = false;
//			useClickForSnapRoute = true;
//			break;
//		default:
//			break;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
// }
