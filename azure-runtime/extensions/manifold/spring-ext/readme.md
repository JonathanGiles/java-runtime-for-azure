# Manifold Extensions - Spring

Experimental extension modules using the [Manifold] extensions functionality. To include this project dependency in your project, add the following to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-runtime-extensions-spring-ext</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

This extension module provides the following extensions:

* `DistributedApplication`
  * `addSpringProject(String name)`
  * `addEurekaServiceDiscovery(String name)`

[Manifold]: http://manifold.systems/
