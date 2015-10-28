//package com.deprecated;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.ty.mapsdk.IPMapFileManager;
//import com.ty.mapsdk.TYMapInfo;
//
//import android.content.Context;
//
//class IPMapInfoJsonParser {
//	public static final String JSON_KEY_MAPINFOS = "MapInfo";
//
//	private static final String KEY_MAPINFO_CITYID = "cityID";
//	private static final String KEY_MAPINFO_BUILDINGID = "buildingID";
//	private static final String KEY_MAP_ID = "mapID";
//	private static final String KEY_FLOOR_NAME = "floorName";
//	private static final String KEY_FLOOR_NUMBER = "floorNumber";
//
//	private static final String KEY_SIZE_X = "size_x";
//	private static final String KEY_SIZE_Y = "size_y";
//
//	private static final String KEY_X_MIN = "xmin";
//	private static final String KEY_X_MAX = "xmax";
//	private static final String KEY_Y_MIN = "ymin";
//	private static final String KEY_Y_MAX = "ymax";
//
//	public IPMapInfoJsonParser() {
//
//	}
//
//	public static List<TYMapInfo> parseMapInfoFromFiles(Context context,
//			String cityID, String buildingID) {
//
//		List<TYMapInfo> mapInfos = new ArrayList<TYMapInfo>();
//
//		try {
//			String path = IPMapFileManager.getMapInfoJsonPath(cityID,
//					buildingID);
//			FileInputStream inStream = new FileInputStream(new File(path));
//			InputStreamReader inputReader = new InputStreamReader(inStream);
//			BufferedReader bufReader = new BufferedReader(inputReader);
//
//			String line = "";
//			StringBuffer jsonStr = new StringBuffer();
//			while ((line = bufReader.readLine()) != null)
//				jsonStr.append(line);
//
//			JSONObject jsonObject = new JSONObject(jsonStr.toString());
//			if (jsonObject != null && !jsonObject.isNull(JSON_KEY_MAPINFOS)) {
//				JSONArray array = jsonObject.getJSONArray(JSON_KEY_MAPINFOS);
//				for (int i = 0; i < array.length(); i++) {
//					TYMapInfo mapInfo = new TYMapInfo();
//					mapInfo = parseJson(array.optJSONObject(i));
//					mapInfos.add(mapInfo);
//				}
//			}
//
//			inputReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return mapInfos;
//	}
//
//	public static TYMapInfo parseMapInfoFromFilesById(Context context,
//			String cityID, String buildingID, String mapID) {
//		List<TYMapInfo> mapInfos = parseMapInfoFromFiles(context, cityID,
//				buildingID);
//
//		for (int i = 0; i < mapInfos.size(); i++) {
//			TYMapInfo mapInfo = mapInfos.get(i);
//			if (mapInfo.getMapID().equals(mapID)) {
//				return mapInfo;
//			}
//		}
//		return null;
//	}
//
//	public static TYMapInfo searchMapInfoFromArray(List<TYMapInfo> array,
//			int floor) {
//		for (TYMapInfo info : array) {
//			if (floor == info.getFloorNumber()) {
//				return info;
//			}
//		}
//		return null;
//	}
//
//	public static TYMapInfo parseJson(JSONObject jsonObject) {
//		TYMapInfo mapInfo = new TYMapInfo();
//		if (jsonObject != null) {
//			if (!jsonObject.isNull(KEY_MAPINFO_CITYID)) {
//				mapInfo.setCityID(jsonObject.optString(KEY_MAPINFO_CITYID));
//			}
//
//			if (!jsonObject.isNull(KEY_MAPINFO_BUILDINGID)) {
//				mapInfo.setBuildingID(jsonObject
//						.optString(KEY_MAPINFO_BUILDINGID));
//			}
//			if (!jsonObject.isNull(KEY_MAP_ID)) {
//				mapInfo.setMapID(jsonObject.optString(KEY_MAP_ID));
//			}
//			if (!jsonObject.isNull(KEY_FLOOR_NAME)) {
//				mapInfo.setFloorName(jsonObject.optString(KEY_FLOOR_NAME));
//			}
//			if (!jsonObject.isNull(KEY_FLOOR_NUMBER)) {
//				mapInfo.setFloorNumber(jsonObject.optInt(KEY_FLOOR_NUMBER));
//			}
//			if (!jsonObject.isNull(KEY_SIZE_X)) {
//				mapInfo.setSize_x(jsonObject.optDouble(KEY_SIZE_X));
//			}
//			if (!jsonObject.isNull(KEY_SIZE_Y)) {
//				mapInfo.setSize_y(jsonObject.optDouble(KEY_SIZE_Y));
//			}
//			if (!jsonObject.isNull(KEY_X_MIN)) {
//				mapInfo.setXmin(jsonObject.optDouble(KEY_X_MIN));
//			}
//			if (!jsonObject.isNull(KEY_X_MAX)) {
//				mapInfo.setXmax(jsonObject.optDouble(KEY_X_MAX));
//			}
//			if (!jsonObject.isNull(KEY_Y_MIN)) {
//				mapInfo.setYmin(jsonObject.optDouble(KEY_Y_MIN));
//			}
//			if (!jsonObject.isNull(KEY_Y_MAX)) {
//				mapInfo.setYmax(jsonObject.optDouble(KEY_Y_MAX));
//			}
//		}
//
//		return mapInfo;
//	}
//
//	public static JSONObject buildJson(TYMapInfo info) {
//		JSONObject jsonObject = new JSONObject();
//		try {
//			jsonObject.put(KEY_MAPINFO_CITYID, info.cityID);
//			jsonObject.put(KEY_MAPINFO_BUILDINGID, info.buildingID);
//			jsonObject.put(KEY_MAP_ID, info.mapID);
//			jsonObject.put(KEY_FLOOR_NAME, info.floorName);
//			jsonObject.put(KEY_FLOOR_NUMBER, info.floorNumber);
//			jsonObject.put(KEY_SIZE_X, info.size_x);
//			jsonObject.put(KEY_SIZE_Y, info.size_y);
//			jsonObject.put(KEY_X_MIN, info.xmin);
//			jsonObject.put(KEY_X_MAX, info.xmax);
//			jsonObject.put(KEY_Y_MIN, info.ymin);
//			jsonObject.put(KEY_Y_MAX, info.ymax);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return jsonObject;
//	}
//
// }
