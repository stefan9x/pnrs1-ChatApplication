LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libcrypto
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := crypto.cpp

include $(BUILD_SHARED_LIBRARY)
