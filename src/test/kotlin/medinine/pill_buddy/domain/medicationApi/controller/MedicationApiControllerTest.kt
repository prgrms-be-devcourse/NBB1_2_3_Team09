package medinine.pill_buddy.domain.medicationApi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import medinine.pill_buddy.domain.medicationApi.dto.MedicationDTO
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import medinine.pill_buddy.domain.medicationApi.service.MedicationApiService
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.global.jwt.JwtTokenProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [MedicationApiController::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(ObjectMapper::class)
@WithMockUser(username = "mock-user", roles = ["USER"])
internal class MedicationApiControllerTest( @Autowired val mockMvc: MockMvc ){

    @MockBean
    lateinit var medicationApiService: MedicationApiService
    @MockBean
    lateinit var jpaMetamodelMappingContext: JpaMetamodelMappingContext
    @MockBean
    lateinit var jwtTokenProvider : JwtTokenProvider

    @BeforeEach
    fun setUp() {
        val medicationDTO = MedicationDTO("아스피린")
        val medicationList = listOf(medicationDTO)
        Mockito.`when`(medicationApiService.findPageByName("아스피린", 0, 10)).thenReturn(PageImpl(medicationList))
        Mockito.`when`(medicationApiService.findPageByName("아스피린", 1, 10)).thenThrow(PillBuddyCustomException(ErrorCode.OUT_OF_PAGE))
        Mockito.`when`(medicationApiService.findPageByName("에러발생", 0, 10)).thenReturn(PageImpl(listOf()))
        Mockito.`when`(medicationApiService.createDto(MedicationForm("에러발생"))).thenThrow(PillBuddyCustomException(ErrorCode.ERROR_CONNECTION))
    }

    @Test
    @DisplayName("데이터를 반환해줄 수 있어야 한다.")
    @Throws(Exception::class)
    fun test_Success() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/search")
                .param("itemName", "아스피린")
                .param("pageNo", "1")
                .param("numOfRows", "10")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].itemName").value("아스피린"))
    }

    @Test
    @DisplayName("필수 값을 넣지 않았을때 bindingResult에 체크돼야 한다.")
    @Throws(
        Exception::class
    )
    fun test_ValidationError() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/search")
                .param("itemName","")
                .param("pageNo", "1")
                .param("numOfRows", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    @Test
    @DisplayName("가능한 페이징 수 이상의 값을 입력했을 때 예외가 발생해야한다.")
    @Throws(
        Exception::class
    )
    fun test_OutOfPageError() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/search")
                .param("itemName", "아스피린")
                .param("pageNo", "2")
                .param("numOfRows", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    @Test
    @DisplayName("외부 API 서버에 문제가 생겼을때 예외가 발생해야한다.")
    @Throws(
        Exception::class
    )
    fun test_TimeOut() {
        // MockMvc로 GET 요청 수행 및 상태 코드 검증
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/search")
                .param("itemName", "에러발생")
                .param("pageNo", "1")
                .param("numOfRows", "10")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isGatewayTimeout())
    }
}