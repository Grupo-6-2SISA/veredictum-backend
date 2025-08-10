package com.veredictum.backendveredictum.dto

import java.time.LocalDate

data class ClienteDTO (
    val idCliente: Int,
    val nome: String,
    val fkIndicador: Int?,
    val email: String,
    val rg: String,
    val cpf: String?,
    val cnpj: String?,
    val telefone: String?,
    val dataNascimento: LocalDate?,
    val dataInicio: LocalDate?,
    val cep: String?,
    val logradouro: String?,
    val bairro: String?,
    val localidade: String?,
    val numero: String?,
    val complemento: String?,
    val descricao: String?,
    val inscricaoEstadual: String?,
    val isProBono: Boolean?,
    val isAtivo: Boolean,
    val isJuridico: Boolean?,
    )