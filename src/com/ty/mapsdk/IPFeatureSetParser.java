package com.ty.mapsdk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;

class IPFeatureSetParser {
	static final String TAG = IPFeatureSetParser.class.getSimpleName();

	public static Map<String, Graphic[]> parseMapDataString(String string) {
		// Log.i(TAG, string);
		Map<String, Graphic[]> result = new HashMap<String, Graphic[]>();
		try {
			JSONObject object = new JSONObject(string);
			result.put("floor", parsePolygonFeatureSet(object, "floor"));
			result.put("room", parsePolygonFeatureSet(object, "room"));
			result.put("asset", parsePolygonFeatureSet(object, "asset"));
			result.put("facility", parsePointFeatureSet(object, "facility"));
			result.put("label", parsePointFeatureSet(object, "label"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	// public static Map<String, Graphic[]> parseMapDataFile(String file) {
	// Map<String, Graphic[]> result = new HashMap<String, Graphic[]>();
	// try {
	// String jsonString = IPFileUtils.readStringFromFile(file);
	// JSONObject object = new JSONObject(jsonString);
	// result.put("floor", parsePolygonFeatureSet(object, "floor"));
	// result.put("room", parsePolygonFeatureSet(object, "room"));
	// result.put("asset", parsePolygonFeatureSet(object, "asset"));
	// result.put("facility", parsePointFeatureSet(object, "facility"));
	// result.put("label", parsePointFeatureSet(object, "label"));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	@SuppressWarnings("unchecked")
	static Graphic[] parsePolygonFeatureSet(JSONObject object, String name) {
		JSONObject featureSetObject = object.optJSONObject(name);
		if (featureSetObject == null) {
			return new Graphic[0];
		}

		JSONArray featuresJsonArray = featureSetObject.optJSONArray("features");

		Graphic[] result = new Graphic[featuresJsonArray.length()];
		for (int i = 0; i < featuresJsonArray.length(); i++) {
			JSONObject featureJsonObject = (JSONObject) featuresJsonArray
					.opt(i);
			JSONObject geometryJsonObject = (JSONObject) featureJsonObject
					.optJSONObject("geometry");
			JSONObject attributeJsonObject = (JSONObject) featureJsonObject
					.optJSONObject("attributes");

			Map<String, Object> attributeMap = new HashMap<String, Object>();
			Iterator<String> iterator = attributeJsonObject.keys();

			while (iterator.hasNext()) {
				String key = iterator.next();
				attributeMap.put(key, attributeJsonObject.opt(key));
			}

			Polygon polygon = new Polygon();
			JSONArray ringJsonArray = geometryJsonObject.optJSONArray("rings");
			for (int j = 0; j < ringJsonArray.length(); j++) {
				JSONArray subRingJsonArray = ringJsonArray.optJSONArray(j);
				for (int k = 0; k < subRingJsonArray.length(); k++) {
					JSONArray pointJsonArray = subRingJsonArray.optJSONArray(k);
					double x = pointJsonArray.optDouble(0);
					double y = pointJsonArray.optDouble(1);
					if (k == 0) {
						polygon.startPath(x, y);
					} else {
						polygon.lineTo(x, y);
					}
				}
			}
			polygon.closeAllPaths();
			result[i] = new Graphic(polygon, null, attributeMap);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	static Graphic[] parsePointFeatureSet(JSONObject object, String name) {
		JSONObject featureSetObject = object.optJSONObject(name);
		if (featureSetObject == null) {
			return new Graphic[0];
		}

		JSONArray featuresJsonArray = featureSetObject.optJSONArray("features");

		Graphic[] result = new Graphic[featuresJsonArray.length()];
		for (int i = 0; i < featuresJsonArray.length(); i++) {
			JSONObject featureJsonObject = (JSONObject) featuresJsonArray
					.opt(i);
			JSONObject geometryJsonObject = (JSONObject) featureJsonObject
					.optJSONObject("geometry");
			JSONObject attributeJsonObject = (JSONObject) featureJsonObject
					.optJSONObject("attributes");

			Map<String, Object> attributeMap = new HashMap<String, Object>();
			Iterator<String> iterator = attributeJsonObject.keys();
			while (iterator.hasNext()) {
				String key = iterator.next();
				attributeMap.put(key, attributeJsonObject.opt(key));
			}

			Point p = new Point(geometryJsonObject.optDouble("x"),
					geometryJsonObject.optDouble("y"));
			result[i] = new Graphic(p, null, attributeMap);
		}
		return result;
	}

	// static Graphic[] parsePolylineFeatureSet(JSONObject object, String name)
	// {
	//
	// }
}
