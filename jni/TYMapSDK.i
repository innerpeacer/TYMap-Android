%module (allprotected="1") IPMapSDK

%include "std_string.i"

%{

#include "TYMapSDK/src/Utils/IPEncryption.hpp"
#include "TYMapSDK/src/Utils/MD5Utils.hpp"
#include "TYMapSDK/src/Utils/IPLicenseValidation.h"

#include <geos.h>
#include <geos/geom.h>

#include "TYMapSDK/src/RouteNetwork/IPXRouteNetworkDataset.hpp"
#include "TYMapSDK/src/RouteNetwork/IPXRouteNetworkDBAdapter.hpp"

using namespace std;
using namespace Innerpeacer::MapSDK;

using namespace geos;
using namespace geos::geom;

%}

typedef int size_t;

%include "TYMapSDK/swig/Geos.i"
%include "TYMapSDK/swig/Utils.i"
%include "TYMapSDK/swig/RouteNetwork.i"