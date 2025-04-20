package com.veredictum.backendveredictum.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.Entity
import jakarta.persistence.Table
import com.fasterxml.jackson.annotation.JsonProperty
import com.veredictum.backendveredictum.dto.LoginUsuarioDTO
import com.veredictum.backendveredictum.dto.UsuarioDTO
import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "usuario")
data class Usuario (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var idUsuario: Int? = null,

    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
    var nome: String = "",

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "Formato de e-mail inválido")
    @field:Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    var email: String = "",

    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    var senha: String = "",

    var isAtivo: Boolean = true,

    var isAdm: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "fk_adm")
    var administrador: Usuario? = null

    ) {
        constructor() : this(null, "", "", "", true, false)

    fun toDTO(): UsuarioDTO {
        return UsuarioDTO(
            idUsuario = this.idUsuario ?: 0,
            nome = this.nome,
            email = this.email,
            isAtivo = this.isAtivo,
            fkAdm = this.administrador?.idUsuario,
            isAdm = this.isAdm,
        )
    }

    fun toLoginDTO(logado: Boolean): LoginUsuarioDTO {
        return LoginUsuarioDTO(
            idUsuario = this.idUsuario ?: 0,
            nome = this.nome,
            email = this.email,
            isAtivo = this.isAtivo,
            isLogado = logado,
            isAdm = this.isAdm,
        )
    }

}