package com.delta.security.controller

import com.delta.security.jwt.JwtProvider
import com.delta.security.model.User
import com.delta.security.repository.UserRepository
import com.delta.security.request.LoginForm
import com.delta.security.response.JwtResponse
import com.delta.security.service.AccessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
class AuthRestAPIs {
    @Autowired
    var authenticationManager: AuthenticationManager? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var accessService: AccessService? = null

    @Autowired
    var encoder: PasswordEncoder? = null

    @Autowired
    var jwtProvider: JwtProvider? = null

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody loginRequest: @Valid LoginForm?): ResponseEntity<*>? {
        val authentication: Authentication = authenticationManager!!.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest!!.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt: String = jwtProvider!!.generateJwtToken(authentication)

        val userDetails = authentication.getPrincipal() as UserDetails
        val data: Optional<User?>? = userRepository?.findByUsername(loginRequest.username)
        return ResponseEntity.ok(
            JwtResponse(
                data!!.get().id,
                data.get().fullName,
                data.get().organization,
                userDetails.username,
                data.get().mobile,
                data.get().isAdmin,
                jwt,
                "Bearer"
            )
        )
    }

    /*
	 * @PutMapping("/api/reset/{id}") public String
	 * passwordReset(@PathVariable("id") String id,@RequestBody PasswordResetRequest
	 * passwordResetRequest) throws Exception { return
	 * accessService.resetPasswordById(id, passwordResetRequest); }
	 */
    @GetMapping("/existbyemail/{emailId}")
    fun isEmail(@PathVariable("emailId") emailId: String?): Boolean {
        return accessService!!.existByUsername(emailId)!!
    }

    @GetMapping("/existByMobile/{mobile}")
    fun isMobile(@PathVariable("mobile") mobile: String?): Boolean {
        return accessService!!.existUserByMobileNumber(mobile)!!
    }

    @GetMapping("/findByMobile/{mobile}")
    fun findUserByMobile(@PathVariable("mobile") mobile: String?): User? {
        return accessService!!.findByMobile(mobile)
    }
    @GetMapping("/policyholders")
    fun getallclients(): MutableList<User?> {
        return accessService!!.getAllDetails();
    }

}