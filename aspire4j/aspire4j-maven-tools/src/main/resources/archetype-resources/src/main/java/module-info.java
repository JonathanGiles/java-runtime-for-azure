module ${moduleId} {
    exports ${package};

    requires com.microsoft.aspire;
    requires java.logging;

#if ($includeAzure == "true")    requires com.microsoft.aspire.extensions.azure.storage;
#end
#if ($includeSpring == "true")    requires com.microsoft.aspire.extensions.spring;
#end
}