/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.recoveryservices.backup.v2017_07_01.implementation;

import retrofit2.Retrofit;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.management.recoveryservices.backup.v2017_07_01.FeatureSupportRequest;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import com.microsoft.rest.Validator;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in FeatureSupports.
 */
public class FeatureSupportsInner {
    /** The Retrofit service to perform REST calls. */
    private FeatureSupportsService service;
    /** The service client containing this operation class. */
    private RecoveryServicesBackupClientImpl client;

    /**
     * Initializes an instance of FeatureSupportsInner.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public FeatureSupportsInner(Retrofit retrofit, RecoveryServicesBackupClientImpl client) {
        this.service = retrofit.create(FeatureSupportsService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for FeatureSupports to be
     * used by Retrofit to perform actually REST calls.
     */
    interface FeatureSupportsService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.recoveryservices.backup.v2017_07_01.FeatureSupports validate" })
        @POST("Subscriptions/{subscriptionId}/providers/Microsoft.RecoveryServices/locations/{azureRegion}/backupValidateFeatures")
        Observable<Response<ResponseBody>> validate(@Path("azureRegion") String azureRegion, @Path("subscriptionId") String subscriptionId, @Query("api-version") String apiVersion, @Body FeatureSupportRequest parameters, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * It will validate if given feature with resource properties is supported in service.
     *
     * @param azureRegion Azure region to hit Api
     * @param parameters Feature support request object
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the AzureVMResourceFeatureSupportResponseInner object if successful.
     */
    public AzureVMResourceFeatureSupportResponseInner validate(String azureRegion, FeatureSupportRequest parameters) {
        return validateWithServiceResponseAsync(azureRegion, parameters).toBlocking().single().body();
    }

    /**
     * It will validate if given feature with resource properties is supported in service.
     *
     * @param azureRegion Azure region to hit Api
     * @param parameters Feature support request object
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<AzureVMResourceFeatureSupportResponseInner> validateAsync(String azureRegion, FeatureSupportRequest parameters, final ServiceCallback<AzureVMResourceFeatureSupportResponseInner> serviceCallback) {
        return ServiceFuture.fromResponse(validateWithServiceResponseAsync(azureRegion, parameters), serviceCallback);
    }

    /**
     * It will validate if given feature with resource properties is supported in service.
     *
     * @param azureRegion Azure region to hit Api
     * @param parameters Feature support request object
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the AzureVMResourceFeatureSupportResponseInner object
     */
    public Observable<AzureVMResourceFeatureSupportResponseInner> validateAsync(String azureRegion, FeatureSupportRequest parameters) {
        return validateWithServiceResponseAsync(azureRegion, parameters).map(new Func1<ServiceResponse<AzureVMResourceFeatureSupportResponseInner>, AzureVMResourceFeatureSupportResponseInner>() {
            @Override
            public AzureVMResourceFeatureSupportResponseInner call(ServiceResponse<AzureVMResourceFeatureSupportResponseInner> response) {
                return response.body();
            }
        });
    }

    /**
     * It will validate if given feature with resource properties is supported in service.
     *
     * @param azureRegion Azure region to hit Api
     * @param parameters Feature support request object
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the AzureVMResourceFeatureSupportResponseInner object
     */
    public Observable<ServiceResponse<AzureVMResourceFeatureSupportResponseInner>> validateWithServiceResponseAsync(String azureRegion, FeatureSupportRequest parameters) {
        if (azureRegion == null) {
            throw new IllegalArgumentException("Parameter azureRegion is required and cannot be null.");
        }
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        if (parameters == null) {
            throw new IllegalArgumentException("Parameter parameters is required and cannot be null.");
        }
        Validator.validate(parameters);
        final String apiVersion = "2017-07-01";
        return service.validate(azureRegion, this.client.subscriptionId(), apiVersion, parameters, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<AzureVMResourceFeatureSupportResponseInner>>>() {
                @Override
                public Observable<ServiceResponse<AzureVMResourceFeatureSupportResponseInner>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<AzureVMResourceFeatureSupportResponseInner> clientResponse = validateDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<AzureVMResourceFeatureSupportResponseInner> validateDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<AzureVMResourceFeatureSupportResponseInner, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<AzureVMResourceFeatureSupportResponseInner>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}