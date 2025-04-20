package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UsuarioRepository: JpaRepository<Usuario, Int> {

    @Query("SELECT u FROM Usuario u ORDER BY u.isAtivo DESC")
    fun findAllOrderByIsAtivo() : List<Usuario>

    fun findByEmailAndSenha(email: String, senha: String): Usuario?

    fun findByAdministradorOrderByIsAtivo(administrador: Usuario): List<Usuario>

    fun findAllByAdministradorNullOrderByIsAtivo(): List<Usuario>

    @Query("SELECT u FROM Usuario u WHERE u.idUsuario = ?1  and u.isAdm = TRUE")
    fun findAdministradorById(id: Int): Usuario?

}