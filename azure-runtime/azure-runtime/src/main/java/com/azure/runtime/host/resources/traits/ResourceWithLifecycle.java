package com.azure.runtime.host.resources.traits;

import com.azure.runtime.host.resources.Resource;

/**
 * All resources have a lifecycle, beginning with creation and ending with the resource being written out to the
 * Java App Host manifest. Within this lifecycle, there are times when the resource should ideally be configured, introspected,
 * and then written out. The stages of a resources lifecycle are:
 *
 * <ol>
 * <li>Resource is created - Resource constructor is called.</li>
 * <li>Resource is added to the distributed application - onResourceAdded() is called.</li>
 * <li>Prior to writing the resource to the manifest, onResourcePrecommit() is called on all resources in the
 *   order they were added to the distributed application.</li>
 * </ol>
 *
 * <p>The most important point to understand about resource lifecycles is that resources work best when they are
 * configured as early as possible, as this allows for other resources to glean more information from them. A resource
 * that keeps its cards close to its chest only hurts the resources around it!</p>
 *
 * @see Resource
 * @see IntrospectiveResource
 */
public interface ResourceWithLifecycle {

    /**
     * This method is called immediately after the resource is added to the distributed application. It is the earliest
     * time that the resource can be configured.
     */
    default void onResourceAdded() {

    }

    /**
     * This method is called immediately after the resource is removed from the distributed application.
     */
    default void onResourceRemoved() {

    }

    /**
     * Prior to writing the resource to the manifest, onResourcePrecommit() is called on all resources in the
     * order they were added to the distributed application.
     */
    default void onResourcePrecommit() {

    }
}
