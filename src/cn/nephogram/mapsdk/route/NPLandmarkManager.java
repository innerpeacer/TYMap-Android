package cn.nephogram.mapsdk.route;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.datamanager.NPMapFileManager;
import cn.nephogram.mapsdk.data.NPMapInfo;

import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

public class NPLandmarkManager {
	private static NPLandmarkManager landmarkManager;

	private int currentFloor = 0;
	private List<NPLandmark> allLandmarks = new ArrayList<NPLandmark>();

	private NPLandmarkManager() {

	}

	public static NPLandmarkManager sharedManager() {
		if (landmarkManager == null) {
			landmarkManager = new NPLandmarkManager();
		}
		return landmarkManager;
	}

	public void loadLandmark(NPMapInfo info) {
		allLandmarks.clear();
		currentFloor = info.getFloorNumber();

		String path = NPMapFileManager.getLandmarkJsonPath(info);
		if (new File(path).exists()) {
			JsonFactory factory = new JsonFactory();
			try {
				JsonParser parser = factory.createJsonParser(new File(path));
				FeatureSet set = FeatureSet.fromJson(parser);

				Graphic[] graphics = set.getGraphics();
				for (Graphic graphic : graphics) {
					String name = (String) graphic.getAttributeValue("NAME");
					Point pos = (Point) graphic.getGeometry();

					NPLocalPoint location = new NPLocalPoint(pos.getX(),
							pos.getY(), currentFloor);
					NPLandmark landmark = new NPLandmark();
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

	public NPLandmark searchLandmark(NPLocalPoint location, double tolerance) {
		if (location.getFloor() != currentFloor) {
			return null;
		}

		for (NPLandmark landmark : allLandmarks) {
			NPLocalPoint lp = landmark.getLocation();
			double distance = lp.distanceWithPoint(location);
			if (distance < tolerance) {
				return landmark;
			}
		}
		return null;
	}
}
