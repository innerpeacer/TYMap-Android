LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/libruntimecore_java.so
LOCAL_MODULE    := arcgis
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := TYMapSDK
LOCAL_SRC_FILES := 	TYMapSDK_wrap.cxx \
					TYMapSDK/src/Utils/IPEncryption.cpp \
					TYMapSDK/src/Utils/IPLicenseValidation.cpp \
					TYMapSDK/src/Utils/IPMemory.cpp \
					TYMapSDK/src/Utils/MD5.cpp \
					TYMapSDK/src/Utils/MD5Utils.cpp \
					
LOCAL_CPPFLAGS += -std=gnu++11 -fexceptions -frtti
				
include $(BUILD_SHARED_LIBRARY)
