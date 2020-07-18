package com.example.storageandsqlstudy.configuration

import com.example.storageandsqlstudy.property.GoogleCloudStorageProperties
import com.example.storageandsqlstudy.repository.ImageGoogleCloudStorageDao
import com.google.cloud.storage.Storage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleCloudStorageDaoConfig {

    @Bean
    fun imageGoogleCloudStorageDao(storage: Storage, googleCloudStorageProperties: GoogleCloudStorageProperties): ImageGoogleCloudStorageDao {
        return ImageGoogleCloudStorageDao(storage, googleCloudStorageProperties)
    }
}
