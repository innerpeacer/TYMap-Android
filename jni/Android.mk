LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := sqlite3
LOCAL_SRC_FILES := sqlite3/$(TARGET_ARCH_ABI)/libsqlite3.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := arcgis/$(TARGET_ARCH_ABI)/libruntimecore_java.so
LOCAL_MODULE    := arcgis
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := geos/$(TARGET_ARCH_ABI)/libgeos.a
LOCAL_MODULE    := geos
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := TYMapSDK
LOCAL_SRC_FILES := 	TYMapSDK_wrap.cxx \
					TYMapSDK/src/Utils/IPEncryption.cpp \
					TYMapSDK/src/Utils/IPLicenseValidation.cpp \
					TYMapSDK/src/Utils/IPMemory.cpp \
					TYMapSDK/src/Utils/MD5.cpp \
					TYMapSDK/src/Utils/MD5Utils.cpp \
					TYMapSDK/src/RouteNetwork/IPXLink.cpp \
					TYMapSDK/src/RouteNetwork/IPXLinkRecord.cpp \
					TYMapSDK/src/RouteNetwork/IPXNode.cpp \
					TYMapSDK/src/RouteNetwork/IPXNodeRecord.cpp \
					TYMapSDK/src/RouteNetwork/IPXRouteNetworkDBAdapter.cpp \
					TYMapSDK/src/RouteNetwork/IPXRouteNetworkDataset.cpp
					
LOCAL_CPPFLAGS += -std=gnu++11 -fexceptions -frtti
LOCAL_C_INCLUDES := $(LOCAL_PATH)/geos/include $(LOCAL_PATH)/sqlite3/include
LOCAL_SHARED_LIBRARIES := sqlite3
LOCAL_STATIC_LIBRARIES := geos
				
include $(BUILD_SHARED_LIBRARY)
