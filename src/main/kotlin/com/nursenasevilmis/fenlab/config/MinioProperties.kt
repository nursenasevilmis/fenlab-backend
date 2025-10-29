package com.nursenasevilmis.fenlab.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "minio")
data class MinioProperties(
    var url: String = "",
    var accessKey: String = "",
    var secretKey: String = "",
    var buckets: Buckets = Buckets()
) {
    data class Buckets(
        var videos: String = "fenlab-videos",
        var images: String = "fenlab-images",
        var pdfs: String = "fenlab-pdfs",
        var profiles: String = "fenlab-profiles"
    )
}