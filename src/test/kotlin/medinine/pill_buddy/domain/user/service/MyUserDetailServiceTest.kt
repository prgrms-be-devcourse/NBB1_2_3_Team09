package medinine.pill_buddy.domain.user.service

import jakarta.transaction.Transactional
import medinine.pill_buddy.domain.user.caretaker.entity.Caretaker
import medinine.pill_buddy.domain.user.caretaker.repository.CaretakerRepository
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootTest
@Transactional
class MyUserDetailServiceTest(

    @Autowired
    private val myUserDetailsService: MyUserDetailService,

    @Autowired
    private val caretakerRepository: CaretakerRepository,

    @Autowired
    private val passwordEncoder: PasswordEncoder,
) {
    private lateinit var caretaker: Caretaker

    @BeforeEach
    fun setup() {
        createCaretaker();
        caretakerRepository.save(caretaker);
    }

    @Test
    @DisplayName("회원의 login Id 를 통해 회원 정보를 조회할 수 있다.")
    fun loadUserByUsername() {
        // given
        val loginId = caretaker.loginId

        // when
        val details = myUserDetailsService.loadUserByUsername(loginId)
        val authorities: Collection<GrantedAuthority?> = details.authorities

        // then
        assertThat(details.username).isEqualTo(loginId)
        assertThat(details.password).isEqualTo(caretaker.password)
        assertThat(authorities.size).isEqualTo(1)
        assertThat(authorities.iterator().next()!!.authority).isEqualTo("ROLE_USER")
    }

    @Test
    @DisplayName("저장되지 않은 login Id 를 입력하면 PillBuddyCustomException 이 발생한다.")
    fun loadUserByUsername_exception() {
        val loginId = "AnonymousUser1"

        assertThatThrownBy { myUserDetailsService.loadUserByUsername(loginId) }
            .isInstanceOf(PillBuddyCustomException::class.java)
            .hasMessage("회원 정보를 찾을 수 없습니다.")
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