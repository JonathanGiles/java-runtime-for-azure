module storage.explorer.apphost {
    requires com.azure.runtime.host;

    requires com.azure.runtime.host.extensions.azure.openai;
    requires com.azure.runtime.host.extensions.azure.storage;
    requires com.azure.runtime.host.extensions.spring;
    requires java.logging;
}