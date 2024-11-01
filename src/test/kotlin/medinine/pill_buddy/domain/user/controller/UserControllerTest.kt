package medinine.pill_buddy.domain.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.domain.user.dto.UserDto
import medinine.pill_buddy.domain.user.dto.UserPasswordUpdateDto
import medinine.pill_buddy.domain.user.dto.UserType
import medinine.pill_buddy.domain.user.dto.UserUpdateDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val caretakerRepository: CaretakerRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private lateinit var caretaker: Caretaker

    @BeforeEach
    fun setup() {
        createCaretaker()
        caretakerRepository.save(caretaker)
    }

    @Test
    @DisplayName("회원 조회 API 테스트")
    fun findUserInfo() {
        // given
        val userId = caretaker.id
        val userType = caretaker.javaClass.simpleName.uppercase()

        // when
        val result = mockMvc.perform(
            get("/api/users/{userId}", userId)
                .param("userType", userType)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn().response
        val jsonResponse = result.contentAsString
        val userDto = objectMapper.readValue(jsonResponse, UserDto::class.java)

        // then
        assertThat(result.status).isEqualTo(200)
        assertThat(userDto.userType).isEqualTo("caretaker")
        assertThat(userDto.username).isEqualTo(caretaker.username)
        assertThat(userDto.email).isEqualTo(caretaker.email)
        assertThat(userDto.phoneNumber).isEqualTo(caretaker.phoneNumber)
    }

    @Test
    @DisplayName("회원 정보 수정 API 테스트")
    fun updateUserInfo() {
        // given
        val userId = caretaker.id
        val userType = UserType.valueOf(caretaker.javaClass.simpleName.uppercase())
        val updatedUser = UserUpdateDto(
            username = "updated-user",
            loginId = "updated-loginId",
            email = "test@gmail.com",
            phoneNumber = "01055555555",
            userType = userType
        )

        // when
        val result = mockMvc.perform(
            put("/api/users/{userId}", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedUser)))
            .andReturn().response
        val jsonResponse = result.contentAsString
        val userDto = objectMapper.readValue(jsonResponse, UserDto::class.java)

        // then
        assertThat(result.status).isEqualTo(200)
        assertThat(userDto.username).isEqualTo(updatedUser.username)
        assertThat(userDto.email).isEqualTo(updatedUser.email)
        assertThat(userDto.phoneNumber).isEqualTo(updatedUser.phoneNumber)
    }

    @Test
    @DisplayName("회원 비밀번호 수정 API 테스트")
    fun updateUserPassword() {
        // given
        val userId = caretaker.id
        val userType = UserType.valueOf(caretaker.javaClass.simpleName.uppercase())
        val passwordUpdateDto = UserPasswordUpdateDto(
            password = "newPassword1234!@",
            userType = userType
        )

        // when
        val result = mockMvc.perform(put("/api/users/{userId}/password", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(passwordUpdateDto)))
            .andReturn().response
        val updatedPasswordUser = caretakerRepository.findById(caretaker.id!!).get()

        // then
        assertThat(result.status).isEqualTo(200)
        assertThat(passwordEncoder.matches("newPassword1234!@", updatedPasswordUser.password)).isTrue()
    }

    @Test
    @DisplayName("DELETE /api/users/{userId} - 사용자 정보를 삭제할 수 있다.")
    fun deleteUser() {
        // given
        val userId = caretaker.id
        val userType = caretaker.javaClass.simpleName.uppercase()

        // when
        mockMvc.perform(
            delete("/api/users/{userId}", userId)
            .param("userType", userType))
            .andExpect(status().isNoContent)
        val deletedUser = caretakerRepository.findById(caretaker.id!!)

        // then
        assertThat(deletedUser.isEmpty).isTrue()
    }

    private fun createCaretaker() {
        caretaker = Caretaker(
            username = "test-caretaker",
            loginId = "test-loginId",
            password = passwordEncoder.encode("test-password"),
            email = "test-email",
            phoneNumber = "test-phoneNumber"
        )
    }
}