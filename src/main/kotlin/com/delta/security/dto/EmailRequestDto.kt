package com.delta.security.dto

import javax.validation.constraints.Email

class EmailRequestDto {
    var from: String? = null
    var subject: String? = null

    @Email(message = "Invalid Email address")
    var email: String? = null
    var body: String? = null

    constructor() : super() {        // TODO Auto-generated constructor stub
    }

    constructor(subject: String?, @Email(message = "Invalid Email address") email: String?, body: String?) : super() {
        this.subject = subject
        this.email = email
        this.body = body
    }
}