package medinine.pill_buddy.domain.user.profile.service.uploader

import medinine.pill_buddy.domain.user.caregiver.entity.Caregiver
import medinine.pill_buddy.domain.user.caregiver.repository.CaregiverRepository
import medinine.pill_buddy.domain.user.profile.repository.ImageRepository
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.util.UploadUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CaregiverProfileUploaderTest @Autowired constructor(
    private val caregiverProfileUploader: CaregiverProfileUploader,
    private val caregiverRepository: CaregiverRepository,
    private val imageRepository: ImageRepository,
) {
    private var uploadedImageUrl: String? = null

    @AfterEach
    fun cleanUp() {
        // 테스트가 완료되면 업로드된 이미지 삭제
        uploadedImageUrl?.let {
            UploadUtils.deleteFile(it)
        }
    }

    @Test
    @DisplayName("보호자의 프로필 이미지를 업로드 할 수 있다.")
    fun `upload caregiver profile image`() {
        // given
        val caregiver = createCaregiver()
        val caregiverId = caregiver.id!!

        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".toByteArray())

        // when
        caregiverProfileUploader.upload(file, caregiverId)
        val uploadedProfileUser = caregiverRepository.findById(caregiverId).get()
        val savedImage = imageRepository.findById(uploadedProfileUser.image?.id!!).get()
        val uploadedImage = uploadedProfileUser.image!!

        uploadedImageUrl = savedImage.url // 삭제를 위해 저장

        // then
        assertThat(savedImage).isEqualTo(uploadedImage)
        assertThat(savedImage.url).endsWith("test.jpg")
        assertThat(uploadedImage.url).endsWith("test.jpg")
    }

    @Test
    @DisplayName("보호자가 존재하지 않다면 PillBuddyException 이 발생한다.")
    fun `throw exception if caregiver does not exist`() {
        val nonExistentUserId = 99999999L
        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".toByteArray())

        assertThatThrownBy { caregiverProfileUploader.upload(file, nonExistentUserId) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("회원 정보를 찾을 수 없습니다.")
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