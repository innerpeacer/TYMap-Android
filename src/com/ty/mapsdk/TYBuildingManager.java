package com.ty.mapsdk;

import java.util.List;

import android.content.Context;

import com.ty.mapdata.TYBuilding;

/**
 * 建筑管理类，用于管理建筑数据
 * 
 * @author innerpeacer
 * 
 */
public class TYBuildingManager {

	/**
	 * 从外部存储目录解析所有建筑信息列表
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityID
	 *            城市ID
	 * @return 建筑类数组
	 */
	public static List<TYBuilding> parseBuildingFromFiles(Context context,
			String cityID) {
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		List<TYBuilding> buildingArray = db.getAllBuildings(cityID);
		db.close();
		return buildingArray;
		// return IPBuildingJsonParser.parseBuildingFromFiles(context,
		// buildingID);
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
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		TYBuilding building = db.getBuildingByID(cityID, buildingID);
		db.close();
		return building;
		// return IPBuildingJsonParser.parseBuildingFromFilesById(context,
		// cityID,
		// buildingID);
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
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		TYBuilding building = db.getBuildingByName(buildingName);
		db.close();
		return building;
		// return IPBuildingJsonParser.parseBuildingFromFilesByName(context,
		// cityID, buildingName);
	}

}
