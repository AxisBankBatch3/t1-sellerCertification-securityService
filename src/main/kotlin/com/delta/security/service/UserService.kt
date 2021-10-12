package com.delta.security.service

import com.delta.security.model.User
import com.delta.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun addSellers(sellerLst:List<User>): MutableList<User> {
        return userRepository.saveAll(sellerLst)
    }

    fun addSeller(seller:User):User{
        return userRepository.save(seller)
    }
}