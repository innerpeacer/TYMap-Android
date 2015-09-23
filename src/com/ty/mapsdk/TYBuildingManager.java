package com.ty.mapsdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ty.mapdata.TYBuilding;

/**
 * 建筑管理类，用于管理建筑数据
 * 
 * @author innerpeacer
 * 
 */
public class TYBuildingManager {

	public static final String JSON_KEY_BUILDINGS = "Buildings";

	/**
	 * 从外部存储目录解析所有建筑信息列表
	 * 
	 * @param context
	 *            上下文环境
	 * @param buildingID
	 *            建筑ID
	 * @return 建筑类数组
	 */
	public static List<TYBuilding> parseBuildingFromFiles(Context context,
			String buildingID) {

		List<TYBuilding> buildings = new ArrayList<TYBuilding>();

		try {
			String path = IPMapFileManager.getBuildingJsonPath(buildingID);
			FileInputStream inStream = new FileInputStream(new File(path));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(TYBuildingManager.JSON_KEY_BUILDINGS)) {
				JSONArray array = jsonObject
						.getJSONArray(TYBuildingManager.JSON_KEY_BUILDINGS);
				for (int i = 0; i < array.length(); i++) {
					TYBuilding building = new TYBuilding();
					building.parseJson(array.optJSONObject(i));
					buildings.add(building);
				}
			}
			inputReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buildings;
	}

	/**
	 * 从外部存储目录按ID解析特定建筑信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityID
	 *            城市ID
	 * @param buildingID
	 *            建筑ID
	 * @return 建筑类
	 */
	public static TYBuilding parseBuildingFromFilesById(Context context,
			String cityID, String buildingID) {
		List<TYBuilding> buildings = parseBuildingFromFiles(context, cityID);

		for (int i = 0; i < buildings.size(); i++) {
			TYBuilding building = buildings.get(i);
			if (building.getBuildingID().equals(buildingID)) {
				return building;
			}
		}
		return null;
	}

	/**
	 * 从外部存储目录按名称解析特定建筑信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityID
	 *            城市ID
	 * @param buildingName
	 *            建筑名称
	 * 
	 * @return 建筑类
	 */
	public static TYBuilding parseBuildingFromFilesByName(Context context,
			String cityID, String buildingName) {
		List<TYBuilding> buildings = parseBuildingFromFiles(context, cityID);

		for (int i = 0; i < buildings.size(); i++) {
			TYBuilding building = buildings.get(i);
			if (building.getName().equals(buildingName)) {
				return building;
			}
		}
		return null;
	}

}
