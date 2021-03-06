/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.devtestlabs.v2018_09_15.implementation;

import com.microsoft.azure.arm.resources.models.implementation.GroupableResourceCoreImpl;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.Lab;
import rx.Observable;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.LabFragment;
import java.util.List;
import java.util.Map;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.StorageType;
import org.joda.time.DateTime;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.PremiumDataDisk;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.EnvironmentPermission;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.LabAnnouncementProperties;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.LabSupportProperties;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.LabAnnouncementPropertiesFragment;
import com.microsoft.azure.management.devtestlabs.v2018_09_15.LabSupportPropertiesFragment;
import rx.functions.Func1;

class LabImpl extends GroupableResourceCoreImpl<Lab, LabInner, LabImpl, DevTestLabsManager> implements Lab, Lab.Definition, Lab.Update {
    private LabFragment updateParameter;
    LabImpl(String name, LabInner inner, DevTestLabsManager manager) {
        super(name, inner, manager);
        this.updateParameter = new LabFragment();
    }

    @Override
    public Observable<Lab> createResourceAsync() {
        LabsInner client = this.manager().inner().labs();
        return client.createOrUpdateAsync(this.resourceGroupName(), this.name(), this.inner())
            .map(new Func1<LabInner, LabInner>() {
               @Override
               public LabInner call(LabInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    public Observable<Lab> updateResourceAsync() {
        LabsInner client = this.manager().inner().labs();
        return client.updateAsync(this.resourceGroupName(), this.name(), this.updateParameter)
            .map(new Func1<LabInner, LabInner>() {
               @Override
               public LabInner call(LabInner resource) {
                   resetCreateUpdateParameters();
                   return resource;
               }
            })
            .map(innerToFluentMap(this));
    }

    @Override
    protected Observable<LabInner> getInnerAsync() {
        LabsInner client = this.manager().inner().labs();
        return client.getByResourceGroupAsync(this.resourceGroupName(), this.name());
    }

    @Override
    public boolean isInCreateMode() {
        return this.inner().id() == null;
    }

    private void resetCreateUpdateParameters() {
        this.updateParameter = new LabFragment();
    }

    @Override
    public LabAnnouncementProperties announcement() {
        return this.inner().announcement();
    }

    @Override
    public String artifactsStorageAccount() {
        return this.inner().artifactsStorageAccount();
    }

    @Override
    public DateTime createdDate() {
        return this.inner().createdDate();
    }

    @Override
    public String defaultPremiumStorageAccount() {
        return this.inner().defaultPremiumStorageAccount();
    }

    @Override
    public String defaultStorageAccount() {
        return this.inner().defaultStorageAccount();
    }

    @Override
    public EnvironmentPermission environmentPermission() {
        return this.inner().environmentPermission();
    }

    @Override
    public Map<String, String> extendedProperties() {
        return this.inner().extendedProperties();
    }

    @Override
    public StorageType labStorageType() {
        return this.inner().labStorageType();
    }

    @Override
    public String loadBalancerId() {
        return this.inner().loadBalancerId();
    }

    @Override
    public List<String> mandatoryArtifactsResourceIdsLinux() {
        return this.inner().mandatoryArtifactsResourceIdsLinux();
    }

    @Override
    public List<String> mandatoryArtifactsResourceIdsWindows() {
        return this.inner().mandatoryArtifactsResourceIdsWindows();
    }

    @Override
    public String networkSecurityGroupId() {
        return this.inner().networkSecurityGroupId();
    }

    @Override
    public PremiumDataDisk premiumDataDisks() {
        return this.inner().premiumDataDisks();
    }

    @Override
    public String premiumDataDiskStorageAccount() {
        return this.inner().premiumDataDiskStorageAccount();
    }

    @Override
    public String provisioningState() {
        return this.inner().provisioningState();
    }

    @Override
    public String publicIpId() {
        return this.inner().publicIpId();
    }

    @Override
    public LabSupportProperties support() {
        return this.inner().support();
    }

    @Override
    public String uniqueIdentifier() {
        return this.inner().uniqueIdentifier();
    }

    @Override
    public String vaultName() {
        return this.inner().vaultName();
    }

    @Override
    public String vmCreationResourceGroup() {
        return this.inner().vmCreationResourceGroup();
    }

    @Override
    public LabImpl withAnnouncement(LabAnnouncementProperties announcement) {
        this.inner().withAnnouncement(announcement);
        return this;
    }

    @Override
    public LabImpl withSupport(LabSupportProperties support) {
        this.inner().withSupport(support);
        return this;
    }

    @Override
    public LabImpl withAnnouncement(LabAnnouncementPropertiesFragment announcement) {
        this.updateParameter.withAnnouncement(announcement);
        return this;
    }

    @Override
    public LabImpl withSupport(LabSupportPropertiesFragment support) {
        this.updateParameter.withSupport(support);
        return this;
    }

    @Override
    public LabImpl withEnvironmentPermission(EnvironmentPermission environmentPermission) {
        if (isInCreateMode()) {
            this.inner().withEnvironmentPermission(environmentPermission);
        } else {
            this.updateParameter.withEnvironmentPermission(environmentPermission);
        }
        return this;
    }

    @Override
    public LabImpl withExtendedProperties(Map<String, String> extendedProperties) {
        if (isInCreateMode()) {
            this.inner().withExtendedProperties(extendedProperties);
        } else {
            this.updateParameter.withExtendedProperties(extendedProperties);
        }
        return this;
    }

    @Override
    public LabImpl withLabStorageType(StorageType labStorageType) {
        if (isInCreateMode()) {
            this.inner().withLabStorageType(labStorageType);
        } else {
            this.updateParameter.withLabStorageType(labStorageType);
        }
        return this;
    }

    @Override
    public LabImpl withMandatoryArtifactsResourceIdsLinux(List<String> mandatoryArtifactsResourceIdsLinux) {
        if (isInCreateMode()) {
            this.inner().withMandatoryArtifactsResourceIdsLinux(mandatoryArtifactsResourceIdsLinux);
        } else {
            this.updateParameter.withMandatoryArtifactsResourceIdsLinux(mandatoryArtifactsResourceIdsLinux);
        }
        return this;
    }

    @Override
    public LabImpl withMandatoryArtifactsResourceIdsWindows(List<String> mandatoryArtifactsResourceIdsWindows) {
        if (isInCreateMode()) {
            this.inner().withMandatoryArtifactsResourceIdsWindows(mandatoryArtifactsResourceIdsWindows);
        } else {
            this.updateParameter.withMandatoryArtifactsResourceIdsWindows(mandatoryArtifactsResourceIdsWindows);
        }
        return this;
    }

    @Override
    public LabImpl withPremiumDataDisks(PremiumDataDisk premiumDataDisks) {
        if (isInCreateMode()) {
            this.inner().withPremiumDataDisks(premiumDataDisks);
        } else {
            this.updateParameter.withPremiumDataDisks(premiumDataDisks);
        }
        return this;
    }

}
