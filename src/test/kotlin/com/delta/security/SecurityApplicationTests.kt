package com.delta.security

import com.delta.security.model.User
import com.delta.security.repository.UserRepository
import com.delta.security.service.AccessService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.net.URI

@SpringBootTest
class SecurityApplicationTests {

	@MockBean
	private val userRepository: UserRepository? = null

	@Autowired
	var accessService: AccessService? = null

	@Test
	fun contextLoads() {
	}

//	@Test
//	fun getAllDetails() {
//		var data = User()
//
//		data.fullName = "user"
//		data.organization = "axis"
//		data.username = "user@gmail.com"
//		data.mobile = "9573631086"
//		data.isAdmin = false
//
//
//		Mockito.`when`(userRepository!!.findAll())
//			.thenReturn(listOf(data))
//		Assertions.assertEquals(1, accessService!!.getAllDetails().size);
//	}




}
