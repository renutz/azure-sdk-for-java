/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.containerregistry.v2019_06_01_preview;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.microsoft.rest.ExpandableStringEnum;

/**
 * Defines values for Variant.
 */
public final class Variant extends ExpandableStringEnum<Variant> {
    /** Static value v6 for Variant. */
    public static final Variant V6 = fromString("v6");

    /** Static value v7 for Variant. */
    public static final Variant V7 = fromString("v7");

    /** Static value v8 for Variant. */
    public static final Variant V8 = fromString("v8");

    /**
     * Creates or finds a Variant from its string representation.
     * @param name a name to look for
     * @return the corresponding Variant
     */
    @JsonCreator
    public static Variant fromString(String name) {
        return fromString(name, Variant.class);
    }

    /**
     * @return known Variant values
     */
    public static Collection<Variant> values() {
        return values(Variant.class);
    }
}
