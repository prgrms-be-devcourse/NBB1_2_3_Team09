package medinine.pill_buddy.domain.user.profile.service

import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.profile.dto.ProfileUploadDto
import medinine.pill_buddy.domain.user.profile.service.uploader.CaregiverProfileUploader
import medinine.pill_buddy.domain.user.profile.service.uploader.CaretakerProfileUploader
import medinine.pill_buddy.domain.user.profile.service.uploader.ProfileUploader
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class ProfileService(
    caregiverProfileUploader: CaregiverProfileUploader,
    caretakerProfileUploader: CaretakerProfileUploader
) {
    private companion object {
        private const val FILE_TYPE_PREFIX: String = "image"
    }

    private val uploaderMap = mapOf(
        UserType.CAREGIVER to caregiverProfileUploader,
        UserType.CARETAKER to caretakerProfileUploader
    )

    fun uploadProfile(userId: Long, profileUploadDto: ProfileUploadDto) {
        val file: MultipartFile = profileUploadDto.file
        val userType: UserType = profileUploadDto.userType

        validateFile(file)

        val uploader: ProfileUploader = uploaderMap[userType] ?: throw PillBuddyCustomException(ErrorCode.USER_INVALID_TYPE)

        uploader.upload(file, userId)
    }

    private fun validateFile(file: MultipartFile) {
        require(file.contentType?.startsWith(FILE_TYPE_PREFIX) == true) {
            throw PillBuddyCustomException(ErrorCode.PROFILE_INVALID_FILE_TYPE)
        }
        require(!file.originalFilename.isNullOrBlank()) {
            throw PillBuddyCustomException(ErrorCode.PROFILE_BLANK_FILE_NAME)
        }
    }
}