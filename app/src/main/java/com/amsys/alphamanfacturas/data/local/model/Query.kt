package com.amsys.alphamanfacturas.data.local.model

open class Query {
    var User: String = "" // junior
    var Password: String = "" //123

    var pageNumber: Int = 0
    var pageSize: Int = 0
    var userId: Int = 0
    var assetId: Int = 0

    var claseParadaId: Int = 0
    var tipoParadaId: Int = 0

    var tipo: Int = 0
    var code: String = ""
    var name: String = ""

    constructor()

    //login
    constructor(User: String, Password: String) {
        this.User = User
        this.Password = Password
    }

    //combo
    constructor(tipo: Int, name: String) {
        this.tipo = tipo
        this.name = name
    }

    //informacion
    constructor(userId: Int, assetId: Int) {
        this.userId = userId
        this.assetId = assetId
    }

//    //tipo parada
//    constructor(userId: Int, claseParadaId: Int, assetId: Int) {
//        this.userId = userId
//        this.claseParadaId = claseParadaId
//        this.assetId = assetId
//    }
//
//    //sub tipo parada
//    constructor(userId: Int, tipoParadaId: Int, claseParadaId: Int, assetId: Int) {
//        this.userId = userId
//        this.tipoParadaId = tipoParadaId
//        this.claseParadaId = claseParadaId
//        this.assetId = assetId
//    }
}