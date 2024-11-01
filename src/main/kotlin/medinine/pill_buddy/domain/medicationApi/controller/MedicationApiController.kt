package medinine.pill_buddy.domain.medicationApi.controller

import io.swagger.v3.oas.annotations.Parameter
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
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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
    fun findMedicationByApi(
        @ModelAttribute @Validated medicationForm: MedicationForm,
        bindingResult: BindingResult,
        @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "1") pageNo: Int,
        @Parameter(description = "한 페이지의 데이터 개수") @RequestParam(defaultValue = "10") numOfRows: Int
    ): JsonForm {
        if (bindingResult.hasErrors()) {
            throw PillBuddyCustomException(ErrorCode.REQUIRED_VALUE)
        }
        log.info("${medicationForm.itemName}, ${medicationForm.itemSeq}, ${medicationForm.entpName}")
        //DB에 레코드가 있다면 DB에서 레코드 반환
        var medicationDtoList = medicationApiService.findPageByName(medicationForm.itemName, pageNo - 1, numOfRows)

        if (!medicationDtoList.isEmpty) {
            log.info("약정보 저장되어있음")
            return JsonForm(
                medicationDtoList.totalElements.toInt(),
                pageNo,
                medicationDtoList.totalPages,
                medicationDtoList.content
            )
        }
        log.info("약정보 없음")

        //DB에 레코드가 없다면 외부API 통신을 통해 DB에 레코드를 저장한 후 레코드 반환
        medicationApiService.saveMedication(medicationApiService.createDto(medicationForm),medicationForm.itemName)
        medicationDtoList = medicationApiService.findPageByName(medicationForm.itemName, pageNo - 1, numOfRows)

        return JsonForm(
            medicationDtoList.totalElements.toInt(),
            pageNo,
            medicationDtoList.totalPages,
            medicationDtoList.content
        )
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul")
    fun synchronizeDB() = medicationApiService.synchronizeDB()
}