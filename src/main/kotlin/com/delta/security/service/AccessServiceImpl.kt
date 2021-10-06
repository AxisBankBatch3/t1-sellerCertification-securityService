package com.delta.security.service

import com.delta.security.model.User
import com.delta.security.repository.UserRepository
import com.delta.security.request.PasswordResetRequest
import com.delta.security.request.SetUpPasswordRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class AccessServiceImpl : AccessService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    var encoder: PasswordEncoder? = null

    override fun getAllDetails(): MutableList<User?> {
        return userRepository!!.findAll()
    }

    override fun getByUsername(username: String?): Optional<User?>? {
        return userRepository?.findByUsername(username)
    }

    override fun existByUsername(emailId: String?): Boolean? {
        return userRepository?.existsByUsername(emailId)
    }

    override fun existUserByMobileNumber(mobile: String?): Boolean? {
        return userRepository?.existsByMobile(mobile)
    }

    override fun findByMobile(mobile: String?): User? {
        return userRepository?.findByMobile(mobile)
    }

    @Throws(Exception::class)
    override fun resetPasswordById(id: String?, passwordResetRequest: PasswordResetRequest?): String? {
        val mydata: Optional<User?> = userRepository!!.findById(id.toString())
        return if (mydata.isPresent()) {
            if (encoder!!.matches(passwordResetRequest?.oldPassword, mydata.get().password)) {
                val user = User(
                    id, mydata.get().fullName, mydata.get().organization,
                    mydata.get().username, mydata.get().mobile,
                    encoder!!.encode(passwordResetRequest?.newPassword), mydata.get().isAdmin
                )
                userRepository.save(user)
                "password set succesfully"
            } else {
                throw Exception("previous password not matched")
            }
        } else {
            throw Exception("User not found")
        }
    }

    @Throws(Exception::class)
    override fun setUpPassword(id: String?, setupPassword: SetUpPasswordRequest?): String? {
        val mydata: Optional<User?> = userRepository!!.findById(id.toString())
        return if (mydata.isPresent()) {
            if (encoder!!.matches(setupPassword?.otp, mydata.get().password)) {
                val user = User(
                    id, mydata.get().fullName, mydata.get().organization,
                    mydata.get().username, mydata.get().mobile,
                    encoder!!.encode(setupPassword?.newPassword), mydata.get().isAdmin
                )
                userRepository.save(user)
                "password set succesfully"
            } else {
                throw Exception("Invalid OPT")
            }
        } else {
            throw Exception("User not found")
        }
    }
}
