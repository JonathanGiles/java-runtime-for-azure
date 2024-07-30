// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.example.storageexplorer.service;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.stream.Stream;

/**
 * Azure Blob Storage implementation of the StorageService interface, allowing for access to Azure Blob Storage to
 * store, retrieve, and delete files.
 */
@Service
public class AzureBlobStorageService implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AzureBlobStorageService.class);

    private String blobStorageContainerName = "mycontainer";

    @Value("${ConnectionStrings__storage-explorer-blobs}")
    private String storageEndpoint;

    private BlobContainerClient blobContainerClient;

    @Override
    public void init() {
        LOGGER.info("Using Azure Blob Storage endpoint {}", storageEndpoint);
        if (blobContainerClient != null) {
            return;
        }

        boolean doInit = true;

        if ((storageEndpoint == null || storageEndpoint.isEmpty())) {
            System.err.println("Error: Please set the ConnectionStrings__storage-explorer-blobs property");
            doInit = false;
        }

        if (!doInit) {
            System.exit(-1);
            return;
        }
        BlobServiceClient blobServiceClient = null;
        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(storageEndpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        blobContainerClient = blobServiceClient.getBlobContainerClient(blobStorageContainerName);
        if (!blobContainerClient.exists()) {
            blobContainerClient = blobServiceClient.createBlobContainer(blobStorageContainerName);
        }
    }

    @Override
    public void store(final String filename, final InputStream inputStream, final long length) {
        final BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        blobClient.upload(inputStream, length);

        final String mimeType = URLConnection.guessContentTypeFromName(filename);
        blobClient.setHttpHeaders(new BlobHttpHeaders()
                .setContentType(mimeType));
    }

    @Override
    public Stream<StorageItem> listAllFiles() {
        return blobContainerClient.listBlobs().stream().map(AzureStorageItem::new);
    }

    @Override
    public StorageItem getFile(String filename) {
        return new AzureStorageItem(blobContainerClient.getBlobClient(filename));
    }

    @Override
    public boolean deleteFile(String filename) {
        long start = System.nanoTime();
        final BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        if (blobClient.exists()) {
            blobClient.delete();
            return true;
        }
        System.out.println(filename + " successfully deleted in " + (System.nanoTime() - start) + " ns");
        return false;
    }
}
