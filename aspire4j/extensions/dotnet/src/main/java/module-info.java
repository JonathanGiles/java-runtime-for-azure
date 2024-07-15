import com.microsoft.aspire.extensions.dotnet.DotnetExtension;

/**
 * An Aspire extension providing support for .NET Projects.
 */
module com.microsoft.aspire.extensions.dotnet {
    requires transitive com.microsoft.aspire;
    requires java.logging;

    exports com.microsoft.aspire.extensions.dotnet;

    provides com.microsoft.aspire.Extension with DotnetExtension;
}