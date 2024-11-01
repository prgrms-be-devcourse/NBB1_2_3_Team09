package medinine.pill_buddy.domain.user.profile.service.uploader

import org.springframework.web.multipart.MultipartFile

interface ProfileUploader {
    fun upload(file: MultipartFile, userId: Long)
}