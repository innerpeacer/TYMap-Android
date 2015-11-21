std::string md5(std::string str);

std::string decryptString(std::string str);
std::string encryptString(std::string str);
std::string decryptFile(const char *file);


std::string decryptString(std::string str, std::string key);
std::string encryptString(std::string originalString, std::string key);

void encryptFile(const char *originalPath, const char *encryptedFile, const char *key);
std::string decryptFile(const char *file, const char *key);

bool checkValidity(std::string userID, std::string license, std::string buildingID);
std::string getExpiredDate(std::string userID, std::string license, std::string buildingID);


namespace Innerpeacer {
    namespace MapSDK {
        namespace GeosGeometryCaster {
            geos::geom::Point *CastedPoint(geos::geom::Geometry *g);
            geos::geom::MultiPoint *CastedMultiPoint(geos::geom::Geometry *g);
            geos::geom::LineString *CastedLineString(geos::geom::Geometry *g);
            geos::geom::MultiLineString *CastedMultiLineString(geos::geom::Geometry *g);
            geos::geom::Polygon *CastedPolygon(geos::geom::Geometry *g);
            geos::geom::MultiPolygon *CastedMultiPolygon(geos::geom::Geometry *g);
        }
    }
}