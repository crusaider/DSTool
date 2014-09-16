# DSTool

A set of java classes that can be used as is or from a command line to download, upload and delete Google Appengine Datastore entities trough the remote API.

Using the GAE Data Store [Remote API] (https://developers.google.com/appengine/docs/java/tools/remoteapi) this tool is able to:
* Download all entities of a kind from a remote app engine application and either store it in local file our write it to stdout.
* Upload entities stored in a local file or read them fom stdin to a remote app engine application.
* Delete all entities of a kind from a remote app engine application.

# Command line syntax: 

``` 
usage: dstool appurl entitykind [-d | -r | -w] [-f <arg>] [-h] [-P <arg>]
       [-p <arg>]  [-u <arg>]
 -d,--delete           delete all entities of the specified kind from the
                       remote app datastore
 -f,--file <arg>       specify a file name to read/write local entity data
                       from/to, stdin/stdout will be used of the filename
                       is omitted
 -h,--help             print this message
 -P,--port <arg>       Network port used when connecting to the app, if
                       omitted the default port 80 wil be used
 -p,--password <arg>   Password to use when sending requests to the remote
                       app
 -r,--read             download all entites of the specified kind from
                       theremote app datastore
 -u,--username <arg>   Username to use when sending requests to the remote
                       app
 -w,--write            upload entites of the specified kind to the remote
                       app datastore
```

## As a library

The code in the sub package `com.momab.dstool` implements the actual logic for the operations but does not have to be run from the command line. I can be integrated into any java application.

## Dependencies

Apart from the [Google App Engine SDK](https://developers.google.com/appengine/downloads) the application also uses the [Apache Commons CLI library](http://commons.apache.org/proper/commons-cli/) for parsing of command line arguments. 