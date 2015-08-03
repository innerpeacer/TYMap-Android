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
 * 建筑类，表示整栋建筑的信息
 */
public class TYBuilding implements Parcelable {
	public static final String JSON_KEY_BUILDINGS = "Buildings";

	private static final String KEY_BUILDING_CITY_ID = "cityID";
	private static final String KEY_BUILDING_ID = "id";

	private static final String KEY_BUILDING_NAME = "name";
	private static final String KEY_BUILDING_ADDRESS = "address";
	private static final String KEY_BUILDING_LONGITUDE = "longitude";
	private static final String KEY_BUILDING_LATITUDE = "latitude";

	private static final String KEY_BUILDING_INIT_ANGLE = "initAngle";
	private static final String KEY_BUILDING_ROUTE_URL = "routeURL";
	private static final String KEY_BUILDING_OFFSET_X = "offsetX";
	private static final String KEY_BUILDING_OFFSET_Y = "offsetY";

	private static final String KEY_BUILDING_STATUS = "status";

	private String cityID;
	private String buildingID;
	private String name;
	private String address;

	private double longitude;
	private double latitude;

	private double initAngle;
	private String routeURL;
	private TYMapSize offset;

	private int status;

	public TYBuilding() {
		super();
	}

	TYBuilding(Parcel in) {
		cityID = in.readString();
		buildingID = in.readString();
		name = in.readString();
		address = in.readString();

		longitude = in.readDouble();
		latitude = in.readDouble();

		initAngle = in.readDouble();
		routeURL = in.readString();
		double x = in.readDouble();
		double y = in.readDouble();
		offset = new TYMapSize(x, y);

		status = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cityID);
		dest.writeString(buildingID);
		dest.writeString(name);
		dest.writeString(address);

		dest.writeDouble(longitude);
		dest.writeDouble(latitude);

		dest.writeDouble(initAngle);
		dest.writeString(routeURL);
		dest.writeDouble(offset.x);
		dest.writeDouble(offset.y);

		dest.writeInt(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof TYBuilding) {
			if (this.buildingID.equals(((TYBuilding) obj).getBuildingID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<TYBuilding> CREATOR = new Creator<TYBuilding>() {
		@Override
		public TYBuilding[] newArray(int size) {
			return new TYBuilding[size];
		}

		@Override
		public TYBuilding createFromParcel(Parcel source) {
			return new TYBuilding(source);
		}
	};

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_BUILDING_CITY_ID)) {
				setCityID(jsonObject.optString(KEY_BUILDING_CITY_ID));
			}

			if (!jsonObject.isNull(KEY_BUILDING_ID)) {
				setBuildingID(jsonObject.optString(KEY_BUILDING_ID));
			}
			if (!jsonObject.isNull(KEY_BUILDING_NAME)) {
				setName(jsonObject.optString(KEY_BUILDING_NAME));
			}
			if (!jsonObject.isNull(KEY_BUILDING_ADDRESS)) {
				setAddress(jsonObject.optString(KEY_BUILDING_ADDRESS));
			}
			if (!jsonObject.isNull(KEY_BUILDING_LONGITUDE)) {
				setLongitude(jsonObject.optDouble(KEY_BUILDING_LONGITUDE));
			}
			if (!jsonObject.isNull(KEY_BUILDING_LATITUDE)) {
				setLatitude(jsonObject.optDouble(KEY_BUILDING_LATITUDE));
			}

			if (!jsonObject.isNull(KEY_BUILDING_INIT_ANGLE)) {
				setInitAngle(jsonObject.optDouble(KEY_BUILDING_INIT_ANGLE));
			}

			if (!jsonObject.isNull(KEY_BUILDING_ROUTE_URL)) {
				setRouteURL(jsonObject.optString(KEY_BUILDING_ROUTE_URL));
			}

			TYMapSize offset = new TYMapSize(0, 0);

			if (!jsonObject.isNull(KEY_BUILDING_OFFSET_X)) {
				double x = jsonObject.optDouble(KEY_BUILDING_OFFSET_X);
				offset.x = x;
			}

			if (!jsonObject.isNull(KEY_BUILDING_OFFSET_Y)) {
				double y = jsonObject.optDouble(KEY_BUILDING_OFFSET_Y);
				offset.y = y;
			}
			setOffset(offset);
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_BUILDING_CITY_ID, cityID);
			jsonObject.put(KEY_BUILDING_ID, buildingID);
			jsonObject.put(KEY_BUILDING_NAME, name);
			jsonObject.put(KEY_BUILDING_ADDRESS, address);
			jsonObject.put(KEY_BUILDING_LONGITUDE, longitude);
			jsonObject.put(KEY_BUILDING_LATITUDE, latitude);

			jsonObject.put(KEY_BUILDING_INIT_ANGLE, initAngle);
			jsonObject.put(KEY_BUILDING_ROUTE_URL, routeURL);
			jsonObject.put(KEY_BUILDING_OFFSET_X, offset.x);
			jsonObject.put(KEY_BUILDING_OFFSET_Y, offset.y);

			jsonObject.put(KEY_BUILDING_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	protected void setCityID(String cityID) {
		this.cityID = cityID;
	}

	protected void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setAddress(String address) {
		this.address = address;
	}

	protected void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	protected void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	protected void setInitAngle(double initAngle) {
		this.initAngle = initAngle;
	}

	protected void setRouteURL(String routeURL) {
		this.routeURL = routeURL;
	}

	protected void setOffset(TYMapSize offset) {
		this.offset = offset;
	}

	protected void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 建筑所在城市ID
	 */
	public String getCityID() {
		return cityID;
	}

	/**
	 * 建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 建筑名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 建筑地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 建筑经度
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 建筑纬度
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 地图初始偏转角度
	 */
	public double getInitAngle() {
		return initAngle;
	}

	/**
	 * 导航服务地址
	 */
	public String getRouteURL() {
		return routeURL;
	}

	/**
	 * 导航偏移量
	 */
	public TYMapSize getOffset() {
		return offset;
	}

	/**
	 * 建筑状态
	 */
	public int getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "MarektID = " + buildingID + ", MarketName = " + name;
	}

	/**
	 * 从外部存储目录解析所有建筑信息列表
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * 
	 * @return 建筑类数组
	 */
	public static List<TYBuilding> parseBuildingFromFiles(Context context,
			String path, String cityID) {

		List<TYBuilding> buildings = new ArrayList<TYBuilding>();

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
					&& !jsonObject.isNull(TYBuilding.JSON_KEY_BUILDINGS)) {
				JSONArray array = jsonObject
						.getJSONArray(TYBuilding.JSON_KEY_BUILDINGS);
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
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingID
	 *            建筑ID
	 * @return 建筑类
	 */
	public static TYBuilding parseBuildingFromFilesById(Context context,
			String path, String cityID, String buildingID) {
		List<TYBuilding> buildings = parseBuildingFromFiles(context, path,
				cityID);

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
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingName
	 *            建筑名称
	 * 
	 * @return 建筑类
	 */
	public static TYBuilding parseBuildingFromFilesByName(Context context,
			String path, String cityID, String buildingName) {
		List<TYBuilding> buildings = parseBuildingFromFiles(context, path,
				cityID);

		for (int i = 0; i < buildings.size(); i++) {
			TYBuilding building = buildings.get(i);
			if (building.getName().equals(buildingName)) {
				return building;
			}
		}
		return null;
	}

}