package com.ty.mapsdk.entity;

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
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 城市类
 */
public class TYCity implements Parcelable {

	private static final String JSON_KEY_CITIES = "Cities";

	private static final String KEY_CITY_ID = "id";
	private static final String KEY_CITY_NAME = "name";
	private static final String KEY_CITY_SHORT_NAME = "sname";
	private static final String KEY_CITY_LONGITUDE = "longitude";
	private static final String KEY_CITY_LATITUDE = "latitude";
	private static final String KEY_CITY_STATUS = "status";

	private String cityID;
	private String name;
	private String sname;

	private double longitude;
	private double latitude;

	private int status;

	public TYCity() {
		super();
	}

	TYCity(Parcel in) {
		cityID = in.readString();
		name = in.readString();
		sname = in.readString();

		longitude = in.readDouble();
		latitude = in.readDouble();

		status = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cityID);
		dest.writeString(name);
		dest.writeString(sname);

		dest.writeDouble(longitude);
		dest.writeDouble(latitude);

		dest.writeInt(status);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof TYCity) {
			if (this.cityID.equals(((TYCity) obj).getCityID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<TYCity> CREATOR = new Creator<TYCity>() {
		@Override
		public TYCity[] newArray(int size) {
			return new TYCity[size];
		}

		@Override
		public TYCity createFromParcel(Parcel source) {
			return new TYCity(source);
		}
	};

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_CITY_ID)) {
				setCityID(jsonObject.optString(KEY_CITY_ID));
			}
			if (!jsonObject.isNull(KEY_CITY_NAME)) {
				setName(jsonObject.optString(KEY_CITY_NAME));
			}
			if (!jsonObject.isNull(KEY_CITY_SHORT_NAME)) {
				setSname(jsonObject.optString(KEY_CITY_SHORT_NAME));
			}
			if (!jsonObject.isNull(KEY_CITY_LONGITUDE)) {
				setLongitude(jsonObject.optDouble(KEY_CITY_LONGITUDE));
			}
			if (!jsonObject.isNull(KEY_CITY_LATITUDE)) {
				setLatitude(jsonObject.optDouble(KEY_CITY_LATITUDE));
			}
			if (!jsonObject.isNull(KEY_CITY_STATUS)) {
				setStatus(jsonObject.optInt(KEY_CITY_STATUS));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_CITY_ID, cityID);
			jsonObject.put(KEY_CITY_NAME, name);
			jsonObject.put(KEY_CITY_SHORT_NAME, sname);
			jsonObject.put(KEY_CITY_LONGITUDE, longitude);
			jsonObject.put(KEY_CITY_LATITUDE, latitude);
			jsonObject.put(KEY_CITY_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 城市ID
	 */
	public String getCityID() {
		return cityID;
	}

	/**
	 * 城市名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 城市简称
	 */
	public String getSname() {
		return sname;
	}

	/**
	 * 城市经度
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 城市纬度
	 */

	public double getLatitude() {
		return latitude;
	}

	/**
	 * 当前状态
	 */
	public int getStatus() {
		return status;
	}

	protected void setCityID(String cityID) {
		this.cityID = cityID;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setSname(String sname) {
		this.sname = sname;
	}

	protected void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	protected void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	protected void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CityID = " + cityID + ", CityName = " + name;
	}

	/**
	 * 从外部存储目录解析所有城市信息信息列表
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @return 城市类数组
	 */
	public static List<TYCity> parseCityFromFiles(Context context, String path) {
		List<TYCity> cities = new ArrayList<TYCity>();

		try {
			FileInputStream inStream = new FileInputStream(new File(path));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(TYCity.JSON_KEY_CITIES)) {
				JSONArray array = jsonObject
						.getJSONArray(TYCity.JSON_KEY_CITIES);
				for (int i = 0; i < array.length(); i++) {
					TYCity city = new TYCity();
					city.parseJson(array.optJSONObject(i));
					cities.add(city);
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
		return cities;
	}

	/**
	 * 从外部存储目录按ID解析特定城市信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityId
	 *            城市ID
	 * 
	 * @return 城市类
	 */
	public static TYCity parseCityFromFilesById(Context context, String path,
			String cityId) {
		List<TYCity> cities = parseCityFromFiles(context, path);

		for (int i = 0; i < cities.size(); i++) {
			TYCity city = cities.get(i);
			if (city.getCityID().equals(cityId)) {
				return city;
			}
		}
		return null;
	}

	/**
	 * 从外部存储目录按名称解析特定城市信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityName
	 *            城市名称
	 * 
	 * @return 城市类
	 */
	public static TYCity parseCityFromFilesByName(Context context, String path,
			String cityName) {
		List<TYCity> cities = parseCityFromFiles(context, path);

		for (int i = 0; i < cities.size(); i++) {
			TYCity city = cities.get(i);
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}
}