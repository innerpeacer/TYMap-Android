package com.ty.mapsdk;

import java.util.List;

import android.content.Context;

import com.ty.mapdata.TYCity;

/**
 * 城市管理类，用于管理城市数据
 * 
 * @author innerpeacer
 * 
 */
public class TYCityManager {

	/**
	 * 从外部存储目录解析所有城市信息信息列表
	 * 
	 * @param context
	 *            上下文环境
	 * @return 城市类数组
	 */
	public static List<TYCity> parseCityFromFiles(Context context) {
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		List<TYCity> cityArray = db.getAllCities();
		db.close();
		return cityArray;
		// return IPCityJsonParser.parseCityFromFiles(context);
	}

	/**
	 * 从外部存储目录按ID解析特定城市信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityId
	 *            城市ID
	 * @return 城市类
	 */
	public static TYCity parseCityFromFilesById(Context context, String cityId) {
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		TYCity city = db.getCityByID(cityId);
		db.close();
		return city;
		// return IPCityJsonParser.parseCityFromFilesById(context, cityId);
	}

	/**
	 * 从外部存储目录按名称解析特定城市信息
	 * 
	 * @param context
	 *            上下文环境
	 * 
	 * @param cityName
	 *            城市名称
	 * 
	 * @return 城市类
	 */
	public static TYCity parseCityFromFilesByName(Context context,
			String cityName) {
		String dbPath = IPMapFileManager.getMapDBPath();
		IPMapDBAdapter db = new IPMapDBAdapter(dbPath);
		db.open();
		TYCity city = db.getCityByName(cityName);
		db.close();
		return city;
		// return IPCityJsonParser.parseCityFromFilesByName(context, cityName);
	}
}
