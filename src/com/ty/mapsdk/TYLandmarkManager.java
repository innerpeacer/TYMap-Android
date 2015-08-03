package com.ty.mapsdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;


import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.ty.mapdata.TYLocalPoint;

public class TYLandmarkManager {
	private static TYLandmarkManager landmarkManager;

	private int currentFloor = 0;
	private List<TYLandmark> allLandmarks = new ArrayList<TYLandmark>();

	private TYLandmarkManager() {

	}

	public static TYLandmarkManager sharedManager() {
		if (landmarkManager == null) {
			landmarkManager = new TYLandmarkManager();
		}
		return landmarkManager;
	}

	public void loadLandmark(TYMapInfo info) {
		allLandmarks.clear();
		currentFloor = info.getFloorNumber();

		String path = IPMapFileManager.getLandmarkJsonPath(info);
		if (new File(path).exists()) {
			JsonFactory factory = new JsonFactory();
			try {
				JsonParser parser = factory.createJsonParser(new File(path));
				FeatureSet set = FeatureSet.fromJson(parser);

				Graphic[] graphics = set.getGraphics();
				for (Graphic graphic : graphics) {
					String name = (String) graphic.getAttributeValue("NAME");
					Point pos = (Point) graphic.getGeometry();

					TYLocalPoint location = new TYLocalPoint(pos.getX(),
							pos.getY(), currentFloor);
					TYLandmark landmark = new TYLandmark();
					landmark.setName(name);
					landmark.setLocation(location);

					allLandmarks.add(landmark);
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TYLandmark searchLandmark(TYLocalPoint location, double tolerance) {
		if (location.getFloor() != currentFloor) {
			return null;
		}

		for (TYLandmark landmark : allLandmarks) {
			TYLocalPoint lp = landmark.getLocation();
			double distance = lp.distanceWithPoint(location);
			if (distance < tolerance) {
				return landmark;
			}
		}
		return null;
	}
}
