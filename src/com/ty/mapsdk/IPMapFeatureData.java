package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.ty.mapdata.TYBuilding;
import com.ty.mapsdk.swig.IPXFeatureRecord;
import com.ty.mapsdk.swig.IPXGeosCoordinate;
import com.ty.mapsdk.swig.IPXGeosGeometryTypeId;
import com.ty.mapsdk.swig.IPXGeosLineString;
import com.ty.mapsdk.swig.IPXGeosMultiPolygon;
import com.ty.mapsdk.swig.IPXGeosPoint;
import com.ty.mapsdk.swig.IPXGeosPolygon;
import com.ty.mapsdk.swig.IPXMapDataDBAdapter;
import com.ty.mapsdk.swig.VectorOfFeatureRecord;

class IPMapFeatureData {
	static final String TAG = IPMapFeatureData.class.getSimpleName();

	private TYBuilding currentBuilding;

	public IPMapFeatureData(TYBuilding building) {
		currentBuilding = building;

		// String dbPath = IPMapFileManager.getMapDBPath(currentBuilding);
		// Log.i(TAG, dbPath);
	}

	Map<String, Graphic[]> getAllMapDataOnFloor(int floor) {
		Map<String, Graphic[]> resultDict = new HashMap<String, Graphic[]>();

		String dbPath = IPMapFileManager.getMapDBPath(currentBuilding);
		IPXMapDataDBAdapter db = new IPXMapDataDBAdapter(dbPath);
		db.open();
		VectorOfFeatureRecord allRecords = db.getAllRecordsOnFloor(floor);
		db.close();

		List<Graphic> floorArray = new ArrayList<Graphic>();
		List<Graphic> roomArray = new ArrayList<Graphic>();
		List<Graphic> assetArray = new ArrayList<Graphic>();
		List<Graphic> facilityArray = new ArrayList<Graphic>();
		List<Graphic> labelArray = new ArrayList<Graphic>();

		for (int i = 0; i < allRecords.size(); i++) {
			IPXFeatureRecord record = allRecords.get(i);
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put("GEO_ID", record.getGeoID());
			attributes.put("POI_ID", record.getPoiID());
			attributes.put("CATEGORY_ID", record.getCategoryID());
			attributes.put("COLOR", record.getSymbolID());

			if (record.getName() != null && record.getName().length() > 0) {
				attributes.put("NAME", record.getName());
			}

			Geometry geometry = agsGeometryFromRecord(record);
			Graphic graphic = new Graphic(geometry, null, attributes);

			switch (record.getLayer()) {
			case 1:
				floorArray.add(graphic);
				break;
			case 2:
				roomArray.add(graphic);
				break;
			case 3:
				assetArray.add(graphic);
				break;
			case 4:
				facilityArray.add(graphic);
				break;
			case 5:
				labelArray.add(graphic);
				break;
			default:
				break;
			}
		}

		{
			Graphic[] floorGraphic = new Graphic[floorArray.size()];
			floorArray.toArray(floorGraphic);
			resultDict.put("floor", floorGraphic);

			Graphic[] roomGraphic = new Graphic[roomArray.size()];
			roomArray.toArray(roomGraphic);
			resultDict.put("room", roomGraphic);

			Graphic[] assetGraphic = new Graphic[assetArray.size()];
			assetArray.toArray(assetGraphic);
			resultDict.put("asset", assetGraphic);

			Graphic[] facilityGraphic = new Graphic[facilityArray.size()];
			facilityArray.toArray(facilityGraphic);
			resultDict.put("facility", facilityGraphic);

			Graphic[] labelGraphic = new Graphic[labelArray.size()];
			labelArray.toArray(labelGraphic);
			resultDict.put("label", labelGraphic);

			Log.i(TAG, "Floor: " + floorGraphic.length + ", Room: "
					+ roomGraphic.length + ", Asset: " + assetGraphic.length
					+ ", Facility: " + facilityGraphic.length + ", Label: "
					+ labelGraphic.length);
		}
		return resultDict;
	}

	static Geometry agsGeometryFromRecord(IPXFeatureRecord record) {
		if (record.getGeometry().getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_POINT) {
			// Log.i(TAG, "Geometry is Point");
			return agsPointFromGeosPoint(record.getPoint());
		} else if (record.getGeometry().getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_POLYGON) {
			// Log.i(TAG, "Geometry is Polygon");
			// Log.i(TAG, record.getPoint() + "");
			return agsPolygonFromGeosPolygon(record.getPolygon());
		}
		// else if (record.getGeometry().getGeometryTypeId() ==
		// IPXGeosGeometryTypeId.GEOS_MULTIPOLYGON) {
		// // Log.i(TAG, "Geometry is MultiPolygon");
		// return agsPolygonFromGeosMultiPolygon((IPXGeosMultiPolygon)
		// geometry);
		// }
		else {
			Log.i(TAG, "Geometry is Geometry");
			return null;
		}
	}

	static Point agsPointFromGeosPoint(IPXGeosPoint p) {
		return new Point(p.getX(), p.getY());
	}

	static Polygon agsPolygonFromGeosPolygon(IPXGeosPolygon p) {
		// Log.i(TAG, "agsPolygonFromGeosPolygon");
		Polygon polygon = new Polygon();

		IPXGeosLineString exteriorRing = p.getExteriorRing();
		if (exteriorRing != null) {
			for (int i = 0; i < exteriorRing.getNumPoints(); i++) {
				IPXGeosCoordinate c = exteriorRing.getCoordinateN(i);
				if (i == 0) {
					polygon.startPath(c.getX(), c.getY());
				} else {
					polygon.lineTo(c.getX(), c.getY());
				}
			}
		}

		for (int j = 0; j < p.getNumInteriorRing(); j++) {
			IPXGeosLineString interiorRing = p.getInteriorRingN(j);
			if (interiorRing != null) {
				for (int k = 0; k < interiorRing.getNumPoints(); k++) {
					IPXGeosCoordinate c = interiorRing.getCoordinateN(k);
					if (k == 0) {
						polygon.startPath(c.getX(), c.getY());
					} else {
						polygon.lineTo(c.getX(), c.getY());
					}
				}
			}
		}

		return polygon;
	}

	static Polygon agsPolygonFromGeosMultiPolygon(IPXGeosMultiPolygon p) {
		Polygon polygon = new Polygon();

		for (int l = 0; l < p.getNumGeometries(); l++) {
			IPXGeosPolygon simplePolygon = (IPXGeosPolygon) p.getGeometryN(l);

			IPXGeosLineString exteriorRing = simplePolygon.getExteriorRing();
			if (exteriorRing != null) {
				for (int i = 0; i < exteriorRing.getNumPoints(); i++) {
					IPXGeosCoordinate c = exteriorRing.getCoordinateN(i);
					if (i == 0) {
						polygon.startPath(c.getX(), c.getY());
					} else {
						polygon.lineTo(c.getX(), c.getY());
					}
				}
			}

			for (int j = 0; j < simplePolygon.getNumInteriorRing(); j++) {
				IPXGeosLineString interiorRing = simplePolygon
						.getInteriorRingN(j);
				if (interiorRing != null) {
					for (int k = 0; k < interiorRing.getNumPoints(); k++) {
						IPXGeosCoordinate c = interiorRing.getCoordinateN(k);
						if (k == 0) {
							polygon.startPath(c.getX(), c.getY());
						} else {
							polygon.lineTo(c.getX(), c.getY());
						}
					}
				}
			}
		}

		return polygon;
	}
}
