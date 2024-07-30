package com.azure.runtime.host.utils.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify that the field represents a path that should be output as a relative path.
 * This is necessary because user-facing API accepts paths that are expected to be relative to the azd execution
 * directory, and internally we need to transform these paths to be relative to the output directory, so that the paths
 * are correct from the perspective of the generated aspire-manifest.json file.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RelativePath {}