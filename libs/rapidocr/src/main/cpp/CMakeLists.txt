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

# Share the verified OpenCV/TBB runtime; static OpenMP conflicts with embedded Paddle Lite.
if (NOT DEFINED OPENCV_NATIVE_DIR)
    message(FATAL_ERROR "OPENCV_NATIVE_DIR must point to the verified OpenCV AAR")
endif ()
add_library(opencv_runtime SHARED IMPORTED)
set_target_properties(opencv_runtime PROPERTIES
    IMPORTED_LOCATION "${OPENCV_NATIVE_DIR}/jni/${ANDROID_ABI}/libopencv_java4.so"
    INTERFACE_INCLUDE_DIRECTORIES "${OPENCV_NATIVE_DIR}/headers")
set(OpenCV_LIBS opencv_runtime)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fomit-frame-pointer -fstrict-aliasing -ffast-math")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fomit-frame-pointer -fstrict-aliasing -ffast-math")

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

target_link_options(RapidOcr PRIVATE -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384)
