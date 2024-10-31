package medinine.pill_buddy.domain.user.controller

import JoinDto
import com.fasterxml.jackson.databind.ObjectMapper
import medinine.pill_buddy.domain.user.dto.UserType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest @Autowired constructor(
    private val mapper: ObjectMapper,
    private val mvc: MockMvc
) {
    @Test
    @DisplayName("회원가입 테스트")
    fun joinTest() {
        // given
        val username = "newUser"
        val loginId = "newLoginId"
        val password = "newPassword123!@"
        val email = "new@gmail.com"
        val phoneNumber = "01011122221"
        val userType = "CARETAKER"

        // when
        val body = mapper.writeValueAsString(
            JoinDto(
                username = username,
                loginId = loginId,
                password = password,
                email = email,
                phoneNumber = phoneNumber,
                userType = UserType.valueOf(userType)
            )
        )

        // then
        mvc.perform(post("api/users/join")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(
        """
            {
                "username": "$username",
                "loginId": "$loginId",
                "email": "$email",
                "phoneNumber": "$phoneNumber",
                "userType": "${userType.lowercase()}"
            }
        """.trimIndent()))
    }
}