package cn.nephogram.mapsdk.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class NPMapInfo implements Parcelable {
	public static final String JSON_KEY_MAPINFOS = "MapInfo";

	private static final String KEY_MAP_ID = "mapID";
	private static final String KEY_FLOOR_NAME = "floorName";
	private static final String KEY_FLOOR_NUMBER = "floorIndex";

	private static final String KEY_SIZE_X = "size_x";
	private static final String KEY_SIZE_Y = "size_y";

	private static final String KEY_X_MIN = "xmin";
	private static final String KEY_X_MAX = "xmax";
	private static final String KEY_Y_MIN = "ymin";
	private static final String KEY_Y_MAX = "ymax";

	private static final String KEY_INIT_ANGLE = "initAngle";

	private String mapID;

	private String floorName;
	private int floorNumber;

	private double size_x;
	private double size_y;

	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;

	private double initAngle;

	public NPMapInfo() {
		super();
	}

	NPMapInfo(Parcel in) {
		mapID = in.readString();
		floorName = in.readString();

		floorNumber = in.readInt();

		size_x = in.readDouble();
		size_y = in.readDouble();

		xmin = in.readDouble();
		xmax = in.readDouble();
		ymin = in.readDouble();
		ymax = in.readDouble();

		initAngle = in.readDouble();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mapID);
		dest.writeString(floorName);

		dest.writeInt(floorNumber);

		dest.writeDouble(size_x);
		dest.writeDouble(size_y);

		dest.writeDouble(xmin);
		dest.writeDouble(xmax);
		dest.writeDouble(ymin);
		dest.writeDouble(ymax);

		dest.writeDouble(initAngle);
	}

	public static final Parcelable.Creator<NPMapInfo> CREATOR = new Creator<NPMapInfo>() {
		public NPMapInfo[] newArray(int size) {
			return new NPMapInfo[size];
		};

		public NPMapInfo createFromParcel(Parcel source) {
			return new NPMapInfo(source);
		};
	};

	public int describeContents() {
		return 0;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
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
			if (!jsonObject.isNull(KEY_INIT_ANGLE)) {
				setInitAngle(jsonObject.optDouble(KEY_INIT_ANGLE));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_MAP_ID, mapID);
			jsonObject.put(KEY_FLOOR_NAME, floorName);
			jsonObject.put(KEY_FLOOR_NUMBER, floorNumber);
			jsonObject.put(KEY_SIZE_X, size_x);
			jsonObject.put(KEY_SIZE_Y, size_y);
			jsonObject.put(KEY_X_MIN, xmin);
			jsonObject.put(KEY_X_MAX, xmax);
			jsonObject.put(KEY_Y_MIN, ymin);
			jsonObject.put(KEY_Y_MAX, ymax);
			jsonObject.put(KEY_INIT_ANGLE, initAngle);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String getMapID() {
		return mapID;
	}

	public MapSize getMapSize() {
		return new MapSize(size_x, size_y);
	}

	public MapExtent getMapExtent() {
		return new MapExtent(xmin, ymin, xmax, ymax);
	}

	public String getFloorName() {
		return floorName;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public double getScaleX() {
		return size_x / (xmax - xmin);
	}

	public double getScaleY() {
		return size_y / (ymax - ymin);
	}

	public double getInitAngle() {
		return initAngle;
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

	protected void setInitAngle(double initAngle) {
		this.initAngle = initAngle;
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

	public class MapSize {
		double x;
		double y;

		public MapSize(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

	}

	public static List<NPMapInfo> parseMapInfoFromFiles(Context context,
			String path, String marketID) {

		List<NPMapInfo> mapInfos = new ArrayList<NPMapInfo>();

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
					&& !jsonObject.isNull(NPMapInfo.JSON_KEY_MAPINFOS)) {
				JSONArray array = jsonObject
						.getJSONArray(NPMapInfo.JSON_KEY_MAPINFOS);
				for (int i = 0; i < array.length(); i++) {
					NPMapInfo mapInfo = new NPMapInfo();
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

	public static List<NPMapInfo> parseMapInfoFromAssets(Context context,
			String path, String marketID) {

		List<NPMapInfo> mapInfos = new ArrayList<NPMapInfo>();

		try {
			InputStream inStream = context.getAssets().open(path);
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(NPMapInfo.JSON_KEY_MAPINFOS)) {
				JSONArray array = jsonObject
						.getJSONArray(NPMapInfo.JSON_KEY_MAPINFOS);
				for (int i = 0; i < array.length(); i++) {
					NPMapInfo mapInfo = new NPMapInfo();
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

	public static NPMapInfo parseMapInfoFromFilesById(Context context,
			String path, String marketID, String mapID) {
		List<NPMapInfo> mapInfos = parseMapInfoFromFiles(context, path,
				marketID);

		for (int i = 0; i < mapInfos.size(); i++) {
			NPMapInfo mapInfo = mapInfos.get(i);
			if (mapInfo.getMapID().equals(mapID)) {
				return mapInfo;
			}
		}
		return null;
	}

	public static NPMapInfo parseMapInfoFromAssetsById(Context context,
			String path, String marketID, String mapID) {
		List<NPMapInfo> mapInfos = parseMapInfoFromAssets(context, path,
				marketID);

		for (int i = 0; i < mapInfos.size(); i++) {
			NPMapInfo mapInfo = mapInfos.get(i);
			if (mapInfo.getMapID().equals(mapID)) {
				return mapInfo;
			}
		}
		return null;
	}

}
