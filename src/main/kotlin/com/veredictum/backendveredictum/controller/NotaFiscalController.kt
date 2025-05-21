package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.NotaFiscal
import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import com.veredictum.backendveredictum.repository.NotaFiscalRepository
import com.veredictum.backendveredictum.dto.NotaFiscalDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import jakarta.validation.Valid
import java.time.LocalDateTime
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository

@RestController
@RequestMapping("/notas-fiscais")
class NotaFiscalController(
    private val notaFiscalRepository: NotaFiscalRepository,
    private val logEnvioLembreteRepository: LogEnvioLembreteRepository,
) {

    @GetMapping
    fun listarNotasFiscais(): ResponseEntity<List<NotaFiscalDTO>> {
        val notasFiscais = notaFiscalRepository.findAll()
        val notasFiscaisDTO = notasFiscais.map { it.toDTO() }
        return ResponseEntity.ok(notasFiscaisDTO)
    }

    @GetMapping("/{id}")
    fun buscarNotaFiscalPorId(@PathVariable id: Int): ResponseEntity<NotaFiscalDTO> {
        return notaFiscalRepository.findById(id)
            .map { it -> ResponseEntity.ok(it.toDTO()) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun criarNotaFiscal(@RequestBody @Valid notaFiscalDTO: NotaFiscalDTO): ResponseEntity<NotaFiscalDTO> {

        val novaNotaFiscal = NotaFiscal(

            dataCriacao = notaFiscalDTO.dataCriacao,
            etiqueta = notaFiscalDTO.etiqueta,
            valor = notaFiscalDTO.valor,
            dataVencimento = notaFiscalDTO.dataVencimento,
            descricao = notaFiscalDTO.descricao,
            urlNuvem = notaFiscalDTO.urlNuvem,
            isEmitida = notaFiscalDTO.isEmitida
        )


        val notaFiscalSalva = notaFiscalRepository.save(novaNotaFiscal)


        return ResponseEntity.status(HttpStatus.CREATED).body(notaFiscalSalva.toDTO())
    }

    @PutMapping("/{id}")
    fun atualizarNotaFiscal(
        @PathVariable id: Int,
        @RequestBody @Valid notaFiscalDTO: NotaFiscalDTO
    ): ResponseEntity<NotaFiscalDTO> {
        if (notaFiscalDTO.idNotaFiscal != null && notaFiscalDTO.idNotaFiscal != id) {
            return ResponseEntity.badRequest().build()
        }

        return notaFiscalRepository.findById(id).map { notaFiscalExistente ->

            val notaFiscalAtualizada = notaFiscalExistente.copy(
                //cliente = cliente, // Removido
                dataCriacao = notaFiscalDTO.dataCriacao,
                etiqueta = notaFiscalDTO.etiqueta,
                valor = notaFiscalDTO.valor,
                dataVencimento = notaFiscalDTO.dataVencimento,
                descricao = notaFiscalDTO.descricao,
                urlNuvem = notaFiscalDTO.urlNuvem,
                isEmitida = notaFiscalDTO.isEmitida
            )
            val notaFiscalSalva = notaFiscalRepository.save(notaFiscalAtualizada)
            ResponseEntity.ok(notaFiscalSalva.toDTO())
        }.orElse(ResponseEntity.notFound().build())
    }



    private fun NotaFiscal.toDTO(): NotaFiscalDTO {
        return NotaFiscalDTO(
            idNotaFiscal = this.idNotaFiscal,
            fkCliente = this.cliente?.idCliente,
            dataCriacao = this.dataCriacao,
            etiqueta = this.etiqueta,
            valor = this.valor,
            dataVencimento = this.dataVencimento,
            descricao = this.descricao,
            urlNuvem = this.urlNuvem,
            isEmitida = this.isEmitida
        )
    }
}
