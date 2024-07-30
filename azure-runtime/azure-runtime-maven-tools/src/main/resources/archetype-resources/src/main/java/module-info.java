module ${moduleId} {
    exports ${package};

    requires com.azure.runtime.host;
    requires java.logging;

#if ($includeAzure == "true")    requires com.azure.runtime.host.extensions.azure.storage;
#end
#if ($includeSpring == "true")    requires com.azure.runtime.host.extensions.spring;
#end
}