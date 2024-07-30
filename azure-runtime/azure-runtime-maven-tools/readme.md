# Maven Tools

If you want to use the Java Runtime for Azure in your Java microservice projects, you can! Here's how you can create your own App Host:

1. Clone this repository onto your machine.
2. From the root of the repository, run `mvn -f azure-runtime/azure-runtime-maven-tools clean install`.
3. Go to your existing Java project and run the following:

```shell
mvn archetype:generate \
  -DarchetypeGroupId=com.azure \
  -DarchetypeArtifactId=azure-runtime-maven-tools \
  -DarchetypeVersion=1.0-SNAPSHOT
```

4. Follow the prompts to create your new App Host project.
5. You will see a new directory created in your project with the name you provided in the prompt. Inside this directory you will find a new `AppHost` Java class that you can use to define your infrastructure as code.