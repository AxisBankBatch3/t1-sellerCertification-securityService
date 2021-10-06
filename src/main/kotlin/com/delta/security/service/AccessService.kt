package com.delta.security.service

import com.delta.security.model.User
import com.delta.security.request.PasswordResetRequest
import com.delta.security.request.SetUpPasswordRequest
import java.util.*

interface AccessService {
    fun getByUsername(username: String?): Optional<User?>?
    fun existByUsername(emailId: String?): Boolean?
    fun existUserByMobileNumber(mobile: String?): Boolean?
    fun findByMobile(mobile: String?): User?

    fun getAllDetails() : MutableList<User?>

    @Throws(Exception::class)
    fun resetPasswordById(id: String?, passwordResetRequest: PasswordResetRequest?): String?

    @Throws(Exception::class)
    fun setUpPassword(id: String?, setupPassword: SetUpPasswordRequest?): String?
}