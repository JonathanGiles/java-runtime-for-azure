import com.azure.runtime.host.Extension;
import com.azure.runtime.host.extensions.dotnet.DotnetExtension;

/**
 * An extension providing support for .NET Projects.
 */
module com.azure.runtime.host.extensions.dotnet {
    requires transitive com.azure.runtime.host;
    requires java.logging;

    exports com.azure.runtime.host.extensions.dotnet;

    provides Extension with DotnetExtension;
}