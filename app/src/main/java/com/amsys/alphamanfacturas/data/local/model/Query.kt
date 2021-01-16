package com.amsys.alphamanfacturas.data.local.model

open class Query {
    var User: String = "" // junior
    var Password: String = "" //123

    var pageNumber: Int = 0
    var pageSize: Int = 0
    var userId: Int = 0

    constructor()

    constructor(User: String, Password: String) {
        this.User = User
        this.Password = Password
    }
}