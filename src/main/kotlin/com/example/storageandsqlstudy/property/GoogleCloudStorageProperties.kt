package com.example.storageandsqlstudy.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "gcs")
data class GoogleCloudStorageProperties(
        val bucketName: String,
        val signUrlDurationInMinutes: Long,
        val baseUrl: String,
        val image: GoogleCloudImage
) {
    data class GoogleCloudImage(
            val defaultImageObject: String,
            val imageExtension: String,
            val imageFormat: String,
            val defaultImageUrl: String
    )
}
