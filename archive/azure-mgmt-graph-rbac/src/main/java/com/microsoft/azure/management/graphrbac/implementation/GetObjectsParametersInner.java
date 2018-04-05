/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.graphrbac.implementation;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request parameters for the GetObjectsByObjectIds API.
 */
public class GetObjectsParametersInner {
    /**
     * The requested object IDs.
     */
    @JsonProperty(value = "objectIds")
    private List<String> objectIds;

    /**
     * The requested object types.
     */
    @JsonProperty(value = "types")
    private List<String> types;

    /**
     * If true, also searches for object IDs in the partner tenant.
     */
    @JsonProperty(value = "includeDirectoryObjectReferences", required = true)
    private boolean includeDirectoryObjectReferences;

    /**
     * Get the objectIds value.
     *
     * @return the objectIds value
     */
    public List<String> objectIds() {
        return this.objectIds;
    }

    /**
     * Set the objectIds value.
     *
     * @param objectIds the objectIds value to set
     * @return the GetObjectsParametersInner object itself.
     */
    public GetObjectsParametersInner withObjectIds(List<String> objectIds) {
        this.objectIds = objectIds;
        return this;
    }

    /**
     * Get the types value.
     *
     * @return the types value
     */
    public List<String> types() {
        return this.types;
    }

    /**
     * Set the types value.
     *
     * @param types the types value to set
     * @return the GetObjectsParametersInner object itself.
     */
    public GetObjectsParametersInner withTypes(List<String> types) {
        this.types = types;
        return this;
    }

    /**
     * Get the includeDirectoryObjectReferences value.
     *
     * @return the includeDirectoryObjectReferences value
     */
    public boolean includeDirectoryObjectReferences() {
        return this.includeDirectoryObjectReferences;
    }

    /**
     * Set the includeDirectoryObjectReferences value.
     *
     * @param includeDirectoryObjectReferences the includeDirectoryObjectReferences value to set
     * @return the GetObjectsParametersInner object itself.
     */
    public GetObjectsParametersInner withIncludeDirectoryObjectReferences(boolean includeDirectoryObjectReferences) {
        this.includeDirectoryObjectReferences = includeDirectoryObjectReferences;
        return this;
    }

}