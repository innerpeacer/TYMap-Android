/* ============= MapDB ================ */
/*
 * Swig File for MapDB Structure
 *
 *
*/	

namespace Innerpeacer {
    namespace MapSDK {
    
       const geos::geom::Point *getPointN(geos::geom::MultiPoint *mp, std::size_t n);
        const geos::geom::LineString *getLineStringN(geos::geom::MultiLineString *ml, std::size_t n);
        const geos::geom::Polygon *getPolygonN(geos::geom::MultiPolygon *mp, std::size_t n);
    
    
    	 class IPXFeatureRecord {
        public:
            geos::geom::Geometry *geometry;
            geos::geom::Point *getPointIfSatisfied() const;
			geos::geom::MultiPoint *getMultiPointIfSatisfied() const;
			geos::geom::LineString *getLineStringIfSatisfied() const;
			geos::geom::MultiLineString *getMultiLineStringIfSatisfied() const;
			geos::geom::Polygon *getPolygonIfSatisfied() const;
			geos::geom::MultiPolygon *getMultiPolygonIfSatisfied() const;
            
            std::string geoID;
            std::string poiID;
            std::string categoryID;
            std::string name;
            int symbolID;
            int floorNumber;
            int layer;
            
            IPXFeatureRecord();
            ~IPXFeatureRecord();
        };
    
    }    
}

%template(VectorOfFeatureRecord) std::vector<Innerpeacer::MapSDK::IPXFeatureRecord *>;
typedef FeatureRecordList std::vector<Innerpeacer::MapSDK::IPXFeatureRecord *>;
namespace Innerpeacer {
    namespace MapSDK {        
        class IPXMapDataDBAdapter {
        public:
            IPXMapDataDBAdapter(const char *dbPath);
            bool open();
            bool close();
            std::vector<Innerpeacer::MapSDK::IPXFeatureRecord *> getAllRecordsOnFloor(int floor);
        };
    }
}