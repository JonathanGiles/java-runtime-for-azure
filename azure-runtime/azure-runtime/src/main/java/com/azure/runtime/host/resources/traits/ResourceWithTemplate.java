package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.resources.Resource;
import com.azure.runtime.host.utils.templates.TemplateFileOutput;

import java.util.List;

/**
 * A resource that has a template associated with it. This may be one or more bicep files, microservice projects (e.g.
 * Spring Boot, Quarkus, etc.), or other types of templates. For each of the template files, we defer to Apache Velocity
 * to process the template and generate the final output. Where this output is placed is determined by the resource
 * itself - sometimes it is a temporary location (which the app host will tidy up), sometimes it will be the output
 * location specified by the app host user.
 * @param <T>
 */
public interface ResourceWithTemplate<T extends Resource<T> & ResourceWithTemplate<T>> extends ResourceTrait<T> {

    /**
     * Processes the template associated with this resource.
     * @return A list of template file outputs.
     */
    List<TemplateFileOutput> processTemplate();
}
