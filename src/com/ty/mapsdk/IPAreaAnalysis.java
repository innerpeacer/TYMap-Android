package com.ty.mapsdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.ty.mapsdk.TYPoi.POI_LAYER;

class IPAreaAnalysis {

	private static final double DEFAULT_BUFFER = 1.0;

	private double buffer;
	private int areaCount;

	private List<TYPoi> featureArray;

	public IPAreaAnalysis(String path) {
		buffer = DEFAULT_BUFFER;
		featureArray = new ArrayList<TYPoi>();

		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);
			Graphic[] graphics = set.getGraphics();

			for (Graphic g : graphics) {
				TYPoi poi = new TYPoi(
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_NAME),
						g.getGeometry(),
						(Integer) g
								.getAttributeValue(IPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ASSET);
				featureArray.add(poi);
			}
			areaCount = featureArray.size();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TYPoi> extractAOIs(double x, double y) {
		List<TYPoi> resultArray = new ArrayList<TYPoi>();

		Point point = new Point(x, y);
		for (TYPoi poi : featureArray) {
			Geometry bufferedGeometry = GeometryEngine.buffer(
					poi.getGeometry(),
					TYMapEnvironment.defaultSpatialReference(), buffer, null);
			if (GeometryEngine.contains(bufferedGeometry, point,
					TYMapEnvironment.defaultSpatialReference())) {
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
