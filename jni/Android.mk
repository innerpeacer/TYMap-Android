LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := arcgis/$(TARGET_ARCH_ABI)/libruntimecore_java.so
LOCAL_MODULE    := arcgis
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := geos/$(TARGET_ARCH_ABI)/libgeos.a
LOCAL_MODULE    := geos
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := sqlite3
LOCAL_SRC_FILES := sqlite3/$(TARGET_ARCH_ABI)/libsqlite3.so
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := TYMapSDK
LOCAL_SRC_FILES := 	TYMapSDK_wrap.cxx \
					TYMapSDK/src/Utils/IPXEncryption.cpp \
					TYMapSDK/src/Utils/IPXLicenseValidation.cpp \
					TYMapSDK/src/Utils/IPXMemory.cpp \
					TYMapSDK/src/Utils/IPXMD5.cpp \
					TYMapSDK/src/Utils/IPXBase64Encoding.cpp \
					TYMapSDK/src/Utils/IPXGeosGeometryCaster.cpp \
					TYMapSDK/src/RouteNetwork/IPXLink.cpp \
					TYMapSDK/src/RouteNetwork/IPXLinkRecord.cpp \
					TYMapSDK/src/RouteNetwork/IPXNode.cpp \
					TYMapSDK/src/RouteNetwork/IPXNodeRecord.cpp \
					TYMapSDK/src/RouteNetwork/IPXRouteNetworkDBAdapter.cpp \
					TYMapSDK/src/RouteNetwork/IPXRouteNetworkDataset.cpp \
					TYMapSDK/src/MapDB/IPXFeatureRecord.cpp \
					TYMapSDK/src/MapDB/IPXMapDataDBAdapter.cpp \
					TYMapSDK/src/PathCalibration/IPXPathCalibration.cpp \
					TYMapSDK/src/PathCalibration/IPXPathDBAdapter.cpp
					
LOCAL_CPPFLAGS += -std=gnu++11 -fexceptions -frtti
LOCAL_C_INCLUDES := $(LOCAL_PATH)/geos/include $(LOCAL_PATH)/sqlite3/include
LOCAL_SHARED_LIBRARIES := sqlite3
LOCAL_STATIC_LIBRARIES := geos				 

include $(BUILD_SHARED_LIBRARY)
