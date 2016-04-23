package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYCity;

class IPMapDBAdapter {
	static final String TAG = IPMapDBAdapter.class.getSimpleName();

	static final String TABLE_CITY = "CITY";
	static final String TABLE_BUILDING = "BUILDING";

	static final String FIELD_CITY_ID = "CITY_ID";
	static final String FIELD_CITY_NAME = "NAME";
	static final String FIELD_CITY_SNAME = "SNAME";
	static final String FIELD_CITY_LONGITUDE = "LONGITUDE";
	static final String FIELD_CITY_LATITUDE = "LATITUDE";
	static final String FIELD_CITY_STATUS = "STATUS";

	static final String FIELD_BUILDING_CITY_ID = "CITY_ID";
	static final String FIELD_BUILDING_ID = "BUILDING_ID";
	static final String FIELD_BUILDING_NAME = "NAME";
	static final String FIELD_BUILDING_LONGITUDE = "LONGITUDE";
	static final String FIELD_BUILDING_LATITUDE = "LATITUDE";
	static final String FIELD_BUILDING_ADDRESS = "ADDRESS";
	static final String FIELD_BUILDING_INIT_ANGLE = "INIT_ANGLE";
	static final String FIELD_BUILDING_ROUTE_URL = "ROUTE_URL";
	static final String FIELD_BUILDING_OFFSET_X = "OFFSET_X";
	static final String FIELD_BUILDING_OFFSET_Y = "OFFSET_Y";
	static final String FIELD_BUILDING_STATUS = "STATUS";

	Context context;
	private SQLiteDatabase db;
	private String dbPath;

	public IPMapDBAdapter(String path) {
		this.dbPath = path;
	}

	public void open() {
		if (db == null || !db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
		}
	}

	public void close() {
		if (db.isOpen()) {
			db.close();
		}
	}

	public List<TYCity> getAllCities() {
		List<TYCity> cityArray = new ArrayList<TYCity>();

		String[] columns = new String[] { FIELD_CITY_ID, FIELD_CITY_NAME,
				FIELD_CITY_SNAME, FIELD_CITY_LONGITUDE, FIELD_CITY_LATITUDE,
				FIELD_CITY_STATUS };
		Cursor c = db.query(true, TABLE_CITY, columns, null, null, null, null,
				null, null, null);

		if (c != null && c.moveToFirst()) {
			do {
				TYCity city = new TYCity();
				city.setCityID(c.getString(0));
				city.setName(c.getString(1));
				city.setSname(c.getString(2));
				city.setLongitude(c.getDouble(3));
				city.setLatitude(c.getDouble(4));
				city.setStatus(c.getInt(5));

				cityArray.add(city);

			} while (c.moveToNext());
		}
		return cityArray;
	}

	public TYCity getCityByID(String cityID) {
		TYCity city = null;

		String[] columns = new String[] { FIELD_CITY_ID, FIELD_CITY_NAME,
				FIELD_CITY_SNAME, FIELD_CITY_LONGITUDE, FIELD_CITY_LATITUDE,
				FIELD_CITY_STATUS };
		String selection = String.format(" %s = ? ", FIELD_CITY_ID);
		String[] selectionArgs = new String[] { cityID };

		Cursor c = db.query(true, TABLE_CITY, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			city = new TYCity();
			city.setCityID(c.getString(0));
			city.setName(c.getString(1));
			city.setSname(c.getString(2));
			city.setLongitude(c.getDouble(3));
			city.setLatitude(c.getDouble(4));
			city.setStatus(c.getInt(5));
		}
		return city;
	}

	public TYCity getCityByName(String name) {
		TYCity city = null;

		String[] columns = new String[] { FIELD_CITY_ID, FIELD_CITY_NAME,
				FIELD_CITY_SNAME, FIELD_CITY_LONGITUDE, FIELD_CITY_LATITUDE,
				FIELD_CITY_STATUS };
		String selection = String.format(" %s = ? ", FIELD_CITY_NAME);
		String[] selectionArgs = new String[] { name };

		Cursor c = db.query(true, TABLE_CITY, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			city = new TYCity();
			city.setCityID(c.getString(0));
			city.setName(c.getString(1));
			city.setSname(c.getString(2));
			city.setLongitude(c.getDouble(3));
			city.setLatitude(c.getDouble(4));
			city.setStatus(c.getInt(5));
		}
		return city;
	}

	public List<TYBuilding> getAllBuildings() {
		List<TYBuilding> buildingArray = new ArrayList<TYBuilding>();
		String[] columns = new String[] { FIELD_BUILDING_CITY_ID,
				FIELD_BUILDING_ID, FIELD_BUILDING_NAME,
				FIELD_BUILDING_LONGITUDE, FIELD_BUILDING_LATITUDE,
				FIELD_BUILDING_ADDRESS, FIELD_BUILDING_INIT_ANGLE,
				FIELD_BUILDING_ROUTE_URL, FIELD_BUILDING_OFFSET_X,
				FIELD_BUILDING_OFFSET_Y, FIELD_BUILDING_STATUS };

		Cursor c = db.query(true, TABLE_BUILDING, columns, null, null, null,
				null, null, null, null);

		if (c != null && c.moveToFirst()) {
			do {
				TYBuilding building = new TYBuilding();

				building.setCityID(c.getString(0));
				building.setBuildingID(c.getString(1));
				building.setName(c.getString(2));
				building.setLongitude(c.getDouble(3));
				building.setLatitude(c.getDouble(4));
				building.setAddress(c.getString(5));
				building.setInitAngle(c.getDouble(6));
				building.setRouteURL(c.getString(7));
				building.setOffset(building.new OffsetSize(c.getDouble(8), c
						.getDouble(9)));
				building.setStatus(10);

				buildingArray.add(building);
			} while (c.moveToNext());
		}

		return buildingArray;
	}

	public List<TYBuilding> getAllBuildings(String cityID) {
		List<TYBuilding> buildingArray = new ArrayList<TYBuilding>();
		String[] columns = new String[] { FIELD_BUILDING_CITY_ID,
				FIELD_BUILDING_ID, FIELD_BUILDING_NAME,
				FIELD_BUILDING_LONGITUDE, FIELD_BUILDING_LATITUDE,
				FIELD_BUILDING_ADDRESS, FIELD_BUILDING_INIT_ANGLE,
				FIELD_BUILDING_ROUTE_URL, FIELD_BUILDING_OFFSET_X,
				FIELD_BUILDING_OFFSET_Y, FIELD_BUILDING_STATUS };

		String selection = String.format(" %s = ? ", FIELD_CITY_ID);
		String[] selectionArgs = new String[] { cityID };

		Cursor c = db.query(true, TABLE_BUILDING, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			do {
				TYBuilding building = new TYBuilding();

				building.setCityID(c.getString(0));
				building.setBuildingID(c.getString(1));
				building.setName(c.getString(2));
				building.setLongitude(c.getDouble(3));
				building.setLatitude(c.getDouble(4));
				building.setAddress(c.getString(5));
				building.setInitAngle(c.getDouble(6));
				building.setRouteURL(c.getString(7));
				building.setOffset(building.new OffsetSize(c.getDouble(8), c
						.getDouble(9)));
				building.setStatus(10);

				buildingArray.add(building);
			} while (c.moveToNext());
		}

		return buildingArray;
	}

	public TYBuilding getBuildingByID(String buildingID) {
		TYBuilding building = null;
		String[] columns = new String[] { FIELD_BUILDING_CITY_ID,
				FIELD_BUILDING_ID, FIELD_BUILDING_NAME,
				FIELD_BUILDING_LONGITUDE, FIELD_BUILDING_LATITUDE,
				FIELD_BUILDING_ADDRESS, FIELD_BUILDING_INIT_ANGLE,
				FIELD_BUILDING_ROUTE_URL, FIELD_BUILDING_OFFSET_X,
				FIELD_BUILDING_OFFSET_Y, FIELD_BUILDING_STATUS };
		String selection = String.format(" %s = ? ", FIELD_BUILDING_ID);
		String[] selectionArgs = new String[] { buildingID };

		Cursor c = db.query(true, TABLE_BUILDING, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			building = new TYBuilding();

			building.setCityID(c.getString(0));
			building.setBuildingID(c.getString(1));
			building.setName(c.getString(2));
			building.setLongitude(c.getDouble(3));
			building.setLatitude(c.getDouble(4));
			building.setAddress(c.getString(5));
			building.setInitAngle(c.getDouble(6));
			building.setRouteURL(c.getString(7));
			building.setOffset(building.new OffsetSize(c.getDouble(8), c
					.getDouble(9)));
			building.setStatus(10);
		}

		return building;
	}

	public TYBuilding getBuildingByID(String cityID, String buildingID) {
		TYBuilding building = null;
		String[] columns = new String[] { FIELD_BUILDING_CITY_ID,
				FIELD_BUILDING_ID, FIELD_BUILDING_NAME,
				FIELD_BUILDING_LONGITUDE, FIELD_BUILDING_LATITUDE,
				FIELD_BUILDING_ADDRESS, FIELD_BUILDING_INIT_ANGLE,
				FIELD_BUILDING_ROUTE_URL, FIELD_BUILDING_OFFSET_X,
				FIELD_BUILDING_OFFSET_Y, FIELD_BUILDING_STATUS };
		String selection = String.format(" %s = ? and %s = ? ", FIELD_CITY_ID,
				FIELD_BUILDING_ID);
		String[] selectionArgs = new String[] { cityID, buildingID };
		Cursor c = db.query(true, TABLE_BUILDING, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			building = new TYBuilding();

			building.setCityID(c.getString(0));
			building.setBuildingID(c.getString(1));
			building.setName(c.getString(2));
			building.setLongitude(c.getDouble(3));
			building.setLatitude(c.getDouble(4));
			building.setAddress(c.getString(5));
			building.setInitAngle(c.getDouble(6));
			building.setRouteURL(c.getString(7));
			building.setOffset(building.new OffsetSize(c.getDouble(8), c
					.getDouble(9)));
			building.setStatus(10);
		}

		return building;
	}

	public TYBuilding getBuildingByName(String name) {
		TYBuilding building = null;
		String[] columns = new String[] { FIELD_BUILDING_CITY_ID,
				FIELD_BUILDING_ID, FIELD_BUILDING_NAME,
				FIELD_BUILDING_LONGITUDE, FIELD_BUILDING_LATITUDE,
				FIELD_BUILDING_ADDRESS, FIELD_BUILDING_INIT_ANGLE,
				FIELD_BUILDING_ROUTE_URL, FIELD_BUILDING_OFFSET_X,
				FIELD_BUILDING_OFFSET_Y, FIELD_BUILDING_STATUS };
		String selection = String.format(" %s = ? ", FIELD_BUILDING_NAME);
		String[] selectionArgs = new String[] { name };

		Cursor c = db.query(true, TABLE_BUILDING, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			building = new TYBuilding();

			building.setCityID(c.getString(0));
			building.setBuildingID(c.getString(1));
			building.setName(c.getString(2));
			building.setLongitude(c.getDouble(3));
			building.setLatitude(c.getDouble(4));
			building.setAddress(c.getString(5));
			building.setInitAngle(c.getDouble(6));
			building.setRouteURL(c.getString(7));
			building.setOffset(building.new OffsetSize(c.getDouble(8), c
					.getDouble(9)));
			building.setStatus(10);
		}
		return building;
	}

}
