package com.ty.mapsdk;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.ty.mapdata.TYBuilding;

/**
 * 渲染方案类：用于表示地图的渲染规则
 */
public class TYRenderingScheme {
	static final String TAG = TYRenderingScheme.class.getSimpleName();

	Context context;

	/**
	 * 默认填充符号
	 */
	private SimpleFillSymbol defaultFillSymbol;

	/**
	 * 默认高亮填充符号
	 */
	private SimpleFillSymbol defaultHighlightFillSymbol;

	/**
	 * 默认线型符号
	 */
	private SimpleLineSymbol defaultLineSymbol;

	/**
	 * 默认高亮线型符号
	 */
	private SimpleLineSymbol defaultHighlightLineSymbol;

	/**
	 * 填充符号字典，{NSNumber: FillSymbol} -> {类型: 填充符号}
	 */
	private Map<Integer, SimpleFillSymbol> fillSymbolDictionary;

	/**
	 * Icon符号字典，{NSNumber: NSString} -> {类型: Icon文件名}
	 */
	private Map<Integer, String> iconSymbolDictionary;

	/**
	 * 渲染方案构造函数
	 * 
	 * @param context
	 *            上下文环境
	 * @param path
	 *            渲染方案文件路径
	 */
	@SuppressLint("UseSparseArrays")
	TYRenderingScheme(Context context, String path) {
		this.context = context;
		fillSymbolDictionary = new HashMap<Integer, SimpleFillSymbol>();
		iconSymbolDictionary = new HashMap<Integer, String>();

		// parseRenderingSchemeFileFromFile(path);
		parseRenderingSchemeFromDBFile(path);
	}

	/**
	 * 渲染方案构造函数
	 * 
	 * @param context
	 *            上下文环境
	 * @param building
	 *            目标建筑
	 */
	public TYRenderingScheme(Context context, TYBuilding building) {
		// this(context, IPMapFileManager.getRenderingScheme(building));
		this(context, IPHPMapFileManager.getMapDataDBPath(building));
	}

	/**
	 * 获取默认填充符号
	 */
	public SimpleFillSymbol getDefaultFillSymbol() {
		return defaultFillSymbol;
	}

	/**
	 * 获取默认高亮填充符号
	 */
	public SimpleFillSymbol getDefaultHighlightFillSymbol() {
		return defaultHighlightFillSymbol;
	}

	/**
	 * 获取默认线型符号
	 */
	public SimpleLineSymbol getDefaultLineSymbol() {
		return defaultLineSymbol;
	}

	/**
	 * 获取默认高亮线型符号
	 */
	public SimpleLineSymbol getDefaultHighlightLineSymbol() {
		return defaultHighlightLineSymbol;
	}

	/**
	 * 获取填充符号字典，{Integer: FillSymbol} -> {类型: 填充符号}
	 */
	public Map<Integer, SimpleFillSymbol> getFillSymbolDictionary() {
		return fillSymbolDictionary;
	}

	/**
	 * 获取Icon符号字典，{Integer: String} -> {类型: Icon文件名}
	 */
	public Map<Integer, String> getIconSymbolDictionary() {
		return iconSymbolDictionary;
	}

	private void parseRenderingSchemeFromDBFile(String path) {
		Log.i(TAG, "parseRenderingSchemeFromDBFile");

		IPDBSymbolDBAdapter db = new IPDBSymbolDBAdapter(path);
		db.open();
		Map<Integer, SimpleFillSymbol> fillDict = db.getFillSymbolDictionary();
		Map<Integer, String> iconDict = db.getIconSymbolDictionary();

		defaultFillSymbol = fillDict.get(9999);
		defaultHighlightFillSymbol = fillDict.get(9998);
		fillSymbolDictionary.putAll(fillDict);
		iconSymbolDictionary.putAll(iconDict);
		db.close();
	}
}
