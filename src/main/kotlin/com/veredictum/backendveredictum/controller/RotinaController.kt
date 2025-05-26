package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.Rotina
import com.veredictum.backendveredictum.repository.RotinaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/rotinas")
class RotinaController(private val rotinaRepository: RotinaRepository) {

    @GetMapping
    fun listarTodas(): ResponseEntity<List<Rotina>> {
        val rotinas = rotinaRepository.findAll()
        return ResponseEntity.ok(rotinas)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Rotina> {
        val rotina = rotinaRepository.findById(id)
        return if (rotina.isPresent) {
            ResponseEntity.ok(rotina.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun criar(@RequestBody rotina: Rotina): ResponseEntity<Rotina> {
        val novaRotina = rotinaRepository.save(rotina)
        return ResponseEntity.status(201).body(novaRotina)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody rotinaAtualizada: Rotina): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotinaExistente ->
            rotinaAtualizada.idRotina = rotinaExistente.idRotina
            val rotinaSalva = rotinaRepository.save(rotinaAtualizada)
            ResponseEntity.ok(rotinaSalva)
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return if (rotinaRepository.existsById(id)) {
            rotinaRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/{id}/ativar")
    fun ativar(@PathVariable id: Int): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotina ->
            rotina.isAtivo = true
            val rotinaAtualizada = rotinaRepository.save(rotina)
            ResponseEntity.ok(rotinaAtualizada)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PatchMapping("/{id}/inativar")
    fun inativar(@PathVariable id: Int): ResponseEntity<Rotina> {
        return rotinaRepository.findById(id).map { rotina ->
            rotina.isAtivo = false
            val rotinaAtualizada = rotinaRepository.save(rotina)
            ResponseEntity.ok(rotinaAtualizada)
        }.orElse(ResponseEntity.notFound().build())
    }
}