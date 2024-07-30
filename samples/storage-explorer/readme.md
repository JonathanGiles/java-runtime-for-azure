# Storage Explorer Sample

This sample is split into the following three modules:

* `storage-explorer`: A sample Spring application that is designed to work with Azure Services. It does not have any specific knowledge of the Java Runtime for Azure framework or how to deploy to Azure.
* `date-service`: A simple Spring application that provides a date microservice. It is used by the `storage-explorer`.
* `storage-explorer-apphost`: The infrastructure as code project that defines how the `storage-explorer` application, using the Java Runtime for Azure framework, should be deployed to Azure.
