package com.veredictum.backendveredictum.dto

data class UsuarioDTO(

    val idUsuario: Int,
    val nome: String,
    val email: String,
    val senha: String,
    val isAtivo: Boolean,
    val fkAdm: Int?,
    val isAdm: Boolean

)
