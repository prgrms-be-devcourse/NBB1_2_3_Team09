package medinine.pill_buddy.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String
) {

    USER_MISMATCHED_ID_OR_PASSWORD(HttpStatus.CONFLICT, "로그인 아이디 혹은 비밀번호가 잘못되었습니다."),
    USER_AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "인증이 필요한 회원입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    USER_ALREADY_REGISTERED_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    USER_ALREADY_REGISTERED_LOGIN_ID(HttpStatus.CONFLICT, "이미 등록된 아이디입니다."),
    USER_ALREADY_REGISTERED_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 등록된 전화번호입니다."),
    USER_INVALID_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 유형입니다."),
    USER_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 이름을 찾을 수 없습니다."),

    PROFILE_INVALID_FILE(HttpStatus.BAD_REQUEST, "잘못된 이미지 파일입니다."),
    PROFILE_INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드할 수 있습니다."),
    PROFILE_NOT_SUPPORT_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    PROFILE_BLANK_FILE_NAME(HttpStatus.BAD_REQUEST, "파일 이름은 공백일 수 없습니다."),
    PROFILE_CREATE_DIRECTORY_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "업로드 디렉토리를 생성할 수 없습니다."),
    PROFILE_DELETE_FILE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 삭제할 수 없습니다."),

    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    JWT_TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다."),

    MEDICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "약 정보를 찾을 수 없습니다."),
    MEDICATION_NOT_MATCHED(HttpStatus.BAD_REQUEST, "약 정보가 일치하지 않습니다."),
    MEDICATION_NOT_REMOVED(HttpStatus.CONFLICT, "약 정보 삭제에 실패했습니다."),
    MEDICATION_NOT_REGISTERED(HttpStatus.CONFLICT, "약 정보 등록에 실패했습니다."),
    MEDICATION_NOT_MODIFIED(HttpStatus.CONFLICT, "약 정보 수정에 실패했습니다."),
    MEDICATION_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않는 약 정보입니다."),
    MEDICATION_IS_NULL(HttpStatus.NOT_FOUND, "현재 복용중인 약이 없습니다."),
    MEDICATION_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "약 이름을 찾을 수 없습니다."),

    CARETAKER_CAREGIVER_NOT_FOUND(HttpStatus.CONFLICT, "부모 정보 등록에 실패했습니다"),
    CARETAKER_CAREGIVER_REMOVED(HttpStatus.CONFLICT, "부모 정보 삭제에 실패했습니다"),

    CARETAKER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    CARETAKER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),
    CARETAKER_NOT_REMOVED(HttpStatus.CONFLICT, "사용자 정보 삭제에 실패했습니다."),
    CARETAKER_NOT_REGISTERED(HttpStatus.CONFLICT, "사용자 정보 등록에 실패했습니다."),
    CARETAKER_NOT_MODIFIED(HttpStatus.CONFLICT, "사용자 정보 수정에 실패했습니다."),
    CARETAKER_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 정보입니다."),
    CARETAKER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 사용자 정보입니다"),

    CAREGIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "보호자 정보를 찾을 수 없습니다."),
    CAREGIVER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "보호자 정보가 일치하지 않습니다."),
    CAREGIVER_NOT_REMOVED(HttpStatus.CONFLICT, "보호자 정보 삭제에 실패했습니다."),
    CAREGIVER_NOT_REGISTERED(HttpStatus.CONFLICT, "보호자 정보 등록에 실패했습니다."),
    CAREGIVER_NOT_MODIFIED(HttpStatus.CONFLICT, "보호자 정보 수정에 실패했습니다."),
    CAREGIVER_NOT_VALID(HttpStatus.BAD_REQUEST, "유효하지 않은 보호자 정보입니다."),

    CARETAKER_CAREGIVER_NOT_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 보호자 정보입니다"),
    CARETAKER_CAREGIVER_NOT_VALID(HttpStatus.CONFLICT, "유효하지 않은 보호자 정보입니다"),
    CAREGIVER_CARETAKER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다"),
    CARETAKER_CAREGIVER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "보호자 정보가 일치하지 않습니다"),

    REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "제품명은 필수입니다."),
    OUT_OF_PAGE(HttpStatus.BAD_REQUEST, "페이지 설정이 잘못됐습니다."),
    ERROR_CONNECTION(HttpStatus.GATEWAY_TIMEOUT, "외부 API 서버에 연결하는 중 오류가 발생했습니다."),
    NETWORK_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "네트워크 통신 중 오류가 발생했습니다."),

    MESSAGE_SEND_FAILED(HttpStatus.BAD_REQUEST, "메시지 전송에 실패했습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알람 정보를 찾을 수 없습니다."),
    PHONE_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "전화번호를 찾을 수 없습니다."),

    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 기록을 찾을 수 없습니다"),
    RECORD_NOT_REGISTERED(HttpStatus.CONFLICT, "기록 저장에 실패 했습니다"),
    RECORD_ALREADY_TAKEN(HttpStatus.CONFLICT, "이미 복용된 약 정보 기록입니다");
}
