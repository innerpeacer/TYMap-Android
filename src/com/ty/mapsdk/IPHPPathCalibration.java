package com.ty.mapsdk;

import java.io.File;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.ty.mapsdk.swig.IPXGeosCoordinate;
import com.ty.mapsdk.swig.IPXPathCalibration;

class IPHPPathCalibration {
	static final String PATH_CALIBRATION_SOURCE_PATH = "%s_Path.db";

	private boolean pathDBExist = false;
	private double width;
	private IPXPathCalibration pathCalibration;
	private Geometry unionPathLine;
	private Geometry unionPathPolygon;

	public IPHPPathCalibration(TYMapInfo mapInfo) {
		String buildingDir = TYMapEnvironment.getDirectoryForBuilding(
				mapInfo.getCityID(), mapInfo.getBuildingID());
		String pathDBName = String.format(PATH_CALIBRATION_SOURCE_PATH,
				mapInfo.getMapID());
		File dbFile = new File(buildingDir, pathDBName);

		pathDBExist = dbFile.exists();
		if (pathDBExist) {
			pathCalibration = new IPXPathCalibration(dbFile.toString());
			unionPathLine = new Polyline();
			unionPathPolygon = new Polygon();

			int pathCount = pathCalibration.getPathCount();
			if (pathCount > 0) {
				unionPathLine = IPHPGeos2AgsConverter
						.agsGeometryFromGeosGeometry(pathCalibration
								.getUnionPaths());
				unionPathPolygon = IPHPGeos2AgsConverter
						.agsGeometryFromGeosGeometry(pathCalibration
								.getUnionPathBuffer());
			} else {
				pathDBExist = false;
			}
		}
	}

	public void setBufferWidth(double w) {
		width = w;
		if (pathDBExist) {
			pathCalibration.setBufferWidth(width);
			unionPathPolygon = IPHPGeos2AgsConverter
					.agsGeometryFromGeosGeometry(pathCalibration
							.getUnionPathBuffer());
		}
	}

	public Point calibrationPoint(Point point) {
		if (!pathDBExist) {
			return point;
		}

		IPXGeosCoordinate c = new IPXGeosCoordinate();
		c.setX(point.getX());
		c.setY(point.getY());

		IPXGeosCoordinate result = pathCalibration.calibratePoint(c);
		return new Point(result.getX(), result.getY());
	}

	public Polyline getUnionPath() {
		return (Polyline) unionPathLine;
	}

	public Polygon getUnionPolygon() {
		return (Polygon) unionPathPolygon;
	}
}
