#
# Resolves a library, downloading the needed files from a host.
#
# ARGUMENTS:
#   - lib_name : the name of the library to resolve
#   - lib_dir : this variable will be used to store the value of the base folder of the downloaded artifacts for the given library
#
# EXAMPLE:
#   deps_resolve("HashTable")
#   Looks for a library 'HashTable' in the current hosts list
#


function(deps_resolve lib_name lib_dir)

	if(NOT DEPS_ENABLED)
		message(FATAL_ERROR "'Deps' is not enabled, include 'deps.cmake' first")
	endif()
	
	# Check if folder for current system and compiler exist
	set(${lib_dir} "${DEPS_ARTIFACTS_DIR}/${lib_name}-${CMAKE_SYSTEM_NAME}-${DEPS_ENVIRONMENT}" PARENT_SCOPE)
	
	
	if(NOT EXISTS "${${lib_dir}}/")
		message("-- [Deps] Resolving dependency '${lib_name}'...")
		#message(STATUS "ARG0:${DEPS_JAR_PROPS}")
		#message(STATUS "ARG1:${lib_name}")
		#message(STATUS "ARG2:${CMAKE_SYSTEM_NAME}")
		#message(STATUS "ARG3:${DEPS_ENVIRONMENT}")
		#message(STATUS "ARG4:${DEPS_ARTIFACTS_DIR}")
		execute_process(COMMAND ${Java_JAVA_EXECUTABLE} -jar ${DEPS_JAR_DIR}/${DEPS_JAR} resolve ${DEPS_JAR_PROPS} ${lib_name} ${CMAKE_SYSTEM_NAME} ${DEPS_ENVIRONMENT} ${DEPS_ARTIFACTS_DIR}
		WORKING_DIRECTORY ${CMAKE_CURRENT_LIST_DIR}
		RESULT_VARIABLE JAVA_RESULT
		OUTPUT_VARIABLE JAVA_OUTPUT
		ERROR_VARIABLE JAVA_OUTPUT)
	
		if(NOT ${JAVA_RESULT})
			#message("-- [Deps] Dependency resolved ${JAVA_OUTPUT}")
		else()
			message( FATAL_ERROR "Could not retrieve library '${lib_name}'. Reason:\n${JAVA_OUTPUT}" )
		endif()
	endif()

endfunction()
