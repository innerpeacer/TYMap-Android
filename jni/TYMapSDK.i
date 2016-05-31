%module (allprotected="1") IPMapSDK

%include "std_string.i"
%include "std_vector.i"

%{

#include "TYMapSDK/src/Utils/IPXEncryption.hpp"
#include "TYMapSDK/src/Utils/IPXMD5.hpp"
#include "TYMapSDK/src/Utils/IPXLicenseValidation.h"
#include "TYMapSDK/src/Utils/IPXGeosGeometryCaster.hpp"

#include <geos.h>
#include <geos/geom.h>

#include "TYMapSDK/src/RouteNetwork/IPXRouteNetworkDataset.hpp"
#include "TYMapSDK/src/RouteNetwork/IPXRouteNetworkDBAdapter.hpp"

#include "TYMapSDK/src/MapDB/IPXFeatureRecord.hpp"
#include "TYMapSDK/src/MapDB/IPXMapDataDBAdapter.hpp"

#include "TYMapSDK/src/PathCalibration/IPXPathCalibration.hpp"

using namespace std;
using namespace Innerpeacer::MapSDK;
using namespace Innerpeacer::MapSDK::GeosGeometryCaster;
//using namespace Innerpeacer::MapSDK::Encryption;
//using namespace Innerpeacer::MapSDK::License;

using namespace geos;
using namespace geos::geom;

%}

typedef int size_t;

%include "TYMapSDK/swig/Geos.i"
%include "TYMapSDK/swig/Utils.i"
%include "TYMapSDK/swig/RouteNetwork.i"
%include "TYMapSDK/swig/MapDB.i"
%include "TYMapSDK/swig/PathCalibration.i"