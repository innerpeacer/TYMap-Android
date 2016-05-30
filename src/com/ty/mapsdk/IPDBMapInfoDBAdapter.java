package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

class IPDBMapInfoDBAdapter {
	static final String TABLE_MAPINFO = "MAPINFO";

	static final String FIELD_MAPINFO_CITY_ID = "CITY_ID";
	static final String FIELD_MAPINFO_BUILDING_ID = "BUILDING_ID";
	static final String FIELD_MAPINFO_MAP_ID = "MAP_ID";
	static final String FIELD_MAPINFO_FLOOR_NAME = "FLOOR_NAME";
	static final String FIELD_MAPINFO_FLOOR_NUMBER = "FLOOR_NUMBER";
	static final String FIELD_MAPINFO_SIZE_X = "SIZE_X";
	static final String FIELD_MAPINFO_SIZE_Y = "SIZE_Y";
	static final String FIELD_MAPINFO_XMIN = "XMIN";
	static final String FIELD_MAPINFO_YMIN = "YMIN";
	static final String FIELD_MAPINFO_XMAX = "XMAX";
	static final String FIELD_MAPINFO_YMAX = "YMAX";

	Context context;
	private SQLiteDatabase db;
	private String dbPath;

	public IPDBMapInfoDBAdapter(String path) {
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

	List<TYMapInfo> getAllMapInfo() {
		List<TYMapInfo> mapInfoArray = new ArrayList<TYMapInfo>();

		String[] columns = new String[] { FIELD_MAPINFO_CITY_ID,
				FIELD_MAPINFO_BUILDING_ID, FIELD_MAPINFO_MAP_ID,
				FIELD_MAPINFO_FLOOR_NAME, FIELD_MAPINFO_FLOOR_NUMBER,
				FIELD_MAPINFO_SIZE_X, FIELD_MAPINFO_SIZE_Y, FIELD_MAPINFO_XMIN,
				FIELD_MAPINFO_YMIN, FIELD_MAPINFO_XMAX, FIELD_MAPINFO_YMAX };
		Cursor c = db.query(true, TABLE_MAPINFO, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				TYMapInfo info = new TYMapInfo();

				info.cityID = c.getString(0);
				info.buildingID = c.getString(1);
				info.mapID = c.getString(2);
				info.floorName = c.getString(3);

				info.floorNumber = c.getInt(4);

				info.size_x = c.getDouble(5);
				info.size_y = c.getDouble(6);
				info.xmin = c.getDouble(7);
				info.ymin = c.getDouble(8);
				info.xmax = c.getDouble(9);
				info.ymax = c.getDouble(10);

				mapInfoArray.add(info);
			} while (c.moveToNext());
		}
		return mapInfoArray;
	}

	TYMapInfo getMapInfoWithName(String floorName) {
		TYMapInfo info = null;
		String[] columns = new String[] { FIELD_MAPINFO_CITY_ID,
				FIELD_MAPINFO_BUILDING_ID, FIELD_MAPINFO_MAP_ID,
				FIELD_MAPINFO_FLOOR_NAME, FIELD_MAPINFO_FLOOR_NUMBER,
				FIELD_MAPINFO_SIZE_X, FIELD_MAPINFO_SIZE_Y, FIELD_MAPINFO_XMIN,
				FIELD_MAPINFO_YMIN, FIELD_MAPINFO_XMAX, FIELD_MAPINFO_YMAX };
		String selection = String.format(" %s = ? ", FIELD_MAPINFO_FLOOR_NAME);
		String[] selectionArgs = new String[] { floorName };

		Cursor c = db.query(true, TABLE_MAPINFO, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			info = new TYMapInfo();

			info.cityID = c.getString(0);
			info.buildingID = c.getString(1);
			info.mapID = c.getString(2);
			info.floorName = c.getString(3);

			info.floorNumber = c.getInt(4);

			info.size_x = c.getDouble(5);
			info.size_y = c.getDouble(6);
			info.xmin = c.getDouble(7);
			info.ymin = c.getDouble(8);
			info.xmax = c.getDouble(9);
			info.ymax = c.getDouble(10);
		}
		return info;
	}

	TYMapInfo getMapInfoWithMapID(String mapID) {
		TYMapInfo info = null;
		String[] columns = new String[] { FIELD_MAPINFO_CITY_ID,
				FIELD_MAPINFO_BUILDING_ID, FIELD_MAPINFO_MAP_ID,
				FIELD_MAPINFO_FLOOR_NAME, FIELD_MAPINFO_FLOOR_NUMBER,
				FIELD_MAPINFO_SIZE_X, FIELD_MAPINFO_SIZE_Y, FIELD_MAPINFO_XMIN,
				FIELD_MAPINFO_YMIN, FIELD_MAPINFO_XMAX, FIELD_MAPINFO_YMAX };
		String selection = String.format(" %s = ? ", FIELD_MAPINFO_MAP_ID);
		String[] selectionArgs = new String[] { mapID };

		Cursor c = db.query(true, TABLE_MAPINFO, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			info = new TYMapInfo();

			info.cityID = c.getString(0);
			info.buildingID = c.getString(1);
			info.mapID = c.getString(2);
			info.floorName = c.getString(3);

			info.floorNumber = c.getInt(4);

			info.size_x = c.getDouble(5);
			info.size_y = c.getDouble(6);
			info.xmin = c.getDouble(7);
			info.ymin = c.getDouble(8);
			info.xmax = c.getDouble(9);
			info.ymax = c.getDouble(10);
		}
		return info;
	}

	TYMapInfo getMapInfoWithFloor(int floor) {
		TYMapInfo info = null;
		String[] columns = new String[] { FIELD_MAPINFO_CITY_ID,
				FIELD_MAPINFO_BUILDING_ID, FIELD_MAPINFO_MAP_ID,
				FIELD_MAPINFO_FLOOR_NAME, FIELD_MAPINFO_FLOOR_NUMBER,
				FIELD_MAPINFO_SIZE_X, FIELD_MAPINFO_SIZE_Y, FIELD_MAPINFO_XMIN,
				FIELD_MAPINFO_YMIN, FIELD_MAPINFO_XMAX, FIELD_MAPINFO_YMAX };
		String selection = String
				.format(" %s = ? ", FIELD_MAPINFO_FLOOR_NUMBER);
		String[] selectionArgs = new String[] { floor + "" };

		Cursor c = db.query(true, TABLE_MAPINFO, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			info = new TYMapInfo();

			info.cityID = c.getString(0);
			info.buildingID = c.getString(1);
			info.mapID = c.getString(2);
			info.floorName = c.getString(3);

			info.floorNumber = c.getInt(4);

			info.size_x = c.getDouble(5);
			info.size_y = c.getDouble(6);
			info.xmin = c.getDouble(7);
			info.ymin = c.getDouble(8);
			info.xmax = c.getDouble(9);
			info.ymax = c.getDouble(10);
		}
		return info;
	}
}
