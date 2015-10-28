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
//import com.ty.mapdata.TYCity;
//import com.ty.mapsdk.IPMapFileManager;
//
//class IPCityJsonParser {
//	private static final String JSON_KEY_CITIES = "Cities";
//
//	public static List<TYCity> parseCityFromFiles(Context context) {
//		List<TYCity> cities = new ArrayList<TYCity>();
//
//		try {
//			String path = IPMapFileManager.getCityJsonPath();
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
//			if (jsonObject != null && !jsonObject.isNull(JSON_KEY_CITIES)) {
//				JSONArray array = jsonObject.getJSONArray(JSON_KEY_CITIES);
//				for (int i = 0; i < array.length(); i++) {
//					TYCity city = new TYCity();
//					city.parseJson(array.optJSONObject(i));
//					cities.add(city);
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
//		return cities;
//	}
//
//	public static TYCity parseCityFromFilesById(Context context, String cityId) {
//		List<TYCity> cities = parseCityFromFiles(context);
//
//		for (int i = 0; i < cities.size(); i++) {
//			TYCity city = cities.get(i);
//			if (city.getCityID().equals(cityId)) {
//				return city;
//			}
//		}
//		return null;
//	}
//
//	public static TYCity parseCityFromFilesByName(Context context,
//			String cityName) {
//		List<TYCity> cities = parseCityFromFiles(context);
//
//		for (int i = 0; i < cities.size(); i++) {
//			TYCity city = cities.get(i);
//			if (city.getName().equals(cityName)) {
//				return city;
//			}
//		}
//		return null;
//	}
//
// }
