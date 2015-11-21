/* ============= PathCalibration ================ */
/*
 * Swig File for PathCalibration Structure
 *
 *
*/	

namespace Innerpeacer {
    namespace MapSDK {
        
        class IPXPathCalibration {
  		   public:
            IPXPathCalibration(const char *dbPath);
            ~IPXPathCalibration();    
            
            void setBufferWidth(double w);
            geos::geom::Coordinate calibratePoint(geos::geom::Coordinate c);
            int getPathCount() const;
            
            geos::geom::Geometry *getUnionPaths() const;
            geos::geom::Geometry *getUnionPathBuffer() const;
                
        };
    }
}