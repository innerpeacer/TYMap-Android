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

public class NPCity implements Parcelable {

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

	public NPCity() {
		super();
	}

	NPCity(Parcel in) {
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
		if (obj != null && obj instanceof NPCity) {
			if (this.cityID.equals(((NPCity) obj).getCityID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<NPCity> CREATOR = new Creator<NPCity>() {
		@Override
		public NPCity[] newArray(int size) {
			return new NPCity[size];
		}

		@Override
		public NPCity createFromParcel(Parcel source) {
			return new NPCity(source);
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

	public String getCityID() {
		return cityID;
	}

	public String getName() {
		return name;
	}

	public String getSname() {
		return sname;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

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

	public static List<NPCity> parseCityFromFiles(Context context, String path) {
		List<NPCity> cities = new ArrayList<NPCity>();

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
					&& !jsonObject.isNull(NPCity.JSON_KEY_CITIES)) {
				JSONArray array = jsonObject
						.getJSONArray(NPCity.JSON_KEY_CITIES);
				for (int i = 0; i < array.length(); i++) {
					NPCity city = new NPCity();
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

	public static List<NPCity> parseCityFromAssets(Context context, String path) {
		List<NPCity> cities = new ArrayList<NPCity>();

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
					&& !jsonObject.isNull(NPCity.JSON_KEY_CITIES)) {
				JSONArray array = jsonObject
						.getJSONArray(NPCity.JSON_KEY_CITIES);
				for (int i = 0; i < array.length(); i++) {
					NPCity city = new NPCity();
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

	public static NPCity parseCityFromFilesById(Context context, String path,
			String cityId) {
		List<NPCity> cities = parseCityFromFiles(context, path);

		for (int i = 0; i < cities.size(); i++) {
			NPCity city = cities.get(i);
			if (city.getCityID().equals(cityId)) {
				return city;
			}
		}
		return null;
	}

	public static NPCity parseCityFromAssetsById(Context context, String path,
			String cityId) {
		List<NPCity> cities = parseCityFromAssets(context, path);

		for (int i = 0; i < cities.size(); i++) {
			NPCity city = cities.get(i);
			if (city.getCityID().equals(cityId)) {
				return city;
			}
		}
		return null;
	}

	public static NPCity parseCityFromFilesByName(Context context, String path,
			String cityName) {
		List<NPCity> cities = parseCityFromFiles(context, path);

		for (int i = 0; i < cities.size(); i++) {
			NPCity city = cities.get(i);
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}

	public static NPCity parseCityFromAssetsByName(Context context,
			String path, String cityName) {
		List<NPCity> cities = parseCityFromAssets(context, path);

		for (int i = 0; i < cities.size(); i++) {
			NPCity city = cities.get(i);
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}

}
