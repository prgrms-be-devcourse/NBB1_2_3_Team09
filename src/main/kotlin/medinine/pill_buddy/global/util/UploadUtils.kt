package medinine.pill_buddy.global.util

import jakarta.annotation.PostConstruct
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.UUID

@Component
class UploadUtils {
    @Value("\${file.path}")
    lateinit var uploadPath: String

    @PostConstruct
    private fun init() {
        val tempDir = File(uploadPath)

        if (!tempDir.exists()) {
            val created = tempDir.mkdir()
            if (!created) {
                throw PillBuddyCustomException(ErrorCode.PROFILE_CREATE_DIRECTORY_FAIL)
            }
        }
        uploadPath = tempDir.absolutePath
    }

    fun upload(file: MultipartFile): String {
        val uuid = UUID.randomUUID().toString()

        // upload 를 호출하는 ProfileService 에서 Null 처리
        val saveFileName = "$uuid$FILE_NAME_REPLACEMENT${cleanFileName(file.originalFilename!!)}"

        return try {
            file.transferTo(File(uploadPath, saveFileName))
            Paths.get(uploadPath, saveFileName).toString()
        } catch (e: IOException) {
            throw PillBuddyCustomException(ErrorCode.PROFILE_NOT_SUPPORT_FILE_TYPE)
        }
    }

    // 파일 이름에 특수 문자나 공백이 포함되어 있는 경우, 그 문자를 밑줄(_)로 대체
    private fun cleanFileName(fileName: String): String {
        return fileName.replace(FILE_NAME_REGEX, FILE_NAME_REPLACEMENT)
    }

    companion object {
        private const val FILE_NAME_REGEX = "[^a-zA-Z0-9.\\-_]"
        private const val FILE_NAME_REPLACEMENT = "_"

        fun deleteFile(filePath: String) {
            val file = File(filePath)

            if (file.exists() && !file.delete()) {
                throw PillBuddyCustomException(ErrorCode.PROFILE_DELETE_FILE_FAIL)
            }
        }
    }
}