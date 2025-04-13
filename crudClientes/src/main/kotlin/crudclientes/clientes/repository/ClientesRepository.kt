package crudclientes.clientes.repository

import crudclientes.clientes.entity.Clientes
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface ClientesRepository: JpaRepository<Clientes, Int> {
    fun findByNomeContainingIgnoreCaseOrderByNomeAsc(nome: String): List<Clientes>

    override fun findById(id: Int): Optional<Clientes>
}