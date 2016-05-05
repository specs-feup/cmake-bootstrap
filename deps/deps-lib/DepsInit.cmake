#
# SET VARIABLES - variables set by this module
#  - DEPS_ENABLED: Flag that indicates this module was executed
#  - DEPS_ENVIRONMENT: An id for the compilation platform of the compiled libraries
#  - DEPS_LIBRARIES: Libraries for linking
#  - DEPS_DLLS_[WIN32 | UNIX]: Contains folders with dynamic libraries that should be copied to where the executable is
#

cmake_minimum_required(VERSION 3.2)

# Set flag, to signal that deps is enabled
set(DEPS_ENABLED 1)


## Define compiler name. Currently only supports gcc.

# Check GCC
if("${CMAKE_C_COMPILER_ID}" MATCHES "GNU")
	# Check GCC version
	#CMAKE_CXX_COMPILER_VERSION
	if (CMAKE_CXX_COMPILER_VERSION VERSION_GREATER 5)
		set(DEPS_COMPILER "gcc5")
	elseif(CMAKE_CXX_COMPILER_VERSION VERSION_GREATER 4)
		set(DEPS_COMPILER "gcc4")
	else()
		message(FATAL_ERROR "-- [Deps] gcc version '${CMAKE_C_COMPILER_VERSION}' not supported (use gcc-4 or gcc-5)")		
	endif()
else()
	message(FATAL_ERROR "-- [Deps] Compiler not supported: '${CMAKE_C_COMPILER_ID}'")
endif()


# Define compilation environment
set(DEPS_ENVIRONMENT "${DEPS_COMPILER}")
message("-- [Deps] Setting compiled library environment to '${DEPS_ENVIRONMENT}'")

# Download modules to the current path
# TODO: Call JAR to download modules from the given list of hosts

# Add the modules in the current path 
set(CMAKE_MODULE_PATH "${CMAKE_CURRENT_LIST_DIR};${CMAKE_MODULE_PATH}")


