/* ============= RouteNetwork ================ */
/*
 * Swig File for PathCalibration Structure
 *
 *
*/	

namespace Innerpeacer {
    namespace MapSDK {
        
        class IPXRouteNetworkDataset {
        public:
        	IPXRouteNetworkDataset();
            ~IPXRouteNetworkDataset();
            geos::geom::LineString *getShorestPath(geos::geom::Point *start, geos::geom::Point *end);
            std::string toString() const;
        };
    }
}

namespace Innerpeacer {
    namespace MapSDK {
     
        class IPXRouteNetworkDBAdapter {
        public:
            IPXRouteNetworkDBAdapter(const char *dbPath);
            bool open();
            bool close();
            IPXRouteNetworkDataset *readRouteNetworkDataset();
        };
    }
}

