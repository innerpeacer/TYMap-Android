package com.ty.mapsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;
import com.ty.mapdata.TYBuilding;
import com.ty.mapsdk.swig.IPXFeatureRecord;
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
			attributes.put("LEVEL_MAX", record.getLevelMax());
			attributes.put("LEVEL_MIN", record.getLevelMin());

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
		return IPGeos2AgsConverter.agsGeometryFromGeosGeometry(record
				.getGeometry());
	}
}
