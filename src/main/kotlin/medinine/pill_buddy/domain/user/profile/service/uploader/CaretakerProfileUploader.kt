package medinine.pill_buddy.domain.user.profile.service.uploader

import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.profile.entity.Image
import medinine.pill_buddy.domain.user.profile.repository.ImageRepository
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.util.UploadUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Component
@Transactional
class CaretakerProfileUploader(
    private val caretakerRepository: CaretakerRepository,
    private val imageRepository: ImageRepository,
    private val uploadUtils: UploadUtils
) : ProfileUploader {

    override fun upload(file: MultipartFile, userId: Long) {
        val caretaker = caretakerRepository.findById(userId).orElseThrow {
            PillBuddyCustomException(ErrorCode.USER_NOT_FOUND)
        }

        val filename = uploadUtils.upload(file)

        val image : Image = caretaker.image?.apply {
            // 기존 이미지가 있다면 삭제하고 새로운 URL 업데이트
            UploadUtils.deleteFile(url)
            updateUrl(filename)
        } ?: Image(url = filename)

        // DB에 이미지 저장
        try {
            val save: Image = imageRepository.save(image)
            caretaker.updateImage(save)
        } catch (e: Exception) {
            // 예외 발생 시 업로드한 파일 삭제
            UploadUtils.deleteFile(filename)
        }
    }
}