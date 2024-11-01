package medinine.pill_buddy.domain.user.profile.service.uploader

import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
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
class CaretakerProfileUploaderTest @Autowired constructor(
    private val caretakerProfileUploader: CaretakerProfileUploader,
    private val caretakerRepository: CaretakerRepository,
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
    @DisplayName("사용자의 프로필 이미지를 업로드 할 수 있다.")
    fun `should upload Caretaker profile image`() {
        // given
        val caretaker = createCaretaker()
        val caretakerId = caretaker.id!!

        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".toByteArray())

        // when
        caretakerProfileUploader.upload(file, caretakerId)
        val uploadedProfileUser = caretakerRepository.findById(caretakerId).get()
        val savedImage = imageRepository.findById(uploadedProfileUser.image?.id!!).get()
        val uploadedImage = uploadedProfileUser.image!!

        uploadedImageUrl = savedImage.url // 삭제를 위해 저장

        // then
        assertThat(savedImage).isEqualTo(uploadedImage)
        assertThat(savedImage.url).endsWith("test.jpg")
        assertThat(uploadedImage.url).endsWith("test.jpg")
    }

    @Test
    @DisplayName("사용자가 존재하지 않다면 PillBuddyException 이 발생한다.")
    fun `should throw exception if Caretaker does not exist`() {
        val nonExistentUserId = 99999999L
        val file = MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".toByteArray())

        assertThatThrownBy { caretakerProfileUploader.upload(file, nonExistentUserId) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("회원 정보를 찾을 수 없습니다.")
    }

    private fun createCaretaker(): Caretaker {
        val caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "test-loginId",
            password = "test-password",
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
        return caretakerRepository.save(caretaker)
    }
}