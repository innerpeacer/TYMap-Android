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

	// private static final String JSON_FIELD_ROOT_RENDERING_SCHEME =
	// "RenderingScheme";
	//
	// private static final String JSON_FIELD_FIRST_DEFAULT_SYMBOL =
	// "DefaultSymbol";
	// private static final String JSON_FIELD_FIRST_FILL_SYMBOL = "FillSymbol";
	// private static final String JSON_FIELD_FIRST_ICON_SYMBOL = "IconSymbol";
	//
	// private static final String JSON_FIELD_SECOND_DEFAULT_FILL_SYMBOL =
	// "DefaultFillSymbol";
	// private static final String JSON_FIELD_SECOND_DEFAULT_HIGHLIGHT_SYMBOL =
	// "DefaultHighlightFillSymbol";
	// private static final String JSON_FIELD_SECOND_DEFAULT_LINE_SYMBOL =
	// "DefaultLineSymbol";
	// private static final String
	// JSON_FIELD_SECOND_DEFAULT_HIGHLIGHT_LINE_SYMBOL =
	// "DefaultHighlightLineSymbol";

	// private static final String JSON_FIELD_LEAVE_COLOR_ID = "colorID";
	// private static final String JSON_FIELD_LEAVE_FILL_COLOR = "fillColor";
	// private static final String JSON_FIELD_LEAVE_OUTLINE_COLOR =
	// "outlineColor";
	// private static final String JSON_FIELD_LEAVE_LINE_WIDTH = "lineWidth";
	// private static final String JSON_FIELD_LEAVE_ICON = "icon";

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
		this(context, IPMapFileManager.getMapDataDBPath(building));
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

		IPSymbolDBAdapter db = new IPSymbolDBAdapter(path);
		db.open();
		Map<Integer, SimpleFillSymbol> fillDict = db.getFillSymbolDictionary();
		Map<Integer, String> iconDict = db.getIconSymbolDictionary();

		defaultFillSymbol = fillDict.get(9999);
		defaultHighlightFillSymbol = fillDict.get(9998);
		fillSymbolDictionary.putAll(fillDict);
		iconSymbolDictionary.putAll(iconDict);
		db.close();
	}

	// private void parseRenderingSchemeFileFromFile(String path) {
	// // Log.i(TAG, "parseRenderingSchemeFileFromFile");
	// try {
	// FileInputStream inStream = new FileInputStream(new File(path));
	// InputStreamReader inputReader = new InputStreamReader(inStream);
	// BufferedReader bufReader = new BufferedReader(inputReader);
	//
	// String line = "";
	// StringBuffer jsonStr = new StringBuffer();
	// while ((line = bufReader.readLine()) != null)
	// jsonStr.append(line);
	//
	// JSONObject jsonObject = new JSONObject(jsonStr.toString());
	//
	// if (jsonObject != null
	// && !jsonObject.isNull(JSON_FIELD_ROOT_RENDERING_SCHEME)) {
	// // Log.i(TAG, JSON_FIELD_ROOT_RENDERING_SCHEME);
	// JSONObject rootDict = jsonObject
	// .getJSONObject(JSON_FIELD_ROOT_RENDERING_SCHEME);
	//
	// JSONObject firstDefaultSymbolDict = rootDict
	// .getJSONObject(JSON_FIELD_FIRST_DEFAULT_SYMBOL);
	// JSONArray firstFillSymbolArray = rootDict
	// .getJSONArray(JSON_FIELD_FIRST_FILL_SYMBOL);
	// JSONArray firstIconSymbolArray = rootDict
	// .getJSONArray(JSON_FIELD_FIRST_ICON_SYMBOL);
	//
	// {
	// JSONObject defaultFillSymbolDict = firstDefaultSymbolDict
	// .getJSONObject(JSON_FIELD_SECOND_DEFAULT_FILL_SYMBOL);
	// int fillColor = Color.parseColor(defaultFillSymbolDict
	// .getString(JSON_FIELD_LEAVE_FILL_COLOR));
	// int outlineColor = Color.parseColor(defaultFillSymbolDict
	// .getString(JSON_FIELD_LEAVE_OUTLINE_COLOR));
	// float lineWidth = (float) defaultFillSymbolDict
	// .getDouble(JSON_FIELD_LEAVE_LINE_WIDTH);
	// defaultFillSymbol = new SimpleFillSymbol(fillColor);
	// defaultFillSymbol.setOutline(new SimpleLineSymbol(
	// outlineColor, lineWidth));
	// }
	//
	// {
	// JSONObject defaultHighlightFillSymbolDict = firstDefaultSymbolDict
	// .getJSONObject(JSON_FIELD_SECOND_DEFAULT_HIGHLIGHT_SYMBOL);
	// int fillColor = Color
	// .parseColor(defaultHighlightFillSymbolDict
	// .getString(JSON_FIELD_LEAVE_FILL_COLOR));
	// int outlineColor = Color
	// .parseColor(defaultHighlightFillSymbolDict
	// .getString(JSON_FIELD_LEAVE_OUTLINE_COLOR));
	// float lineWidth = (float) defaultHighlightFillSymbolDict
	// .getDouble(JSON_FIELD_LEAVE_LINE_WIDTH);
	// defaultHighlightFillSymbol = new SimpleFillSymbol(fillColor);
	// defaultHighlightFillSymbol.setOutline(new SimpleLineSymbol(
	// outlineColor, lineWidth));
	// }
	//
	// {
	// fillSymbolDictionary.clear();
	// for (int i = 0; i < firstFillSymbolArray.length(); i++) {
	// JSONObject secondDict = firstFillSymbolArray
	// .getJSONObject(i);
	// int colorID = secondDict
	// .getInt(JSON_FIELD_LEAVE_COLOR_ID);
	// int fillColor = Color.parseColor(secondDict
	// .getString(JSON_FIELD_LEAVE_FILL_COLOR));
	// int outlineColor = Color.parseColor(secondDict
	// .getString(JSON_FIELD_LEAVE_OUTLINE_COLOR));
	// float lineWidth = (float) secondDict
	// .getDouble(JSON_FIELD_LEAVE_LINE_WIDTH);
	//
	// SimpleFillSymbol sfs = new SimpleFillSymbol(fillColor);
	// sfs.setOutline(new SimpleLineSymbol(outlineColor,
	// lineWidth));
	// fillSymbolDictionary.put(colorID, sfs);
	// }
	// }
	//
	// {
	// iconSymbolDictionary.clear();
	// for (int i = 0; i < firstIconSymbolArray.length(); i++) {
	// JSONObject secondDict = firstIconSymbolArray
	// .getJSONObject(i);
	// int colorID = secondDict
	// .getInt(JSON_FIELD_LEAVE_COLOR_ID);
	// String icon = secondDict
	// .getString(JSON_FIELD_LEAVE_ICON);
	// iconSymbolDictionary.put(colorID, icon);
	//
	// }
	// }
	// }
	//
	// inputReader.close();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }

}
