package com.ty.mapsdk;

import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.ty.mapdata.TYBuilding;

/**
 * 地图信息类：用于表示某一层地图的配置信息，包含地图ID、地图尺寸、地图范围、地图偏转角等
 */
public class TYMapInfo implements Parcelable {
	static final String TAG = TYMapInfo.class.getSimpleName();
	String cityID;
	String buildingID;
	String mapID;

	String floorName;
	int floorNumber;

	double size_x;
	double size_y;

	double xmin;
	double xmax;
	double ymin;
	double ymax;

	/**
	 * 地图信息类构造函数
	 */
	public TYMapInfo() {
		super();
	}

	/**
	 * 地图信息类构造函数
	 * 
	 * @param in
	 *            包含地图信息的Parcel对象
	 */
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

	@Override
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

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 获取所在城市的ID
	 */
	public String getCityID() {
		return cityID;
	}

	/**
	 * 获取所在建筑的ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 获取当前地图的唯一ID，当前与楼层的FloorID一致
	 */
	public String getMapID() {
		return mapID;
	}

	/**
	 * 获取地图尺寸
	 */
	public TYMapSize getMapSize() {
		return new TYMapSize(size_x, size_y);
	}

	/**
	 * 获取地图范围
	 */
	public MapExtent getMapExtent() {
		return new MapExtent(xmin, ymin, xmax, ymax);
	}

	/**
	 * 获取当前楼层名称，如F1、B1
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * 获取当前楼层序号,如-1、1
	 */
	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 * 获取地图X方向放缩比例，当前比例为1
	 */
	public double getScaleX() {
		return size_x / (xmax - xmin);
	}

	/**
	 * 获取地图Y方向放缩比例，当前比例为1
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
	 * 解析目标建筑的所有楼层地图信息
	 * 
	 * @param building
	 *            目标建筑
	 * @return 目标楼层所有地图信息
	 */
	public static List<TYMapInfo> parseAllMapInfo(TYBuilding building) {
		String dbPath = IPMapFileManager.getMapInfoDBPath(building);
		IPMapInfoDBAdapter db = new IPMapInfoDBAdapter(dbPath);
		db.open();
		List<TYMapInfo> mapInfoArray = db.getAllMapInfo();
		db.close();
		return mapInfoArray;
	}

	/**
	 * 从外部存储目录解析某建筑所有楼层的地图信息
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityID
	 *            城市ID
	 * @param buildingID
	 *            建筑ID
	 * @return 地图信息数组
	 */
	public static List<TYMapInfo> parseMapInfoFromFiles(Context context,
			String cityID, String buildingID) {
		String dbPath = IPMapFileManager.getMapInfoDBPath(cityID, buildingID);
		Log.i(TAG, dbPath);
		IPMapInfoDBAdapter db = new IPMapInfoDBAdapter(dbPath);
		db.open();
		List<TYMapInfo> mapInfoArray = db.getAllMapInfo();
		db.close();

		Log.i(TAG, mapInfoArray.toString());
		return mapInfoArray;
		// return IPMapInfoJsonParser.parseMapInfoFromFiles(context, cityID,
		// buildingID);
	}

	/**
	 * 从外部存储目录解析某楼层地图信息的静态方法
	 * 
	 * @param context
	 *            上下文环境
	 * @param cityID
	 *            楼层所在城市的ID
	 * @param buildingID
	 *            楼层所在建筑的ID
	 * @param mapID
	 *            楼层地图ID
	 * @return 楼层地图信息
	 */
	public static TYMapInfo parseMapInfoFromFilesById(Context context,
			String cityID, String buildingID, String mapID) {
		TYMapInfo info = null;
		String dbPath = IPMapFileManager.getMapInfoDBPath(cityID, buildingID);
		IPMapInfoDBAdapter db = new IPMapInfoDBAdapter(dbPath);
		db.open();
		info = db.getMapInfoWithMapID(mapID);
		db.close();
		return info;
		// return IPMapInfoJsonParser.parseMapInfoFromFilesById(context, cityID,
		// buildingID, mapID);
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
