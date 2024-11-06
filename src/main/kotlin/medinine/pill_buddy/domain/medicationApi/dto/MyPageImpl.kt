package medinine.pill_buddy.domain.medicationApi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@JsonIgnoreProperties(ignoreUnknown = true, value = ["pageable"])
class MyPageImpl<T>(
    @JsonProperty("content") content: List<T>,
    @JsonProperty("number") page: Int,
    @JsonProperty("size")  size: Int,
    @JsonProperty("totalElements") totalElements: Long
) : PageImpl<T>(content, PageRequest.of(page, size), totalElements) {

    constructor(page: Page<T>) : this(page.content, page.number, page.size, page.totalElements)

    constructor(list: List<T>, pageable: Pageable, totalElements: Long) : this(
        list,
        pageable.pageNumber,
        pageable.pageSize,
        totalElements
    )
}
