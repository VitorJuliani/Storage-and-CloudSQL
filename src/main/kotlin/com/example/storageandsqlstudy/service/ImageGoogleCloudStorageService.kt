package com.example.storageandsqlstudy.service

import com.example.storageandsqlstudy.property.GoogleCloudStorageProperties
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import java.net.URL
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit

class ImageGoogleCloudStorageService(private val storage: Storage,
                                     private val storageProperties: GoogleCloudStorageProperties) : StorageService {

    override fun uploadObject(data: ByteArray?): String {
        return data?.let {
            val objectName = "${UUID.randomUUID()}"
            val blobId = BlobId.of(storageProperties.bucketName, objectName)
            val blobInfo = BlobInfo.newBuilder(blobId).build()

            try {
                storage.create(blobInfo, it)
                        .toBuilder()
                        .setContentType(storageProperties.image.imageFormat)
                        .build()
                        .update()
                objectUrl(objectName)
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }

        } ?: storageProperties.image.defaultImageUrl
    }

    override fun updateObject(data: ByteArray?, objectUrl: String): String {
        val objectName = getObjectNameFromUrl(objectUrl)

        return if (objectName == storageProperties.image.defaultImageObject) {
            uploadObject(data)
        } else {
            val blobId = BlobId.of(storageProperties.bucketName, objectName)
            val blob = storage.get(blobId)
            val objectWriter = blob.writer()

            data?.let {
                try {
                    objectWriter.write(ByteBuffer.wrap(it))
                    objectWriter.close()
                    objectUrl(objectName)
                } catch (e: Exception) {
                    throw Exception("Image update error")
                }
            } ?: deleteObject(objectUrl)
            storageProperties.image.defaultImageUrl
        }
    }

    override fun deleteObject(objectUrl: String): Boolean {
        val objectName = getObjectNameFromUrl(objectUrl)

        return if (objectName != storageProperties.image.defaultImageObject) {
            try {
                storage.delete(storageProperties.bucketName, objectName)
            } catch (e: Exception) {
                throw Exception(e.localizedMessage)
            }
        } else {
            true
        }
    }

    override fun generateSignUrl(objectName: String): String {
        val blobId = BlobId.of(storageProperties.bucketName, objectName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()

        try {
            return storage.signUrl(blobInfo, storageProperties.signUrlDurationInMinutes, TimeUnit.MINUTES).toExternalForm()
        } catch (e: Exception) {
            throw Exception(e.localizedMessage)
        }
    }

    private fun getObjectNameFromUrl(objectUrl: String): String {
        return URL(objectUrl).path
                .removePrefix("/${storageProperties.bucketName}/")
                .removeSuffix(".${storageProperties.image.imageExtension}")
    }

    private fun objectUrl(objectName: String ): String {
        return "${storageProperties.baseUrl}/${storageProperties.bucketName}/$objectName.${storageProperties.image.imageExtension}"
    }
}
