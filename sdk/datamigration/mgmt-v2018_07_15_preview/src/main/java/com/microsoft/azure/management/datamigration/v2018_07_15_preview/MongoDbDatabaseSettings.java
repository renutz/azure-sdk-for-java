/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.datamigration.v2018_07_15_preview;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes how an individual MongoDB database should be migrated.
 */
public class MongoDbDatabaseSettings {
    /**
     * The collections on the source database to migrate to the target. The
     * keys are the unqualified names of the collections.
     */
    @JsonProperty(value = "collections", required = true)
    private Map<String, MongoDbCollectionSettings> collections;

    /**
     * The RUs that should be configured on a CosmosDB target, or null to use
     * the default, or 0 if throughput should not be provisioned for the
     * database. This has no effect on non-CosmosDB targets.
     */
    @JsonProperty(value = "targetRUs")
    private Integer targetRUs;

    /**
     * Get the collections on the source database to migrate to the target. The keys are the unqualified names of the collections.
     *
     * @return the collections value
     */
    public Map<String, MongoDbCollectionSettings> collections() {
        return this.collections;
    }

    /**
     * Set the collections on the source database to migrate to the target. The keys are the unqualified names of the collections.
     *
     * @param collections the collections value to set
     * @return the MongoDbDatabaseSettings object itself.
     */
    public MongoDbDatabaseSettings withCollections(Map<String, MongoDbCollectionSettings> collections) {
        this.collections = collections;
        return this;
    }

    /**
     * Get the RUs that should be configured on a CosmosDB target, or null to use the default, or 0 if throughput should not be provisioned for the database. This has no effect on non-CosmosDB targets.
     *
     * @return the targetRUs value
     */
    public Integer targetRUs() {
        return this.targetRUs;
    }

    /**
     * Set the RUs that should be configured on a CosmosDB target, or null to use the default, or 0 if throughput should not be provisioned for the database. This has no effect on non-CosmosDB targets.
     *
     * @param targetRUs the targetRUs value to set
     * @return the MongoDbDatabaseSettings object itself.
     */
    public MongoDbDatabaseSettings withTargetRUs(Integer targetRUs) {
        this.targetRUs = targetRUs;
        return this;
    }

}
