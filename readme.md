# Aspire4J

This is a proof of concept project, which introduces a minimal example of a Java implementation of the Aspire framework. For an overview of Aspire, see [.NET Aspire overview](https://learn.microsoft.com/en-us/dotnet/aspire/get-started/aspire-overview). It aims to provide a Java-idiomatic developer experience that mimics the concepts introduced by the Aspire framework for .net. In particular, it introduces the concept of the AppHost, allowing for Java developers to define infrastructure as code, whilst continuing to rely on the existing Java ecosystem frameworks such as Spring, Micronaut, and Quarkus.

This GitHub repository is split into many sub-projects, but they can be broadly categorised as follows:

* **Aspire4J**:
  * `aspire4j`: The core library that provides the AppHost and the ability to define infrastructure as code.
  * `aspire4j-extensions-*`: Extensions for the Aspire4J library, providing support for Azure, Spring, etc.
  * `aspire4j-maven-tools`: A Maven archetype that can be used to create new Aspire4J App Hosts.
* **Sample Applications**:
  * **Storage Explorer**: Refer to the [readme](tree/main/samples/storage-explorer/readme.md) for more information.

In this way, the file of most interest is the `StorageExplorerAppHost` file, which defines the infrastructure as code.

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
2. From the root of the repository, run `mvn -f aspire4j/aspire4j-maven-tools clean install`.
3. Go to your existing Java project and run the following:

```shell
mvn archetype:generate \
  -DarchetypeGroupId=com.microsoft.aspire \
  -DarchetypeArtifactId=aspire4j-maven-tools \
  -DarchetypeVersion=1.0-SNAPSHOT
```

4. Follow the prompts to create your new Aspire4J project.
5. You will see a new directory created in your project with the name you provided in the prompt. Inside this directory you will find a new `AspireAppHost` Java class that you can use to define your infrastructure as code.
