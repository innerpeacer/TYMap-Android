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
//import android.content.Context;
//
//import com.ty.mapdata.TYBuilding;
//import com.ty.mapsdk.IPMapFileManager;
//
//class IPBuildingJsonParser {
//	public static final String JSON_KEY_BUILDINGS = "Buildings";
//
//	public static List<TYBuilding> parseBuildingFromFiles(Context context,
//			String buildingID) {
//
//		List<TYBuilding> buildings = new ArrayList<TYBuilding>();
//
//		try {
//			String path = IPMapFileManager.getBuildingJsonPath(buildingID);
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
//			if (jsonObject != null && !jsonObject.isNull(JSON_KEY_BUILDINGS)) {
//				JSONArray array = jsonObject.getJSONArray(JSON_KEY_BUILDINGS);
//				for (int i = 0; i < array.length(); i++) {
//					TYBuilding building = new TYBuilding();
//					building.parseJson(array.optJSONObject(i));
//					buildings.add(building);
//				}
//			}
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
//
//		return buildings;
//	}
//
//	public static TYBuilding parseBuildingFromFilesById(Context context,
//			String cityID, String buildingID) {
//		List<TYBuilding> buildings = parseBuildingFromFiles(context, cityID);
//
//		for (int i = 0; i < buildings.size(); i++) {
//			TYBuilding building = buildings.get(i);
//			if (building.getBuildingID().equals(buildingID)) {
//				return building;
//			}
//		}
//		return null;
//	}
//
//	public static TYBuilding parseBuildingFromFilesByName(Context context,
//			String cityID, String buildingName) {
//		List<TYBuilding> buildings = parseBuildingFromFiles(context, cityID);
//
//		for (int i = 0; i < buildings.size(); i++) {
//			TYBuilding building = buildings.get(i);
//			if (building.getName().equals(buildingName)) {
//				return building;
//			}
//		}
//		return null;
//	}
// }
