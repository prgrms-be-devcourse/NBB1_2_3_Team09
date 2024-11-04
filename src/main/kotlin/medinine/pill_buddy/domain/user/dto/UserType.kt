package medinine.pill_buddy.domain.user.dto

import medinine.pill_buddy.domain.user.entity.User
import medinine.pill_buddy.global.exception.ErrorCode
import medinine.pill_buddy.global.exception.PillBuddyCustomException

enum class UserType {
    CAREGIVER, CARETAKER;

    companion object {
        fun from(user : User) : UserType {
            val userType = user.javaClass.simpleName.uppercase()
            return UserType.valueOf(userType)
        }

        fun from(userType: String) : UserType {
            return try {
                UserType.valueOf(userType.uppercase())
            }  catch (e : IllegalArgumentException) {
                throw PillBuddyCustomException(ErrorCode.USER_INVALID_TYPE)
            }
        }
    }
}