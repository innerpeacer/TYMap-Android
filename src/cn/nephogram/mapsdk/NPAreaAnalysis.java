package cn.nephogram.mapsdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

public class NPAreaAnalysis {

	private static final double DEFAULT_BUFFER = 1.0;

	private double buffer;
	private int areaCount;

	private List<NPPoi> featureArray;

	public NPAreaAnalysis(String path) {
		buffer = DEFAULT_BUFFER;
		featureArray = new ArrayList<NPPoi>();

		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);
			Graphic[] graphics = set.getGraphics();

			for (Graphic g : graphics) {
				NPPoi poi = new NPPoi(
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ASSET);
				featureArray.add(poi);
			}
			areaCount = featureArray.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<NPPoi> extractAOIs(double x, double y) {
		List<NPPoi> resultArray = new ArrayList<NPPoi>();

		Point point = new Point(x, y);
		for (NPPoi poi : featureArray) {
			Geometry bufferedGeometry = GeometryEngine.buffer(
					poi.getGeometry(),
					NPMapEnvironment.defaultSpatialReference(), buffer, null);
			if (GeometryEngine.contains(bufferedGeometry, point,
					NPMapEnvironment.defaultSpatialReference())) {
				resultArray.add(poi);
			}
		}
		return resultArray;
	}

	public double getBuffer() {
		return buffer;
	}

	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	public int getAreaCount() {
		return areaCount;
	}

}
