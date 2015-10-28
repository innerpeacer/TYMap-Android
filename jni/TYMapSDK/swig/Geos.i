/* ============= GEOS ================ */
/*
 * Swig File for GEOS Structure
 *
 *
*/	

%rename(IPXGeosGeometryTypeId) GeometryTypeId;
namespace geos {
	namespace geom { 
enum GeometryTypeId {
	GEOS_POINT,
	GEOS_LINESTRING,
	GEOS_LINEARRING,
	GEOS_POLYGON,
	GEOS_MULTIPOINT,
	GEOS_MULTILINESTRING,
	GEOS_MULTIPOLYGON,
	GEOS_GEOMETRYCOLLECTION
};
}
}

%rename(IPXGeosGeometry) Geometry;
namespace geos {
	namespace geom { 
		class Geometry {
		public:
			virtual ~Geometry();
			virtual GeometryTypeId getGeometryTypeId() const=0;

		protected:
		Geometry();
		};
	}
}


%rename(IPXGeosCoordinate) Coordinate;
namespace geos {
	namespace geom { 
		class Coordinate {
		public:
			double x;
			double y;
		};
	}
}

%rename(IPXGeosPoint) Point;
namespace geos {
	namespace geom { 
		class Point : public Geometry{
		public:
			double getX() const;
			double getY() const;
			
		protected:
			Point(); 
		};
	}
}

%rename(IPXGeosGeometryFactory) GeometryFactory;
namespace geos {
	namespace geom { 
		class GeometryFactory {
		public:
			Point* createPoint(const Coordinate& coordinate) const;
		};
	}
}

%rename(IPXGeosLineString) LineString;
namespace geos {
	namespace geom { 
		class LineString : public Geometry {
		public:
			virtual ~LineString();
			virtual const Coordinate& getCoordinateN(int n) const;
			virtual std::size_t getNumPoints() const;
			virtual const Coordinate* getCoordinate() const;
			virtual double getLength() const;
			
		protected:
			LineString();	
		};
	}
}


%rename(IPXGeosPolygon) Polygon;
namespace geos {
	namespace geom { 
		class Polygon : public Geometry {
		public:
			virtual ~Polygon();
			const LineString* getExteriorRing() const;
			size_t getNumInteriorRing() const;
			const LineString* getInteriorRingN(std::size_t n) const;
		protected:
			Polygon();	
		};
	}
}

%rename(IPXGeosMultiPolygon) MultiPolygon;
namespace geos {
	namespace geom { 
		class MultiPolygon : public Geometry {
		public:
			virtual ~MultiPolygon();
			
			std::size_t getNumGeometries() const;
			const Geometry* getGeometryN(std::size_t) const;
		protected:
			MultiPolygon();	
		};
	}
}