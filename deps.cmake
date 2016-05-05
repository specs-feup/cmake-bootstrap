#
# Bootstraps 
#
cmake_minimum_required(VERSION 3.2)

#Initialize deps
if(NOT DEPS_ENABLED)
	include("deps/deps-lib/DepsInit.cmake")
endif()