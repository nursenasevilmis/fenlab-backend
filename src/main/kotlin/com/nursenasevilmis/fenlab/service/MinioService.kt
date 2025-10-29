package com.nursenasevilmis.fenlab.service
import com.nursenasevilmis.fenlab.config.MinioProperties
import io.minio.*
import io.minio.http.Method
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class MinioService(
    private val minioClient: MinioClient,
    private val minioProperties: MinioProperties
) {


    private fun createBucketsIfNotExists() {
        val buckets = listOf(
            minioProperties.buckets.videos,
            minioProperties.buckets.images,
            minioProperties.buckets.pdfs,
            minioProperties.buckets.profiles
        )

        buckets.forEach { bucketName ->
            val found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            )
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                )
                // Bucket'ı public read yapmak isterseniz policy ekleyin
                setBucketPolicy(bucketName)
            }
        }
    }

    private fun setBucketPolicy(bucketName: String) {
        val policy = """
            {
              "Version": "2012-10-17",
              "Statement": [
                {
                  "Effect": "Allow",
                  "Principal": {"AWS": "*"},
                  "Action": ["s3:GetObject"],
                  "Resource": ["arn:aws:s3:::$bucketName/*"]
                }
              ]
            }
        """.trimIndent()

        minioClient.setBucketPolicy(
            SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(policy)
                .build()
        )
    }

    /**
     * Video yükleme
     */
    fun uploadVideo(file: MultipartFile): String {
        val fileName = generateFileName(file.originalFilename ?: "video", "mp4")
        return uploadFile(
            file.inputStream,
            fileName,
            file.contentType ?: "video/mp4",
            minioProperties.buckets.videos
        )
    }

    /**
     * Resim yükleme
     */
    fun uploadImage(file: MultipartFile): String {
        val fileName = generateFileName(file.originalFilename ?: "image", "jpg")
        return uploadFile(
            file.inputStream,
            fileName,
            file.contentType ?: "image/jpeg",
            minioProperties.buckets.images
        )
    }

    /**
     * Profil resmi yükleme
     */
    fun uploadProfileImage(file: MultipartFile): String {
        val fileName = generateFileName(file.originalFilename ?: "profile", "jpg")
        return uploadFile(
            file.inputStream,
            fileName,
            file.contentType ?: "image/jpeg",
            minioProperties.buckets.profiles
        )
    }

    /**
     * PDF yükleme
     */
    fun uploadPdf(inputStream: InputStream, fileName: String): String {
        val pdfFileName = generateFileName(fileName, "pdf")
        return uploadFile(
            inputStream,
            pdfFileName,
            "application/pdf",
            minioProperties.buckets.pdfs
        )
    }

    /**
     * Genel dosya yükleme
     */
    private fun uploadFile(
        inputStream: InputStream,
        fileName: String,
        contentType: String,
        bucketName: String
    ): String {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .stream(inputStream, inputStream.available().toLong(), -1)
                    .contentType(contentType)
                    .build()
            )
            return getFileUrl(bucketName, fileName)
        } catch (e: Exception) {
            throw RuntimeException("Dosya yüklenirken hata oluştu: ${e.message}", e)
        }
    }

    /**
     * Dosya URL'i alma
     */
    fun getFileUrl(bucketName: String, fileName: String): String {
        return "${minioProperties.url}/$bucketName/$fileName"
    }

    /**
     * Geçici URL oluşturma (presigned URL)
     */
    fun getPresignedUrl(bucketName: String, fileName: String, expiryMinutes: Int = 60): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .`object`(fileName)
                .expiry(expiryMinutes, TimeUnit.MINUTES)
                .build()
        )
    }

    /**
     * Dosya silme
     */
    fun deleteFile(bucketName: String, fileName: String) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`(fileName)
                    .build()
            )
        } catch (e: Exception) {
            throw RuntimeException("Dosya silinirken hata oluştu: ${e.message}", e)
        }
    }

    /**
     * Video silme
     */
    fun deleteVideo(videoUrl: String) {
        val fileName = extractFileNameFromUrl(videoUrl)
        deleteFile(minioProperties.buckets.videos, fileName)
    }

    /**
     * Resim silme
     */
    fun deleteImage(imageUrl: String) {
        val fileName = extractFileNameFromUrl(imageUrl)
        deleteFile(minioProperties.buckets.images, fileName)
    }

    /**
     * Dosya indirme
     */
    fun downloadFile(bucketName: String, fileName: String): InputStream {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucketName)
                .`object`(fileName)
                .build()
        )
    }

    /**
     * Benzersiz dosya adı oluşturma
     */
    private fun generateFileName(originalFileName: String, extension: String): String {
        val uuid = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()
        return "${timestamp}_${uuid}.$extension"
    }

    /**
     * URL'den dosya adını çıkarma
     */
    private fun extractFileNameFromUrl(url: String): String {
        return url.substringAfterLast("/")
    }
}