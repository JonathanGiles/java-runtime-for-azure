# Aspire4J

This is a proof of concept project, which introduces a minimal example of a Java implementation of the Aspire framework. It aims to provide a Java-idiomatic developer experience that mimics the concepts introduced by the Aspire framework for .net. In particular, it introduces the concept of the AppHost, allowing for Java developers to define infrastructure as code, whilst continuing to rely on the existing Java ecosystem frameworks such as Spring, Micronaut, and Quarkus.

This GitHub repository is split into six sub-projects:

* `aspire4j`: The core library that provides the AppHost and the ability to define infrastructure as code.
* `aspire4j-extensions-azure-storage`: An extension to the core library that provides the ability to configure Azure Storage resources.
* `aspire4j-extensions-azure`: A wrapper project that developers could use to bring in all Azure extensions at once.
* `storage-explorer`: A sample Spring application that is designed to work with Azure Services. It does not have any specific knowledge of the Aspire4J framework or how to deploy to Azure.
* `date-service`: A simple Spring application that provides a date microservice. It is used by the `storage-explorer`.
* `storage-explorer-apphost`: The infrastructure as code project that defines how the `storage-explorer` application, using the Aspire4J framework, should be deployed to Azure.

In this way, the file of most interest is the [StorageExplorerAppHost file](https://github.com/JonathanGiles/aspire4j/blob/main/storage-explorer-apphost/src/main/java/com/microsoft/aspire/springsample/StorageExplorerAppHost.java), which defines the infrastructure as code.

## Getting Started

> [!IMPORTANT]
> Because this project is a very early proof of concept, `azd` does not know how to work with Aspire4J projects yet. To work around this, we have developed a fork of `azd` that can be used to work with Aspire4J projects. Instructions on how to work with this fork should [be followed here](https://github.com/Azure/azure-dev-pr/pull/1670).

With the new `azd` installed, you can proceed with running this project locally, and having it be deployed into Azure by following these instructions:

1. Clone this repository onto your machine.
2. Open a terminal and navigate to the root of the repository.
3. Run `azd init`, followed by `azd up`.

Feedback is welcome!

## Using Aspire4J

If you want to use Aspire4J in your Java microservice projects, you can! Here's how you can create your own Aspire App Host:

1. Clone this repository onto your machine.
2. From the root of the repository, run `mvn -f aspire4j-maven-archetype clean install`.
3. Go to your existing Java project and run the following:

```shell
mvn archetype:generate \
  -DarchetypeGroupId=com.microsoft.aspire \
  -DarchetypeArtifactId=aspire4j-maven-archetype \
  -DarchetypeVersion=1.0-SNAPSHOT
```

4. Follow the prompts to create your new Aspire4J project.
5. You will see a new directory created in your project with the name you provided in the prompt. Inside this directory you will find a new `AspireAppHost` Java class that you can use to define your infrastructure as code.