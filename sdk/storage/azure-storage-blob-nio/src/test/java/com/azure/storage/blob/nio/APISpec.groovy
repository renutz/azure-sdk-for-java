// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.blob.nio

import com.azure.core.http.HttpClient
import com.azure.core.http.ProxyOptions
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder
import com.azure.core.http.policy.HttpPipelinePolicy
import com.azure.core.test.InterceptorManager
import com.azure.core.test.TestMode
import com.azure.core.test.utils.TestResourceNamer
import com.azure.core.util.Configuration
import com.azure.core.util.CoreUtils
import com.azure.core.util.FluxUtil
import com.azure.core.util.logging.ClientLogger
import com.azure.storage.blob.BlobAsyncClient
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobClientBuilder
import com.azure.storage.blob.BlobContainerAsyncClient
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.blob.BlobServiceAsyncClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.models.BlobContainerItem
import com.azure.storage.blob.models.ListBlobContainersOptions
import com.azure.storage.common.StorageSharedKeyCredential
import com.azure.storage.common.implementation.Constants
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Timeout

import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.charset.StandardCharsets
import java.nio.file.attribute.FileAttribute
import java.time.Duration
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

@Timeout(value = 5, unit = TimeUnit.MINUTES)
class APISpec extends Specification {
    @Shared
    ClientLogger logger = new ClientLogger(APISpec.class)

    Integer entityNo = 0 // Used to generate stable container names for recording tests requiring multiple containers.

    // both sync and async clients point to same container
    @Shared
    BlobContainerClient cc

    @Shared
    BlobContainerAsyncClient ccAsync

    // Fields used for conveniently creating blobs with data.
    static final String defaultText = "default"

    public static final ByteBuffer defaultData = ByteBuffer.wrap(defaultText.getBytes(StandardCharsets.UTF_8))

    static final Supplier<InputStream> defaultInputStream = new Supplier<InputStream>() {
        @Override
        InputStream get() {
            return new ByteArrayInputStream(defaultText.getBytes(StandardCharsets.UTF_8))
        }
    }

    static int defaultDataSize = defaultData.remaining()

    protected static final Flux<ByteBuffer> defaultFlux = Flux.just(defaultData).map { buffer -> buffer.duplicate() }

    // Prefixes for blobs and containers
    String containerPrefix = "jtc" // java test container

    String blobPrefix = "javablob"

    public static final String defaultEndpointTemplate = "https://%s.blob.core.windows.net/"

    static def AZURE_TEST_MODE = "AZURE_TEST_MODE"
    static def PRIMARY_STORAGE = "PRIMARY_STORAGE_"
    static def SECONDARY_STORAGE = "SECONDARY_STORAGE_"

    protected static StorageSharedKeyCredential primaryCredential
    static StorageSharedKeyCredential alternateCredential
    static TestMode testMode

    BlobServiceClient primaryBlobServiceClient
    BlobServiceAsyncClient primaryBlobServiceAsyncClient
    BlobServiceClient alternateBlobServiceClient

    InterceptorManager interceptorManager
    boolean recordLiveMode
    protected TestResourceNamer resourceNamer
    protected String testName
    String containerName

    def setupSpec() {
        testMode = setupTestMode()
        primaryCredential = getCredential(PRIMARY_STORAGE)
        alternateCredential = getCredential(SECONDARY_STORAGE)
        // The property is to limit flapMap buffer size of concurrency
        // in case the upload or download open too many connections.
        System.setProperty("reactor.bufferSize.x", "16")
        System.setProperty("reactor.bufferSize.small", "100")
        System.out.println(String.format("--------%s---------", testMode))
    }

    def setup() {
        String fullTestName = specificationContext.getCurrentIteration().getName().replace(' ', '').toLowerCase()
        String className = specificationContext.getCurrentSpec().getName()
        int iterationIndex = fullTestName.lastIndexOf("[")
        int substringIndex = (int) Math.min((iterationIndex != -1) ? iterationIndex : fullTestName.length(), 50)
        this.testName = fullTestName.substring(0, substringIndex)
        this.interceptorManager = new InterceptorManager(className + fullTestName, testMode)
        this.resourceNamer = new TestResourceNamer(className + testName, testMode, interceptorManager.getRecordedData())

        // If the test doesn't have the Requires tag record it in live mode.
        recordLiveMode = specificationContext.getCurrentIteration().getDescription().getAnnotation(Requires.class) != null

        primaryBlobServiceClient = setClient(primaryCredential)
        primaryBlobServiceAsyncClient = getServiceAsyncClient(primaryCredential)
        alternateBlobServiceClient = setClient(alternateCredential)

        containerName = generateContainerName()
        cc = primaryBlobServiceClient.getBlobContainerClient(containerName)
        ccAsync = primaryBlobServiceAsyncClient.getBlobContainerAsyncClient(containerName)
    }

    def cleanup() {
        def options = new ListBlobContainersOptions().setPrefix(containerPrefix + testName)
        for (BlobContainerItem container : primaryBlobServiceClient.listBlobContainers(options, Duration.ofSeconds(120))) {
            BlobContainerClient containerClient = primaryBlobServiceClient.getBlobContainerClient(container.getName())

            containerClient.delete()
        }

        interceptorManager.close()
    }

    //TODO: Should this go in core.
    static Mono<ByteBuffer> collectBytesInBuffer(Flux<ByteBuffer> content) {
        return FluxUtil.collectBytesInByteBufferStream(content).map { bytes -> ByteBuffer.wrap(bytes) }
    }

    static TestMode setupTestMode() {
        String testMode = Configuration.getGlobalConfiguration().get(AZURE_TEST_MODE)

        if (testMode != null) {
            try {
                return TestMode.valueOf(testMode.toUpperCase(Locale.US))
            } catch (IllegalArgumentException ignore) {
                return TestMode.PLAYBACK
            }
        }

        return TestMode.PLAYBACK
    }

    static boolean liveMode() {
        return setupTestMode() == TestMode.LIVE
    }

    String getAccountKey(String accountType) {
        if (testMode == TestMode.RECORD || testMode == TestMode.LIVE) {
            return Configuration.getGlobalConfiguration().get(accountType + "ACCOUNT_KEY")
        } else {
            accountKey = "astorageaccountkey"
        }
    }

    String getAccountName(String accountType) {
        if (testMode == TestMode.RECORD || testMode == TestMode.LIVE) {
            return Configuration.getGlobalConfiguration().get(accountType + "ACCOUNT_NAME")
        } else {
            accountName = "azstoragesdkaccount"
        }
    }

    private StorageSharedKeyCredential getCredential(String accountType) {
        String accountName = getAccountName(accountType)
        String accountKey = getAccountKey(accountType)

        if (accountName == null || accountKey == null) {
            logger.warning("Account name or key for the {} account was null. Test's requiring these credentials will fail.", accountType)
            return null
        }

        return new StorageSharedKeyCredential(accountName, accountKey)
    }

    BlobServiceClient setClient(StorageSharedKeyCredential credential) {
        try {
            return getServiceClient(credential)
        } catch (Exception ignore) {
            return null
        }
    }

    BlobServiceClient getServiceClient(String endpoint) {
        return getServiceClient(null, endpoint, null)
    }

    BlobServiceClient getServiceClient(StorageSharedKeyCredential credential) {
        return getServiceClient(credential, String.format(defaultEndpointTemplate, credential.getAccountName()), null)
    }

    BlobServiceClient getServiceClient(StorageSharedKeyCredential credential, String endpoint) {
        return getServiceClient(credential, endpoint, null)
    }

    BlobServiceClient getServiceClient(StorageSharedKeyCredential credential, String endpoint,
        HttpPipelinePolicy... policies) {
        return getServiceClientBuilder(credential, endpoint, policies).buildClient()
    }

    BlobServiceClient getServiceClient(String sasToken, String endpoint) {
        return getServiceClientBuilder(null, endpoint, null).sasToken(sasToken).buildClient()
    }

    BlobServiceAsyncClient getServiceAsyncClient(StorageSharedKeyCredential credential) {
        return getServiceClientBuilder(credential, String.format(defaultEndpointTemplate, credential.getAccountName()))
            .buildAsyncClient()
    }

    BlobServiceClientBuilder getServiceClientBuilder(StorageSharedKeyCredential credential, String endpoint,
        HttpPipelinePolicy... policies) {
        BlobServiceClientBuilder builder = new BlobServiceClientBuilder()
            .endpoint(endpoint)
            .httpClient(getHttpClient())

        for (HttpPipelinePolicy policy : policies) {
            builder.addPolicy(policy)
        }

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        if (credential != null) {
            builder.credential(credential)
        }

        return builder
    }

    BlobContainerClient getContainerClient(String sasToken, String endpoint) {
        getContainerClientBuilder(endpoint).sasToken(sasToken).buildClient()
    }

    BlobContainerClientBuilder getContainerClientBuilder(String endpoint) {
        BlobContainerClientBuilder builder = new BlobContainerClientBuilder()
            .endpoint(endpoint)
            .httpClient(getHttpClient())

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        return builder
    }

    BlobAsyncClient getBlobAsyncClient(StorageSharedKeyCredential credential, String endpoint, String blobName) {
        BlobClientBuilder builder = new BlobClientBuilder()
            .endpoint(endpoint)
            .blobName(blobName)
            .httpClient(getHttpClient())

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        builder.credential(credential).buildAsyncClient()
    }

    BlobClient getBlobClient(String sasToken, String endpoint, String blobName) {
        return getBlobClient(sasToken, endpoint, blobName, null)
    }

    BlobClient getBlobClient(String sasToken, String endpoint, String blobName, String snapshotId) {
        BlobClientBuilder builder = new BlobClientBuilder()
            .endpoint(endpoint)
            .blobName(blobName)
            .snapshot(snapshotId)
            .httpClient(getHttpClient())

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        return builder.sasToken(sasToken).buildClient()
    }

    BlobClient getBlobClient(StorageSharedKeyCredential credential, String endpoint, HttpPipelinePolicy... policies) {
        BlobClientBuilder builder = new BlobClientBuilder()
            .endpoint(endpoint)
            .httpClient(getHttpClient())

        for (HttpPipelinePolicy policy : policies) {
            builder.addPolicy(policy)
        }

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        return builder.credential(credential).buildClient()
    }

    BlobClient getBlobClient(StorageSharedKeyCredential credential, String endpoint, String blobName) {
        BlobClientBuilder builder = new BlobClientBuilder()
            .endpoint(endpoint)
            .blobName(blobName)
            .httpClient(getHttpClient())

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        return builder.credential(credential).buildClient()
    }

    BlobClient getBlobClient(String endpoint, String sasToken) {
        BlobClientBuilder builder = new BlobClientBuilder()
            .endpoint(endpoint)
            .httpClient(getHttpClient())

        if (!CoreUtils.isNullOrEmpty(sasToken)) {
            builder.sasToken(sasToken)
        }

        if (testMode == TestMode.RECORD) {
            builder.addPolicy(interceptorManager.getRecordPolicy())
        }

        return builder.buildClient()
    }

    HttpClient getHttpClient() {
        NettyAsyncHttpClientBuilder builder = new NettyAsyncHttpClientBuilder()
        if (testMode == TestMode.RECORD || testMode == TestMode.LIVE) {
            builder.wiretap(true)

            if (Boolean.parseBoolean(Configuration.getGlobalConfiguration().get("AZURE_TEST_DEBUGGING"))) {
                builder.proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("localhost", 8888)))
            }

            return builder.build()
        } else {
            return interceptorManager.getPlaybackClient()
        }
    }

    Map<String, Object> initializeConfigMap() {
        def config = [:]
        config[AzureFileSystem.AZURE_STORAGE_HTTP_CLIENT] = getHttpClient()
        config[AzureFileSystem.AZURE_STORAGE_USE_HTTPS] = defaultEndpointTemplate.startsWith("https")
        return config as Map<String, Object>
    }

    def getAccountUri() {
        return new URI("azb://?account=" + getAccountName(PRIMARY_STORAGE))
    }

    def generateContainerName() {
        generateResourceName(containerPrefix, entityNo++)
    }

    def generateBlobName() {
        generateResourceName(blobPrefix, entityNo++)
    }

    private String generateResourceName(String prefix, int entityNo) {
        return resourceNamer.randomName(prefix + testName + entityNo, 63)
    }

    String getConfigValue(String value) {
        return resourceNamer.recordValueFromConfig(value)
    }

    String getRandomUUID() {
        return resourceNamer.randomUuid()
    }

    String getBlockID() {
        return Base64.encoder.encodeToString(resourceNamer.randomUuid().getBytes(StandardCharsets.UTF_8))
    }

    def createFS(Map<String,String> config) {
        config[AzureFileSystem.AZURE_STORAGE_FILE_STORES] = generateContainerName() + "," + generateContainerName()
        config[AzureFileSystem.AZURE_STORAGE_ACCOUNT_KEY] = getAccountKey(PRIMARY_STORAGE)

        return new AzureFileSystem(new AzureFileSystemProvider(), getAccountName(PRIMARY_STORAGE), config)
    }

    OffsetDateTime getUTCNow() {
        return resourceNamer.now()
    }

    byte[] getRandomByteArray(int size) {
        long seed = UUID.fromString(resourceNamer.randomUuid()).getMostSignificantBits() & Long.MAX_VALUE
        Random rand = new Random(seed)
        byte[] data = new byte[size]
        rand.nextBytes(data)
        return data
    }

    /*
     Size must be an int because ByteBuffer sizes can only be an int. Long is not supported.
     */
    ByteBuffer getRandomData(int size) {
        return ByteBuffer.wrap(getRandomByteArray(size))
    }

    /*
    We only allow int because anything larger than 2GB (which would require a long) is left to stress/perf.
     */
    File getRandomFile(int size) {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".txt")
        file.deleteOnExit()
        FileOutputStream fos = new FileOutputStream(file)

        if (size > Constants.MB) {
            for (def i = 0; i < size / Constants.MB; i++) {
                def dataSize = Math.min(Constants.MB, size - i * Constants.MB)
                fos.write(getRandomByteArray(dataSize))
            }
        } else {
            fos.write(getRandomByteArray(size))
        }

        fos.close()
        return file
    }

    /**
     * Compares two files for having equivalent content.
     *
     * @param file1 File used to upload data to the service
     * @param file2 File used to download data from the service
     * @param offset Write offset from the upload file
     * @param count Size of the download from the service
     * @return Whether the files have equivalent content based on offset and read count
     */
    def compareFiles(File file1, File file2, long offset, long count) {
        def pos = 0L
        def readBuffer = 8 * Constants.KB
        def fileChannel1 = AsynchronousFileChannel.open(file1.toPath())
        def fileChannel2 = AsynchronousFileChannel.open(file2.toPath())

        while (pos < count) {
            def bufferSize = (int) Math.min(readBuffer, count - pos)
            def buffer1 = ByteBuffer.allocate(bufferSize)
            def buffer2 = ByteBuffer.allocate(bufferSize)

            def readCount1 = fileChannel1.read(buffer1, offset + pos).get()
            def readCount2 = fileChannel2.read(buffer2, pos).get()

            if (readCount1 != readCount2 || buffer1 != buffer2) {
                return false
            }

            pos += bufferSize
        }

        def verificationRead = fileChannel2.read(ByteBuffer.allocate(1), pos).get()

        fileChannel1.close()
        fileChannel2.close()

        return pos == count && verificationRead == -1
    }

    // Only sleep if test is running in live mode
    def sleepIfRecord(long milliseconds) {
        if (testMode != TestMode.PLAYBACK) {
            sleep(milliseconds)
        }
    }

    def rootToContainer(String root) {
        return root.substring(0, root.length() - 1)
    }

    static class TestFileAttribute<T> implements  FileAttribute<T> {
        String name
        T value

        TestFileAttribute(String name, T value) {
            this.name = name
            this.value = value
        }

        @Override
        String name() {
            return this.name
        }

        @Override
        T value() {
            return this.value
        }
    }
}
