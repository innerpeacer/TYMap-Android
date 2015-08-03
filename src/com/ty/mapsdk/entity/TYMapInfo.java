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
 * 地图信息类：用于表示某一层地图的配置信息，包含地图ID、地图尺寸、地图范围、地图偏转角等
 */
public class TYMapInfo implements Parcelable {
	public static final String JSON_KEY_MAPINFOS = "MapInfo";

	private static final String KEY_MAPINFO_CITYID = "cityID";
	private static final String KEY_MAPINFO_BUILDINGID = "buildingID";
	private static final String KEY_MAP_ID = "mapID";
	private static final String KEY_FLOOR_NAME = "floorName";
	private static final String KEY_FLOOR_NUMBER = "floorIndex";

	private static final String KEY_SIZE_X = "size_x";
	private static final String KEY_SIZE_Y = "size_y";

	private static final String KEY_X_MIN = "xmin";
	private static final String KEY_X_MAX = "xmax";
	private static final String KEY_Y_MIN = "ymin";
	private static final String KEY_Y_MAX = "ymax";

	private String cityID;
	private String buildingID;
	private String mapID;

	private String floorName;
	private int floorNumber;

	private double size_x;
	private double size_y;

	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;

	public TYMapInfo() {
		super();
	}

	TYMapInfo(Parcel in) {
		cityID = in.readString();
		buildingID = in.readString();
		mapID = in.readString();
		floorName = in.readString();

		floorNumber = in.readInt();

		size_x = in.readDouble();
		size_y = in.readDouble();

		xmin = in.readDouble();
		xmax = in.readDouble();
		ymin = in.readDouble();
		ymax = in.readDouble();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cityID);
		dest.writeString(buildingID);
		dest.writeString(mapID);
		dest.writeString(floorName);

		dest.writeInt(floorNumber);

		dest.writeDouble(size_x);
		dest.writeDouble(size_y);

		dest.writeDouble(xmin);
		dest.writeDouble(xmax);
		dest.writeDouble(ymin);
		dest.writeDouble(ymax);

	}

	public static final Parcelable.Creator<TYMapInfo> CREATOR = new Creator<TYMapInfo>() {
		public TYMapInfo[] newArray(int size) {
			return new TYMapInfo[size];
		};

		public TYMapInfo createFromParcel(Parcel source) {
			return new TYMapInfo(source);
		};
	};

	public int describeContents() {
		return 0;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_MAPINFO_CITYID)) {
				setCityID(jsonObject.optString(KEY_MAPINFO_CITYID));
			}

			if (!jsonObject.isNull(KEY_MAPINFO_BUILDINGID)) {
				setBuildingID(jsonObject.optString(KEY_MAPINFO_BUILDINGID));
			}
			if (!jsonObject.isNull(KEY_MAP_ID)) {
				setMapID(jsonObject.optString(KEY_MAP_ID));
			}
			if (!jsonObject.isNull(KEY_FLOOR_NAME)) {
				setFloorName(jsonObject.optString(KEY_FLOOR_NAME));
			}
			if (!jsonObject.isNull(KEY_FLOOR_NUMBER)) {
				setFloorNumber(jsonObject.optInt(KEY_FLOOR_NUMBER));
			}
			if (!jsonObject.isNull(KEY_SIZE_X)) {
				setSize_x(jsonObject.optDouble(KEY_SIZE_X));
			}
			if (!jsonObject.isNull(KEY_SIZE_Y)) {
				setSize_y(jsonObject.optDouble(KEY_SIZE_Y));
			}
			if (!jsonObject.isNull(KEY_X_MIN)) {
				setXmin(jsonObject.optDouble(KEY_X_MIN));
			}
			if (!jsonObject.isNull(KEY_X_MAX)) {
				setXmax(jsonObject.optDouble(KEY_X_MAX));
			}
			if (!jsonObject.isNull(KEY_Y_MIN)) {
				setYmin(jsonObject.optDouble(KEY_Y_MIN));
			}
			if (!jsonObject.isNull(KEY_Y_MAX)) {
				setYmax(jsonObject.optDouble(KEY_Y_MAX));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_MAPINFO_CITYID, cityID);
			jsonObject.put(KEY_MAPINFO_BUILDINGID, buildingID);
			jsonObject.put(KEY_MAP_ID, mapID);
			jsonObject.put(KEY_FLOOR_NAME, floorName);
			jsonObject.put(KEY_FLOOR_NUMBER, floorNumber);
			jsonObject.put(KEY_SIZE_X, size_x);
			jsonObject.put(KEY_SIZE_Y, size_y);
			jsonObject.put(KEY_X_MIN, xmin);
			jsonObject.put(KEY_X_MAX, xmax);
			jsonObject.put(KEY_Y_MIN, ymin);
			jsonObject.put(KEY_Y_MAX, ymax);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 所在城市的ID
	 */
	public String getCityID() {
		return cityID;
	}

	/**
	 * 所在建筑的ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 当前地图的唯一ID，当前与楼层的FloorID一致
	 */
	public String getMapID() {
		return mapID;
	}

	/**
	 * 地图尺寸
	 */
	public TYMapSize getMapSize() {
		return new TYMapSize(size_x, size_y);
	}

	/**
	 * 地图范围
	 */
	public MapExtent getMapExtent() {
		return new MapExtent(xmin, ymin, xmax, ymax);
	}

	/**
	 * 当前楼层名称，如F1、B1
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * 当前楼层序号,如-1、1
	 */
	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * 地图X方向放缩比例，当前比例为1
	 */
	public double getScaleX() {
		return size_x / (xmax - xmin);
	}

	/**
	 * 地图Y方向放缩比例，当前比例为1
	 */
	public double getScaleY() {
		return size_y / (ymax - ymin);
	}

	protected void setCityID(String cityID) {
		this.cityID = cityID;
	}

	protected void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	protected void setMapID(String mapID) {
		this.mapID = mapID;
	}

	protected void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	protected void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	protected void setSize_x(double size_x) {
		this.size_x = size_x;
	}

	protected void setSize_y(double size_y) {
		this.size_y = size_y;
	}

	protected void setXmin(double xmin) {
		this.xmin = xmin;
	}

	protected void setXmax(double xmax) {
		this.xmax = xmax;
	}

	protected void setYmin(double ymin) {
		this.ymin = ymin;
	}

	protected void setYmax(double ymax) {
		this.ymax = ymax;
	}

	@Override
	public String toString() {
		String str = "MapID: %s, Floor:%d ,Size:(%.2f, %.2f) Extent: (%.4f, %.4f, %.4f, %.4f)";
		return String.format(str, mapID, floorNumber, size_x, size_y, xmin,
				ymin, xmax, ymax);
	}

	/**
	 * 地图坐标范围:{xmin, ymin, xmax, ymax}
	 */
	public class MapExtent {
		double xmin;
		double ymin;
		double xmax;
		double ymax;

		public MapExtent(double xmin, double ymin, double xmax, double ymax) {
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
		}

		public double getXmin() {
			return xmin;
		}

		public double getYmin() {
			return ymin;
		}

		public double getXmax() {
			return xmax;
		}

		public double getYmax() {
			return ymax;
		}
	}

	/**
	 * 从外部存储目录解析某建筑所有楼层的地图信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param buildingID
	 *            楼层所在建筑的ID
	 * @return 所有楼层的地图信息数组:[NPMapInfo]
	 */
	public static List<TYMapInfo> parseMapInfoFromFiles(Context context,
			String path, String buildingID) {

		List<TYMapInfo> mapInfos = new ArrayList<TYMapInfo>();

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
					&& !jsonObject.isNull(TYMapInfo.JSON_KEY_MAPINFOS)) {
				JSONArray array = jsonObject
						.getJSONArray(TYMapInfo.JSON_KEY_MAPINFOS);
				for (int i = 0; i < array.length(); i++) {
					TYMapInfo mapInfo = new TYMapInfo();
					mapInfo.parseJson(array.optJSONObject(i));
					mapInfos.add(mapInfo);
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
		return mapInfos;
	}

	/**
	 * 从外部存储目录解析某楼层地图信息的静态方法
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param buildingID
	 *            楼层所在建筑的ID
	 * @param mapID
	 *            楼层地图ID
	 * @return 楼层地图信息
	 */
	public static TYMapInfo parseMapInfoFromFilesById(Context context,
			String path, String buildingID, String mapID) {
		List<TYMapInfo> mapInfos = parseMapInfoFromFiles(context, path,
				buildingID);

		for (int i = 0; i < mapInfos.size(); i++) {
			TYMapInfo mapInfo = mapInfos.get(i);
			if (mapInfo.getMapID().equals(mapID)) {
				return mapInfo;
			}
		}
		return null;
	}

	/**
	 * 从一组地图信息中搜索特定楼层的地图信息
	 * 
	 * @param array
	 *            目标地图信息数组
	 * @param floor
	 *            目标楼层
	 * 
	 * @return 目标楼层信息
	 */
	public static TYMapInfo searchMapInfoFromArray(List<TYMapInfo> array,
			int floor) {
		for (TYMapInfo info : array) {
			if (floor == info.getFloorNumber()) {
				return info;
			}
		}
		return null;
	}

}
