ARG BASE_IMAGE
FROM ${BASE_IMAGE}
RUN echo "Base image is ${BASE_IMAGE}"
MAINTAINER microsoft.com
USER root
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
# Install any necessary tools (if not already available in the base image)
RUN apt-get update && apt-get install -y \
    sed \
    && rm -rf /var/lib/apt/lists/* \
USER cnb
RUN API_VERSION=$(sed -n '/^[[:space:]]*api = "[^"]*"/{s/^[[:space:]]*api = "\([^"]*\)".*/\1/; /^[^[:space:]]*$/ p; q;}' /layers/config/metadata.toml) \
    && echo "API_VERSION=$API_VERSION" \
    # Create or append content to metadata.toml using echo and redirection \
    && { \
        echo ''; \
        echo '[[buildpacks]]'; \
        echo '  id="java-runtime-for-azure-otel"'; \
        echo '  version = "0.0.1"'; \
        echo "  api = \"$API_VERSION\""; \
        } >> /layers/config/metadata.toml
RUN mkdir -p /layers/java-runtime-for-azure-otel/jre/env.launch
RUN cat /layers/config/metadata.toml
COPY JAVA_TOOL_OPTIONS.delim /layers/java-runtime-for-azure-otel/jre/env.launch/JAVA_TOOL_OPTIONS.delim
COPY JAVA_TOOL_OPTIONS.append /layers/java-runtime-for-azure-otel/jre/env.launch/JAVA_TOOL_OPTIONS.append