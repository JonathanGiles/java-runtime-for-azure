# Java Runtime for Azure

This is a proof of concept project, which introduces a minimal Java runtime for Azure. It provides a Java-idiomatic developer experience for defining the infrastructure required for successful deployment to Azure. In particular, it introduces the concept of the App Host, allowing for Java developers to define infrastructure as code, whilst continuing to rely on the existing Java ecosystem frameworks such as Spring, Micronaut, and Quarkus.

> [!TIP]
> JavaDoc is [published on every push to this repo](https://aspire4j.z22.web.core.windows.net/). It's a great way to see all of the available APIs and how to use them.

This GitHub repository is split into many sub-projects, but they can be broadly categorised as follows:

* **azure-runtime**:
  * `/azure-runtime`: The core library that provides the AppHost and the ability to define infrastructure as code.
  * `/extensions/*`: Extensions for the Azure Runtime, providing support for Azure, Spring, etc.
  * `/azure-runtime-maven-tools`: A Maven archetype that can be used to create new App Hosts.
* **Sample Applications**:
  * **Storage Explorer**: Refer to the [StorageExplorerReadme] for more information.

In this way, the file of most interest is the [StorageExplorerAppHost] file, which defines the infrastructure as code.

## Getting Started

> [!IMPORTANT]
> Because this project is a very early proof of concept, `azd` does not know how to work with Java Runtime for Azure projects yet. To work around this, we have developed a fork of `azd` that can be used. Instructions on how to work with this fork should [be followed here](https://github.com/Azure/azure-dev-pr/pull/1670).

With the new `azd` installed, you can proceed with running this project locally, and having it be deployed into Azure by following these instructions:

1. Clone this repository onto your machine.
2. Open a terminal and navigate to the root of the repository.
3. Run `azd init`, followed by `azd up`.

Feedback is welcome!

## Using Java Runtime for Azure

If you want to use the Java Runtime for Azure in your Java microservice projects, you can! Refer to the [Azure Runtime Maven Tools sub-project][AzureRuntimeMavenToolsReadme] for more information.

[AzureRuntimeMavenToolsReadme]: azure-runtime/azure-runtime-maven-tools/readme.md
[StorageExplorerReadme]: samples/storage-explorer/readme.md
[StorageExplorerAppHost]: samples/storage-explorer/storage-explorer-apphost/src/main/java/com/azure/example/storageexplorer/StorageExplorerAppHost.java