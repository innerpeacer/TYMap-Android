package com.ty.mapsdk;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.ty.mapsdk.swig.IPMapSDK;
import com.ty.mapsdk.swig.IPXGeosCoordinate;
import com.ty.mapsdk.swig.IPXGeosGeometry;
import com.ty.mapsdk.swig.IPXGeosGeometryTypeId;
import com.ty.mapsdk.swig.IPXGeosLineString;
import com.ty.mapsdk.swig.IPXGeosMultiLineString;
import com.ty.mapsdk.swig.IPXGeosMultiPolygon;
import com.ty.mapsdk.swig.IPXGeosMutliPoint;
import com.ty.mapsdk.swig.IPXGeosPoint;
import com.ty.mapsdk.swig.IPXGeosPolygon;

class IPHPGeos2AgsConverter {
	static final String TAG = IPHPGeos2AgsConverter.class.getSimpleName();

	public static Geometry agsGeometryFromGeosGeometry(IPXGeosGeometry geometry) {
		if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_POINT) {
			return agsPointFromGeosPoint(IPMapSDK.CastedPoint(geometry));
		} else if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_MULTIPOINT) {
			return agsMultiPointFromGeosMultiPoint(IPMapSDK
					.CastedMultiPoint(geometry));
		} else if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_LINESTRING) {
			return agsPolylineFromGeosLineString(IPMapSDK
					.CastedLineString(geometry));
		} else if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_MULTILINESTRING) {
			return agsPolylineFromGeosMultiLineString(IPMapSDK
					.CastedMultiLineString(geometry));
		} else if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_POLYGON) {
			return agsPolygonFromGeosPolygon(IPMapSDK.CastedPolygon(geometry));
		} else if (geometry.getGeometryTypeId() == IPXGeosGeometryTypeId.GEOS_MULTIPOLYGON) {
			return agsPolygonFromGeosMultiPolygon(IPMapSDK
					.CastedMultiPolygon(geometry));
		} else {
			Log.i(TAG, "Geometry is Geometry");
			return null;
		}
	}

	public static Point agsPointFromGeosPoint(IPXGeosPoint p) {
		return new Point(p.getX(), p.getY());
	}

	public static MultiPoint agsMultiPointFromGeosMultiPoint(
			IPXGeosMutliPoint mp) {
		MultiPoint result = new MultiPoint();
		for (int i = 0; i < mp.getNumGeometries(); i++) {
			IPXGeosPoint p = IPMapSDK.getPointN(mp, i);
			result.add(new Point(p.getX(), p.getY()));
		}
		return result;
	}

	public static Polyline agsPolylineFromGeosLineString(IPXGeosLineString ls) {
		Polyline result = new Polyline();

		for (int i = 0; i < ls.getNumPoints(); i++) {
			IPXGeosCoordinate c = ls.getCoordinateN(i);
			if (i == 0) {
				result.startPath(c.getX(), c.getY());
			} else {
				result.lineTo(c.getX(), c.getY());
			}
		}
		return result;
	}

	// public static Polyline agsPolylineFromGeosLinearRing() {
	// return null;
	// }

	public static Polyline agsPolylineFromGeosMultiLineString(
			IPXGeosMultiLineString mls) {
		Polyline result = new Polyline();

		for (int l = 0; l < mls.getNumGeometries(); ++l) {
			IPXGeosLineString ls = IPMapSDK.getLineStringN(mls, l);
			for (int i = 0; i < ls.getNumPoints(); i++) {
				IPXGeosCoordinate c = ls.getCoordinateN(i);
				if (i == 0) {
					result.startPath(c.getX(), c.getY());
				} else {
					result.lineTo(c.getX(), c.getY());
				}
			}
		}
		return result;
	}

	public static Polygon agsPolygonFromGeosPolygon(IPXGeosPolygon p) {
		Polygon result = new Polygon();
		IPXGeosLineString exteriorRing = p.getExteriorRing();
		if (exteriorRing != null) {
			for (int i = 0; i < exteriorRing.getNumPoints(); i++) {
				IPXGeosCoordinate c = exteriorRing.getCoordinateN(i);
				if (i == 0) {
					result.startPath(c.getX(), c.getY());
				} else {
					result.lineTo(c.getX(), c.getY());
				}
			}
		}

		for (int j = 0; j < p.getNumInteriorRing(); j++) {
			IPXGeosLineString interiorRing = p.getInteriorRingN(j);
			if (interiorRing != null) {
				for (int k = 0; k < interiorRing.getNumPoints(); k++) {
					IPXGeosCoordinate c = interiorRing.getCoordinateN(k);
					if (k == 0) {
						result.startPath(c.getX(), c.getY());
					} else {
						result.lineTo(c.getX(), c.getY());
					}
				}
			}
		}
		return result;
	}

	public static Polygon agsPolygonFromGeosMultiPolygon(IPXGeosMultiPolygon p) {
		Polygon result = new Polygon();

		for (int l = 0; l < p.getNumGeometries(); l++) {
			IPXGeosPolygon simplePolygon = IPMapSDK.getPolygonN(p, l);
			if (simplePolygon == null) {
				continue;
			}

			IPXGeosLineString exteriorRing = simplePolygon.getExteriorRing();
			if (exteriorRing != null) {
				for (int i = 0; i < exteriorRing.getNumPoints(); i++) {
					IPXGeosCoordinate c = exteriorRing.getCoordinateN(i);
					if (i == 0) {
						result.startPath(c.getX(), c.getY());
					} else {
						result.lineTo(c.getX(), c.getY());
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
							result.startPath(c.getX(), c.getY());
						} else {
							result.lineTo(c.getX(), c.getY());
						}
					}
				}
			}
		}
		return result;
	}
}
