package medinine.pill_buddy.global.exception

class PillBuddyCustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)