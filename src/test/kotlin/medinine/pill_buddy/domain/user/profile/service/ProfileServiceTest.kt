package medinine.pill_buddy.domain.user.profile.service

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.profile.dto.ProfileUploadDto
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ProfileServiceTest @Autowired constructor(
    private val profileService: ProfileService,
    private val caregiverRepository: CaregiverRepository,
) {
    @Test
    @DisplayName("파일의 ContentType 이 image 로 시작하지 않으면 예외가 발생한다.")
    fun uploadProfile_with_invalid_content_type() {
        val caregiver = createCaregiver()

        val file = MockMultipartFile("file", "test.jpg", "audio/mpeg", "test image content".toByteArray())
        val profileUploadDto = ProfileUploadDto(file, UserType.CAREGIVER)

        assertThatThrownBy { profileService.uploadProfile(caregiver.id!!, profileUploadDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.PROFILE_INVALID_FILE_TYPE.message)
    }

    @Test
    @DisplayName("파일의 이름이 비어 있는 경우 예외가 발생한다.")
    fun uploadProfile_with_blank_file_name() {
        val caregiver = createCaregiver()

        val file = MockMultipartFile("file", " ", "image/jpeg", "test image content".toByteArray())
        val profileUploadDto = ProfileUploadDto(file, UserType.CAREGIVER)

        assertThatThrownBy { profileService.uploadProfile(caregiver.id!!, profileUploadDto) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage(ErrorCode.PROFILE_BLANK_FILE_NAME.message)
    }

    private fun createCaregiver(): Caregiver {
        val caregiver = Caregiver(
            username = "test-caregiver",
            loginId = "test-loginId",
            password = "test-password",
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
        return caregiverRepository.save(caregiver)
    }
}