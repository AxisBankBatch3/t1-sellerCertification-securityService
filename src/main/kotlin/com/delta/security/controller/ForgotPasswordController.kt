package com.delta.security.controller

import com.delta.security.dto.EmailRequestDto
import com.delta.security.jwt.JwtAuthTokenFilter
import com.delta.security.jwt.JwtProvider
import com.delta.security.model.User
import com.delta.security.repository.UserRepository
import com.delta.security.request.ForgetPasswordRequest
import com.delta.security.request.SetUpPasswordRequest
import com.delta.security.response.JwtResponse
import com.delta.security.service.AccessService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.text.DecimalFormat
import java.util.*
import javax.validation.Valid


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
class ForgotPasswordController {
    @Autowired
    var authenticationManager: AuthenticationManager? = null

    @Autowired
    var encoder: PasswordEncoder? = null

    @Autowired
    var jwtProvider: JwtProvider? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var restTemplate: RestTemplate? = null

    @Autowired
    var accessService: AccessService? = null
    var email: EmailRequestDto? = null
    @PostMapping("/api/auth/forgotPassword")
    @Throws(Exception::class)
    fun forgotPassword(@RequestBody forgetPasswordRequest: ForgetPasswordRequest): ResponseEntity<*> {
        return if (accessService!!.existUserByMobileNumber(forgetPasswordRequest.mobile)!!) {
            val userByMobile: User? = accessService!!.findByMobile(forgetPasswordRequest.mobile)
            if (accessService!!.existByUsername(forgetPasswordRequest.email)!!) {
                val userByEmail: Optional<User?>? = accessService!!.getByUsername(forgetPasswordRequest.email)
                if (userByMobile!!.username.equals(userByEmail!!.get().username)) {
                    val otp: String = DecimalFormat("000000").format(Random().nextInt(999999))
                    val user = User(
                        userByMobile.id, userByMobile.fullName, userByMobile.organization,
                        userByMobile.username, userByMobile.mobile,
                        encoder!!.encode(otp), userByMobile.isAdmin
                    )
                    userRepository?.save(user)
                    val authentication: Authentication = authenticationManager!!.authenticate(
                        UsernamePasswordAuthenticationToken(userByEmail.get().username, otp)
                    )
                    SecurityContextHolder.getContext().authentication = authentication
                    val jwt: String = jwtProvider!!.generateJwtToken(authentication)
                    email = EmailRequestDto(
                        "Orchestrator-Reset Password",
                        userByEmail.get().username,
                        "username : " + userByEmail.get().username.toString() + "   OTP : " + otp
                    )
                    System.out.println(userByEmail.get().username)
                    ResponseEntity.ok(
                        JwtResponse(
                            userByEmail.get().id,
                            userByEmail.get().fullName,
                            userByEmail.get().username,
                            jwt
                        )
                    )
                } else {
                    throw Exception("User Not found")
                }
            } else {
                throw Exception("Email Address is not Registered")
            }
        } else {
            throw Exception("mobile number is not registered")
        }
    }

    @PostMapping("/api/auth/email")
    fun sendEmail(@RequestBody emailRequestDto: @Valid EmailRequestDto?): String? {
        emailRequestDto?.email=email?.email
        emailRequestDto?.body=email?.body
        emailRequestDto?.subject=email?.subject
        emailRequestDto?.from="octatenoreply@gmail.com"
        val headers = HttpHeaders()

        headers.setBearerAuth(JwtAuthTokenFilter.jwt.toString())
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON))
        val entity: HttpEntity<EmailRequestDto> = HttpEntity<EmailRequestDto>(emailRequestDto, headers)
        val responseEntity = restTemplate!!.exchange(
            "http://localhost:8082/api/access/email",
            HttpMethod.POST, entity, String::class.java
        )
        return responseEntity.body
    }

    @PostMapping("/api/setPassword/{id}")
    @Throws(Exception::class)
    fun setPassword(@PathVariable("id") id: String?, @RequestBody setUpPassword: SetUpPasswordRequest?): String? {
        return accessService!!.setUpPassword(id, setUpPassword)
    }
}