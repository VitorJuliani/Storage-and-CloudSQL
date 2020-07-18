package com.example.storageandsqlstudy.repository

import com.example.storageandsqlstudy.property.GoogleCloudStorageProperties
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import java.nio.ByteBuffer
import java.util.*

class ImageGoogleCloudStorageDao(private val storage: Storage,
                                 private val storageProperties: GoogleCloudStorageProperties) {

    fun uploadObject(data: ByteArray?): String? {
        val objectName = "${UUID.randomUUID()}"
        val blobId = BlobId.of(storageProperties.bucketName, objectName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()

        return data?.let {
            storage.create(blobInfo, it)
                    .toBuilder()
                    .setContentType(storageProperties.image.imageFormat)
                    .build()
                    .update()

            "${storageProperties.baseUrl}/${storageProperties.bucketName}/$objectName.${storageProperties.image.imageExtension}"
        }
    }

    fun updateObject(data: ByteArray?, objectName: String): String? {
        return data?.let {
            val blobId = BlobId.of(storageProperties.bucketName, objectName)
            val blob = storage.get(blobId)

            val objectWriter = blob.writer()
            objectWriter.write(ByteBuffer.wrap(data))
            objectWriter.close()

            "${storageProperties.baseUrl}/${storageProperties.bucketName}/$objectName.${storageProperties.image.imageExtension}"
        }
    }

    fun deleteObject(objectName: String): Boolean {
        return storage.delete(storageProperties.bucketName, objectName)
    }
}
