# cmake-deps
CMake-based repository manager for reusing and sharing compiled C/C++ libraries


# Quickstart

To install 'deps' copy the file `deps.cmake` and the folder `deps` to the root of the repository.

There are two flows when using 'deps':
- When developing a library to be shared with deps
- When using a library shared through deps

## Sharing a library

The basic flow to share a library is to compile it, create the archive and upload it to a server. deps automates this flow by providing a 'deploy' target (e.g., `make deploy`). To use the 'deploy' target, you will need two things:
- Add the 'deploy' target to the CMakeLists.txt of the library 
- Setup the server to where the archive will be uploaded

### Adding 'deploy' target

The 'deploy' target requires that you have a standard 'install' target already defined in CMakeLists.txt. To add the 'deploy' target, after the definition of the install target add the following code:

```
include("<REPOSITORY_ROOT>/deps.cmake")
make_package_target("<LIB_NAME>" "<HOST_ID>")
```

The function 'make_package_target' takes two arguments, the name of the library you will be creating, and an id for the host where you will upload the library to. 

To adapt already existing projects you do not need to change the 'install' target, just add those two lines to add the 'deploy' target.

### Setup server

When uploading a file, deps will look for the file 'deps/deps-lib/<HOST_ID>.properties', which will contain the details of the server. Looking at the example file 'deps/deps-lib/example_host.properties' in the repository:

```
# Host type. Currently only 'sftp' is supported
# For this type of host, there should be a file <HOST_ID>.creds with the credentials to access the host (login/pass)
type=sftp

# Address of the host (e.g., ip address)
host=127.0.0.1

# Path on the host to where deploy files should be transferred
location=/var/www/html/libs
```

The option 'type' defines how the file will be uploaded (currently only 'sftp' is supported). The option 'host' is the address of the server, and 'location' is the path to where the file will be transferred.

When using 'sftp', deps expects a file '<HOST_ID>.creds' to exist in the same folder as the .properties file. Looking at the example file 'deps/deps-lib/example_host.creds' in the repository:

```
# Login/pass to access a host. This file should not be committed
login = a_login
pass = a_pass
```

The '.creds' file will contain the login and pass in plain view and should not be commited to the repository. We acknowledge this is a very weak way to do authentication, we want to improve the authentication method in the near future.

## Using a library

(coming soon)
