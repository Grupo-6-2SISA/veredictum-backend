package crudclientes.clientes.controller

import crudclientes.clientes.entity.Clientes
import crudclientes.clientes.repository.ClientesRepository
import jakarta.validation.Valid
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
@RequestMapping("/clientes")
class ClientesController(val repositorio: ClientesRepository) {

    @GetMapping
    fun buscar(@RequestParam(required = false) nome: String?): ResponseEntity<List<Clientes>> {
        val clientes = if (nome.isNullOrBlank()) {
            repositorio.findAll(Sort.by(Sort.Order.asc("nome")))
        } else {
            repositorio.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome)
        }

        return if (clientes.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(clientes)
        }
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Clientes> {
        val clientes = repositorio.findById(id)
        return ResponseEntity.of(clientes)
    }

    @PostMapping
    fun cadastrar(@RequestBody @Valid novoCliente: Clientes): ResponseEntity<Clientes> {
        val cliente = repositorio.save(novoCliente)
        return ResponseEntity.status(201).body(novoCliente)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody clientesAtualizados: Clientes): ResponseEntity<Clientes> {
        if (repositorio.existsById(id)) {
            clientesAtualizados.id
            val clientes = repositorio.save(clientesAtualizados)
            return ResponseEntity.status(200).body(clientesAtualizados)
        } else {
            return ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/{id}")
    fun atualizarParcial(
        @PathVariable id: Int,
        @RequestBody atualizacoes: Map<String, Any>
    ): ResponseEntity<Clientes> {
        val clienteOptional = repositorio.findById(id)
        if (clienteOptional.isEmpty) return ResponseEntity.status(404).build()

        val cliente = clienteOptional.get()

        atualizacoes.forEach { (chave, valor) ->
            when (chave) {
                "nome" -> cliente.nome = valor.toString()
                "email" -> cliente.email = valor.toString()
                "rg" -> cliente.rg = valor.toString()
                "cpf" -> cliente.cpf = valor.toString()
                "cnpj" -> cliente.cnpj = valor.toString()
                "dataNascimento" -> cliente.dataNascimento = LocalDateTime.parse(valor.toString())
                "dataInicio" -> cliente.dataInicio = LocalDateTime.parse(valor.toString())
                "endereco" -> cliente.endereco = valor.toString()
                "cep" -> cliente.cep = valor.toString()
                "descricao" -> cliente.descricao = valor.toString()
                "inscricaoEstadual" -> cliente.inscricaoEstadual = valor.toString()
                "proBono" -> cliente.isProBono = valor.toString().toBoolean()
                "ativo" -> cliente.isAtivo = valor.toString().toBoolean()
                "juridico" -> cliente.isJuridico = valor.toString().toBoolean()
                else -> {
                }
            }
        }

        repositorio.save(cliente)
        return ResponseEntity.status(200).body(cliente)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id)
            return ResponseEntity.status(204).build()
        }
        return ResponseEntity.status(404).build()
    }

}

