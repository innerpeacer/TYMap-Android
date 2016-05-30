package com.ty.mapsdk;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;

public class IPSymbolDBAdapter {
	static final String TAG = IPSymbolDBAdapter.class.getSimpleName();

	static final String TABLE_FILL_SYMBOL = "FILL_SYMBOL";
	static final String TABLE_ICON_SYMBOL = "ICON_SYMBOL";

	static final String FIELD_SYMBOL_FILL_0_PRIMARY_KEY = "_id";
	static final String FIELD_SYMBOL_FILL_1_SYMBOL_ID = "SYMBOL_ID";
	static final String FIELD_SYMBOL_FILL_2_FILL_COLOR = "FILL";
	static final String FIELD_SYMBOL_FILL_3_OUTLINE_COLOR = "OUTLINE";
	static final String FIELD_SYMBOL_FILL_4_LINE_WIDTH = "LINE_WIDTH";

	static final String FIELD_SYMBOL_ICON_0_PRIMARY_KEY = "_id";
	static final String FIELD_SYMBOL_ICON_1_SYMBOL_ID = "SYMBOL_ID";
	static final String FIELD_SYMBOL_ICON_2_ICON = "ICON";

	Context context;
	private SQLiteDatabase db;
	private String dbPath;

	public IPSymbolDBAdapter(String path) {
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

	@SuppressLint("UseSparseArrays")
	public Map<Integer, SimpleFillSymbol> getFillSymbolDictionary() {
		Map<Integer, SimpleFillSymbol> fillDictionary = new HashMap<Integer, SimpleFillSymbol>();
		String[] columns = new String[] { FIELD_SYMBOL_FILL_1_SYMBOL_ID,
				FIELD_SYMBOL_FILL_2_FILL_COLOR,
				FIELD_SYMBOL_FILL_3_OUTLINE_COLOR,
				FIELD_SYMBOL_FILL_4_LINE_WIDTH };
		Cursor c = db.query(true, TABLE_FILL_SYMBOL, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				int symbolID = c.getInt(0);
				int fillColor = Color.parseColor(c.getString(1));
				int outlineColor = Color.parseColor(c.getString(2));
				float lineWidth = (float) c.getDouble(3);

				SimpleFillSymbol sfs = new SimpleFillSymbol(fillColor);
				sfs.setOutline(new SimpleLineSymbol(outlineColor, lineWidth));

				fillDictionary.put(symbolID, sfs);
			} while (c.moveToNext());
		}
		return fillDictionary;
	}

	@SuppressLint("UseSparseArrays")
	public Map<Integer, String> getIconSymbolDictionary() {
		Map<Integer, String> iconDictionary = new HashMap<Integer, String>();
		String[] columns = new String[] { FIELD_SYMBOL_ICON_1_SYMBOL_ID,
				FIELD_SYMBOL_ICON_2_ICON };
		Cursor c = db.query(true, TABLE_ICON_SYMBOL, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				int symbolID = c.getInt(0);
				String icon = c.getString(1);
				iconDictionary.put(symbolID, icon);
			} while (c.moveToNext());
		}
		return iconDictionary;
	}
}
