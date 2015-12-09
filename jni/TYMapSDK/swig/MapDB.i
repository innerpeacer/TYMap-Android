/* ============= MapDB ================ */
/*
 * Swig File for MapDB Structure
 *
 *
*/	

namespace Innerpeacer {
    namespace MapSDK {
       
    	 class IPXFeatureRecord {
        public:
            geos::geom::Geometry *geometry;
            std::string geoID;
            std::string poiID;
            std::string categoryID;
            std::string name;
            int symbolID;
            int floorNumber;
            int layer;
            int levelMax;
    		int levelMin;
    
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