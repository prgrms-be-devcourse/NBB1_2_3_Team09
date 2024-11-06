package medinine.pill_buddy.domain.medicationApi.controller

import medinine.pill_buddy.domain.medicationApi.dto.JsonForm
import medinine.pill_buddy.domain.medicationApi.dto.MedicationForm
import medinine.pill_buddy.domain.medicationApi.service.MedicationApiService
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException
import medinine.pill_buddy.log
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
@SessionAttributes("searchCriteria")
@Controller
@RequestMapping("/search")
class MedicationWebController(private val medicationApiService: MedicationApiService) {
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
        var medicationDtoList = medicationApiService.findPageByName(
            medicationForm.itemName ?: throw PillBuddyCustomException(
                ErrorCode.REQUIRED_VALUE
            ), pageNo - 1, numOfRows
        )

        // 데이터가 없으면 외부 API를 통해 새로 저장
        if (medicationDtoList.isEmpty) {
            medicationApiService.saveMedication(
                medicationApiService.createDto(medicationForm),
                medicationForm.itemName ?: throw PillBuddyCustomException(
                    ErrorCode.REQUIRED_VALUE
                )
            )
            medicationDtoList = medicationApiService.findPageByName(
                medicationForm.itemName ?: throw PillBuddyCustomException(
                    ErrorCode.REQUIRED_VALUE
                ), pageNo - 1, numOfRows
            )
        }

        // 검색 결과를 모델에 추가
        val jsonForm = JsonForm(
            totalCount = medicationDtoList.totalElements.toInt(),
            nowPageNum = pageNo,
            maxPageNum = medicationDtoList.totalPages,
            data = medicationDtoList.content
        )
        model.addAttribute("jsonForm", jsonForm)
        model.addAttribute("medicationForm", medicationForm)

        return ModelAndView("medication/medicationList")
    }

    @GetMapping("/synchronized")
    fun synchronize(): ModelAndView {
        medicationApiService.synchronizeDB()
        return ModelAndView("redirect:/api/search")
    }

    @GetMapping("/{medicationId}")
    fun findById(
        @PathVariable medicationId: Long,
        @RequestParam itemName: String?,
        @RequestParam entpName: String?,
        @RequestParam itemSeq: Long?,
        model: Model
    ): String {
        val medication = medicationApiService.findById(medicationId)
        model.addAttribute("medication", medication)

        // 검색 조건을 모델에 추가하여 뷰로 전달
        model.addAttribute("itemName", itemName)
        model.addAttribute("entpName", entpName)
        model.addAttribute("itemSeq", itemSeq)
        return "medication/medication"
    }

}