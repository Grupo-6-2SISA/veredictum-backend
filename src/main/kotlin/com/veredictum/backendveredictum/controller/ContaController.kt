package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Conta
import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import com.veredictum.backendveredictum.repository.ContaRepository
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository// Importe o LogRepository
import com.veredictum.backendveredictum.repository.UsuarioRepository // Importe o UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/contas")
class ContaController(
    private val contaRepository: ContaRepository,
    private val logRepository: LogEnvioLembreteRepository, // Injete o LogRepository
    private val usuarioRepository: UsuarioRepository // Injete o UsuarioRepository
) {

    @PostMapping
    fun criarConta(@RequestBody conta: Conta): ResponseEntity<Conta> {
        // Valida se o usuário existe
        val usuario = conta.usuario?.idUsuario?.let { usuarioId ->
            usuarioRepository.findById(usuarioId).orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário com ID $usuarioId não encontrado")
            }
        } ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário deve ser informado")

        val novaConta = Conta(
            usuario = usuario,
            dataCriacao = LocalDate.now(),
            etiqueta = conta.etiqueta,
            valor = conta.valor,
            dataVencimento = conta.dataVencimento,
            urlNuvem = conta.urlNuvem,
            descricao = conta.descricao,
            isPago = conta.isPago
        )
        val contaSalva = contaRepository.save(novaConta)


        return ResponseEntity.status(HttpStatus.CREATED).body(contaSalva)
    }
}
