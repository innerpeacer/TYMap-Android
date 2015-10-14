/* ============= GEOS ================ */
/*
 * Swig File for GEOS Structure
 *
 *
*/	

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
		class Point {
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
		class LineString {
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
