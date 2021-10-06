package com.delta.security.request

class PasswordResetRequest {
    var oldPassword: String? = null
    var newPassword: String? = null

    constructor() : super() {
    }

    constructor(oldPassword: String?, newPassword: String?) : super() {
        this.oldPassword = oldPassword
        this.newPassword = newPassword
    }
}