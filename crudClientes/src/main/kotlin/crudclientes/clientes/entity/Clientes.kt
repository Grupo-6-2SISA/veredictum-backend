package crudclientes.clientes.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
data class Clientes(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id // do pacote jakarta.persistence
    @GeneratedValue(strategy = GenerationType.IDENTITY) // faz com que o id seja auto increment
    var id:Int?,
    @field: NotBlank var nome: String?,
    @field: NotBlank var email: String?,
    @field: NotBlank var rg: String?,
    @field: NotBlank var cpf: String?,
    var cnpj: String?,
    @field: NotBlank var dataNascimento: LocalDateTime?,
    @field: NotBlank var dataInicio: LocalDateTime?,
    @field: NotBlank var endereco: String?,
    @field: NotBlank var cep: String?,
    var descricao: String?,
    var inscricaoEstadual: String?,
    @field: NotBlank var isProBono: Boolean?,
    @field: NotBlank var isAtivo: Boolean?,
    @field: NotBlank var isJuridico: Boolean?,
)



