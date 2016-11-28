package com.ty.mapproject.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapproject.R;
import com.ty.mapsdk.TYLitsoTraceLayer;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYSnappingManager;
import com.ty.mapsdk.TYTraceLocalPoint;

public class TraceLayerActivity extends BaseMapViewActivity {
	static final String TAG = TraceLayerActivity.class.getSimpleName();

	GraphicsLayer testLayer;
	GraphicsLayer geometryLayer;
	// TYTraceLayer traceLayer;
	TYLitsoTraceLayer traceLayer;

	TYSnappingManager snappingManager;

	SimpleMarkerSymbol originMarkerSymbol;
	SimpleMarkerSymbol snappedCoordinateSymbol;
	SimpleMarkerSymbol snappedVertexSymbol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		originMarkerSymbol = new SimpleMarkerSymbol(Color.GREEN, 10,
				STYLE.CIRCLE);
		snappedCoordinateSymbol = new SimpleMarkerSymbol(Color.RED, 10,
				STYLE.CIRCLE);
		snappedVertexSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
				STYLE.CIRCLE);

		mapView.setHighlightPoiOnSelection(false);

		geometryLayer = new GraphicsLayer();
		mapView.addLayer(geometryLayer);
		geometryLayer.setRenderer(new SimpleRenderer(new SimpleLineSymbol(
				Color.GREEN, 2.0f)));

		mapView.setAllowRotationByPinch(true);

		// traceLayer = new TYTraceLayer();
		traceLayer = new TYLitsoTraceLayer();
		mapView.addLayer(traceLayer);

		testLayer = new GraphicsLayer();
		mapView.addLayer(testLayer);

		snappingManager = new TYSnappingManager(currentBuilding, mapInfos);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_trace;
	}

	int index = 0;
	float angle = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {
		Log.i(TAG, "Clicked Point: " + mappoint.getX() + ", " + mappoint.getY());

		// // 点击模拟轨迹点，测试轨迹层
		// TYLocalPoint lp = new TYLocalPoint(mappoint.getX(), mappoint.getY(),
		// mapView.getCurrentMapInfo().getFloorNumber());
		// TYTraceLocalPoint tp = new TYTraceLocalPoint();
		// tp.location = lp;
		// traceLayer.addTracePoint(tp);
		// // traceLayer.showTraces();
		// traceLayer.showSnappedTraces(snappingManager);

		// // 测试吸附功能
		// testLayer.removeAll();
		// testLayer.addGraphic(new Graphic(mappoint, originMarkerSymbol));
		//
		// Point snappedPoint = snappingManager.getSnappedPoint(lp);
		// testLayer
		// .addGraphic(new Graphic(snappedPoint, snappedCoordinateSymbol));
		//
		// Point snappedVertex = snappingManager.getSnappedVertexResult(lp)
		// .getCoordinate();
		// testLayer.addGraphic(new Graphic(snappedVertex,
		// snappedVertexSymbol));

		testTrace();
	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);

		Geometry route = snappingManager.getRouteGeometries().get(
				mapInfo.getFloorNumber());
		if (route != null) {
			geometryLayer.removeAll();
			geometryLayer.addGraphic(new Graphic(route, null));
		}
		traceLayer.setFloor(mapInfo.getFloorNumber());
	}

	private void testTrace() {
		File traceDataFile = new File(
				TYMapEnvironment.getRootDirectoryForMapFiles(),
				"TraceData.json");
		FileInputStream inStream;
		BufferedReader bufReader;
		try {
			inStream = new FileInputStream(new File(traceDataFile.toString()));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			JSONObject traceData = jsonObject.getJSONObject("Trace");
			JSONObject themes = traceData.getJSONObject("themes");

			Map<String, TYLocalPoint> themeArray = new HashMap<String, TYLocalPoint>();
			Iterator<String> themeIterator = themes.keys();
			while (themeIterator.hasNext()) {
				String themeID = themeIterator.next();
				JSONObject themeDict = themes.getJSONObject(themeID);
				TYLocalPoint lp = new TYLocalPoint(themeDict.getDouble("x"),
						themeDict.getDouble("y"), themeDict.getInt("floor"));
				themeArray.put(themeID, lp);
			}

			JSONArray points = traceData.getJSONArray("Points");
			List<TYTraceLocalPoint> pointArray = new ArrayList<TYTraceLocalPoint>();
			for (int i = 0; i < points.length(); ++i) {
				JSONObject pointDict = points.getJSONObject(i);
				TYTraceLocalPoint tp = new TYTraceLocalPoint();
				tp.location = new TYLocalPoint(pointDict.getDouble("x"),
						pointDict.getDouble("y"), pointDict.getInt("floor"));
				tp.inTheme = pointDict.getBoolean("inTheme");
				if (!pointDict.isNull("themeID")) {
					tp.themeID = pointDict.getString("themeID");
				}
				pointArray.add(tp);
			}

			Log.i(TAG, "points: " + pointArray.size());
			Log.i(TAG, "themes: " + themeArray.size());

			traceLayer.reset();
			traceLayer.addTracePoints(pointArray, themeArray);
			traceLayer.showSnappedTraces(snappingManager);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
