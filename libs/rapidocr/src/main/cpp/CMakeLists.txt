cmake_minimum_required(VERSION 3.22.1)

project(RapidOcr)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# OnnxRuntime

get_filename_component(MODULE_DIR "${CMAKE_CURRENT_LIST_DIR}/../../.." ABSOLUTE)
if (NOT DEFINED ONNXRUNTIME_NATIVE_DIR)
    message(FATAL_ERROR "ONNXRUNTIME_NATIVE_DIR must point to the extracted Maven AAR")
endif ()
add_library(onnxruntime SHARED IMPORTED)
set_target_properties(onnxruntime PROPERTIES
    IMPORTED_LOCATION "${ONNXRUNTIME_NATIVE_DIR}/jni/${ANDROID_ABI}/libonnxruntime.so"
    INTERFACE_INCLUDE_DIRECTORIES "${ONNXRUNTIME_NATIVE_DIR}/headers")
set(OnnxRuntime_LIBS onnxruntime)
set(OnnxRuntime_INCLUDE_DIRS "${ONNXRUNTIME_NATIVE_DIR}/headers")
include_directories(${OnnxRuntime_INCLUDE_DIRS})

# Private OpenCV: the core and imgproc static libraries of the official OpenCV Android SDK.
# Its TBB backend avoids the OpenMP runtime that aborts next to Paddle Lite in user APKs, and
# hidden symbols keep this copy invisible to any other OpenCV runtime loaded in the same process.
if (NOT DEFINED OPENCV_SDK_NATIVE_DIR)
    message(FATAL_ERROR "OPENCV_SDK_NATIVE_DIR must point to sdk/native of the verified OpenCV Android SDK")
endif ()
set(OpenCV_DIR "${OPENCV_SDK_NATIVE_DIR}/jni")
find_package(OpenCV REQUIRED COMPONENTS core imgproc)
if (OpenCV_SHARED)
    message(FATAL_ERROR "RapidOcr must link the static OpenCV libraries")
endif ()
include_directories(${OpenCV_INCLUDE_DIRS})

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fomit-frame-pointer -fstrict-aliasing -ffast-math")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fomit-frame-pointer -fstrict-aliasing -ffast-math")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -ffunction-sections -fdata-sections")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -ffunction-sections -fdata-sections")

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fvisibility=hidden")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fvisibility=hidden -fvisibility-inlines-hidden")

# disable rtti and exceptions
# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fno-rtti -fno-exceptions")

include_directories(include)
file(GLOB OCR_SRC src/*.cpp)
set(OCR_COMPILE_CODE ${OCR_SRC})

add_library(RapidOcr SHARED ${OCR_COMPILE_CODE})

find_library( # Sets the name of the path variable.
        log-lib
        log)

find_library(
        android-lib
        android
)

target_link_libraries(
        RapidOcr
        ${OnnxRuntime_LIBS}
        ${OpenCV_LIBS}
        android
        z
        ${log-lib}
        ${android-lib}
        jnigraphics)

# Export only the JNI entry points; OpenCV, TBB and the other static archives stay local.
set(RAPID_OCR_EXPORTS "${CMAKE_CURRENT_LIST_DIR}/RapidOcr.map")
set_target_properties(RapidOcr PROPERTIES LINK_DEPENDS "${RAPID_OCR_EXPORTS}")
target_link_options(RapidOcr PRIVATE
        -Wl,--exclude-libs,ALL
        -Wl,--gc-sections
        "-Wl,--version-script=${RAPID_OCR_EXPORTS}"
        -Wl,-z,max-page-size=16384
        -Wl,-z,common-page-size=16384)
