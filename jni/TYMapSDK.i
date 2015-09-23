%module (allprotected="1") IPMapSDK

%include "std_string.i"

%{

#include "TYMapSDK/src/Utils/IPEncryption.hpp"
#include "TYMapSDK/src/Utils/MD5Utils.hpp"
#include "TYMapSDK/src/Utils/IPLicenseValidation.h"

using namespace std;

%}

%include "TYMapSDK/swig/Utils.i"