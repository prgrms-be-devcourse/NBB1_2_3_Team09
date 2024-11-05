package medinine.pill_buddy.domain.medicationApi.controller

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import medinine.pill_buddy.domain.medicationApi.dto.JsonForm
import medinine.pill_buddy.domain.medicationApi.dto.MedicationDTO
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import medinine.pill_buddy.domain.medicationApi.service.MedicationApiService
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/api/search")
@Tag(name = "약 검색 기능", description = "공공API e약은요 를 이용해 일반의약품 정보를 검색한다")
class MedicationApiController(private val medicationApiService: MedicationApiService) {
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(value =  "{\"totalCount\": 1,\"nowPageNum\": 1,\"maxPageNum\": 1,\"data\": [{\"entpName\": \"한국존슨앤드존슨판매(유)\",\"itemName\": \"어린이타이레놀산160밀리그램(아세트아미노펜)\",\"itemSeq\": \"202005623\"}]}")],
            schema = Schema(implementation = MedicationDTO::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "API에 약이 없을 경우",
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"약 정보를 찾을 수 없습니다.\"}")]
        )]
    )
    @ApiResponse(
        responseCode = "400",
        description = "페이지 설정이 잘못됐을 경우",
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"페이지 설정이 잘못됐습니다.\"}")]
        )]
    )
    @ApiResponse(
        responseCode = "504",
        description = "외부 API와의 통신 오류의 경우",
        content = [Content(
            mediaType = "application/json",
            examples = [ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"네트워크 통신 중 오류가 발생했습니다.\"}")]
        )]
    )
    @GetMapping
    fun showSearchForm(model: Model): ModelAndView {
        // 빈 MedicationForm 객체를 모델에 추가해 폼을 렌더링
        model.addAttribute("medicationForm", MedicationForm())
        return ModelAndView("medication/search")
    }

    @PostMapping
    fun searchMedications(
        @ModelAttribute @Validated medicationForm: MedicationForm,
        bindingResult: BindingResult,
        @RequestParam(defaultValue = "1") pageNo: Int,
        @RequestParam(defaultValue = "10") numOfRows: Int,
        model: Model
    ): ModelAndView {
        if (bindingResult.hasErrors()) {
            throw PillBuddyCustomException(ErrorCode.REQUIRED_VALUE)
        }

        // 검색 결과 로깅
        log.info("${medicationForm.itemName}, ${medicationForm.itemSeq}, ${medicationForm.entpName}")

        // 검색 결과 조회 (DB 또는 API에서)
        var medicationDtoList = medicationApiService.findPageByName(medicationForm.itemName?:throw PillBuddyCustomException(ErrorCode.REQUIRED_VALUE), pageNo - 1, numOfRows)

        // 데이터가 없으면 외부 API를 통해 새로 저장
        if (medicationDtoList.isEmpty) {
            medicationApiService.saveMedication(medicationApiService.createDto(medicationForm), medicationForm.itemName?:throw PillBuddyCustomException(ErrorCode.REQUIRED_VALUE))
            medicationDtoList = medicationApiService.findPageByName(medicationForm.itemName?:throw PillBuddyCustomException(ErrorCode.REQUIRED_VALUE), pageNo - 1, numOfRows)
        }

        // 검색 결과를 모델에 추가
        val jsonForm = JsonForm(
            totalCount = medicationDtoList.totalElements.toInt(),
            nowPageNum = pageNo,
            maxPageNum = medicationDtoList.totalPages,
            data = medicationDtoList.content
        )
        model.addAttribute("jsonForm", jsonForm)
        model.addAttribute("medicationForm",medicationForm)
        return ModelAndView("medication/medicationList")
    }
    @GetMapping("/synchronized")
    fun synchronize(): String {
        medicationApiService.synchronizeDB()
        return "redirect:/api/search"
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    fun synchronizeDB() = medicationApiService.synchronizeDB()


}