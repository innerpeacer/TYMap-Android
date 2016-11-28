package com.ty.mapsdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;

@SuppressLint("UseSparseArrays")
@SuppressWarnings("unchecked")
public class TYSnappingManager {
	private Map<Integer, Geometry> routeGeometries = new HashMap<Integer, Geometry>();

	public TYSnappingManager(TYBuilding building, List<TYMapInfo> mapInfoArray) {
		String buildingDir = TYMapEnvironment.getDirectoryForBuilding(building);
		File snappingFile = new File(buildingDir, String.format(
				"%s_Snapping.json", building.getBuildingID()));
		if (snappingFile.exists()) {
			FileInputStream inStream;
			BufferedReader bufReader;
			try {
				inStream = new FileInputStream(
						new File(snappingFile.toString()));
				InputStreamReader inputReader = new InputStreamReader(inStream);
				bufReader = new BufferedReader(inputReader);

				String line = "";
				StringBuffer jsonStr = new StringBuffer();
				while ((line = bufReader.readLine()) != null)
					jsonStr.append(line);

				JsonFactory factory = new JsonFactory();
				JSONObject jsonObject = new JSONObject(jsonStr.toString());
				if (jsonObject != null) {
					Iterator<String> mapIDIterable = jsonObject.keys();

					while (mapIDIterable.hasNext()) {
						String mapID = mapIDIterable.next();

						TYMapInfo targetInfo = null;
						for (TYMapInfo info : mapInfoArray) {
							if (info.getMapID().equals(mapID)) {
								targetInfo = info;
								break;
							}
						}

						if (targetInfo != null) {
							JsonParser parser = factory
									.createJsonParser(jsonObject
											.getString(mapID));
							FeatureSet set = FeatureSet.fromJson(parser);
							Graphic graphic = set.getGraphics()[0];
							routeGeometries.put(targetInfo.getFloorNumber(),
									graphic.getGeometry());
						}
					}

				}
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

	public Point getSnappedPoint(TYLocalPoint location) {
		Point point = new Point(location.getX(), location.getY());
		Geometry geometry = routeGeometries.get(location.getFloor());
		if (geometry != null) {
			Proximity2DResult pr = GeometryEngine.getNearestCoordinate(
					geometry, point, false);
			return pr.getCoordinate();
		}
		return point;
	}

	public Proximity2DResult getSnappedResult(TYLocalPoint location) {
		Point point = new Point(location.getX(), location.getY());
		Geometry geometry = routeGeometries.get(location.getFloor());
		if (geometry != null) {
			Proximity2DResult pr = GeometryEngine.getNearestCoordinate(
					geometry, point, false);
			return pr;
		}
		return null;
	}

	public Proximity2DResult getSnappedVertexResult(TYLocalPoint location) {
		Point point = new Point(location.getX(), location.getY());
		Geometry geometry = routeGeometries.get(location.getFloor());
		if (geometry != null) {
			Proximity2DResult pr = GeometryEngine.getNearestVertex(geometry,
					point);
			return pr;
		}
		return null;
	}

	public Map<Integer, Geometry> getRouteGeometries() {
		return routeGeometries;
	}

}
