import com.microsoft.aspire.extensions.dotnet.DotnetExtension;

module com.microsoft.aspire.extensions.dotnet {
    requires transitive com.microsoft.aspire;
    requires java.logging;

    exports com.microsoft.aspire.extensions.dotnet;

    provides com.microsoft.aspire.Extension with DotnetExtension;
}