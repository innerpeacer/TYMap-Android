package cn.nephogram.mapsdk;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import cn.nephogram.mapsdk.data.NPAssetsManager;
import cn.nephogram.mapsdk.data.NPFileManager;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.layer.NPAssetLayer;
import cn.nephogram.mapsdk.layer.NPFacilityLayer;
import cn.nephogram.mapsdk.layer.NPFloorLayer;
import cn.nephogram.mapsdk.layer.NPLabelLayer;
import cn.nephogram.mapsdk.layer.NPRoomLayer;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;

public class NPMapView extends MapView {
	static final String TAG = NPMapView.class.getSimpleName();

	private Context context;

	private String marketID;
	private NPMapInfo currentMapInfo;

	private NPFloorLayer floorLayer;
	private NPRoomLayer roomLayer;
	private NPAssetLayer assetLayer;

	private NPFacilityLayer facilityLayer;
	private NPLabelLayer labelLayer;

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

	private void init(NPMapInfo info) {
		Log.i(TAG, "init");

		Envelope envelope = new Envelope(info.getMapExtent().getXmin(), info
				.getMapExtent().getYmin(), info.getMapExtent().getXmax(), info
				.getMapExtent().getYmax());
		SpatialReference sr = NPMapEnvironment.defaultSpatialReference();

		floorLayer = new NPFloorLayer(context, sr, envelope);
		addLayer(floorLayer);

		roomLayer = new NPRoomLayer(context, sr, envelope);
		addLayer(roomLayer);

		assetLayer = new NPAssetLayer(context, sr, envelope);
		addLayer(assetLayer);

		facilityLayer = new NPFacilityLayer(context, sr, envelope);
		addLayer(facilityLayer);

		labelLayer = new NPLabelLayer(context, sr, envelope);
		addLayer(labelLayer);
	}

	public void setFloor(NPMapInfo info) {
		if (currentMapInfo == null) {
			init(info);
			currentMapInfo = info;
		}

		boolean isUsingFileFromAssets = true;
		// isUsingFileFromAssets = true;
		if (isUsingFileFromAssets) {
			String floorPathFromAssets = NPAssetsManager.getFloorFilePath(info
					.getMapID());
			floorLayer.loadContentsFromAssetsWithInfo(floorPathFromAssets);

			String roomPathFromAsset = NPAssetsManager.getRoomFilePath(info
					.getMapID());
			roomLayer.loadContentsFromAssetsWithInfo(roomPathFromAsset);

			String assetPathFromAsset = NPAssetsManager.getAssetFilePath(info
					.getMapID());
			assetLayer.loadContentsFromAssetsWithInfo(assetPathFromAsset);

			String facilityPathFromAsset = NPAssetsManager
					.getFacilityFilePath(info.getMapID());
			facilityLayer.loadContentsFromAssetsWithInfo(facilityPathFromAsset);

			String labelPathFromAsset = NPAssetsManager.getLabelFilePath(info
					.getMapID());
			labelLayer.loadContentsFromAssetsWithInfo(labelPathFromAsset);

		} else {
			String floorPathFromFile = NPFileManager.getFloorFilePath(info
					.getMapID());
			floorLayer.loadContentsFromFileWithInfo(floorPathFromFile);

			String roomPathFromFile = NPFileManager.getRoomFilePath(info
					.getMapID());
			roomLayer.loadContentsFromAssetsWithInfo(roomPathFromFile);

			String assetPathFromFile = NPFileManager.getAssetFilePath(info
					.getMapID());
			assetLayer.loadContentsFromAssetsWithInfo(assetPathFromFile);

			String facilityPathFromFile = NPFileManager
					.getFacilityFilePath(info.getMapID());
			facilityLayer.loadContentsFromFileWithInfo(facilityPathFromFile);

			String labelPathFromFile = NPFileManager.getLabelFilePath(info
					.getMapID());
			labelLayer.loadContentsFromFileWithInfo(labelPathFromFile);
		}

	}

	public String getMarketID() {
		return marketID;
	}

	public void setMarketID(String marketID) {
		this.marketID = marketID;
	}

	public NPMapInfo getCurrentMapInfo() {
		return currentMapInfo;
	}

	public void setCurrentMapInfo(NPMapInfo currentMapInfo) {
		this.currentMapInfo = currentMapInfo;
	}

}
