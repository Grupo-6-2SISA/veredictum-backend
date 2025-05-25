package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.entity.Usuario
import com.veredictum.backendveredictum.repository.UsuarioRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional // Importe Optional

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository
) {

    fun findById(id: Int): Optional<Usuario> { // Renomeado e alterado o tipo de retorno para Optional
        return usuarioRepository.findById(id)
    }

    fun listarTodos(): List<Usuario> {
        return usuarioRepository.findAllOrderByIsAtivo()
    }

    fun criarUsuario(usuario: Usuario): Usuario {
        return usuarioRepository.save(usuario)
    }


    @Transactional
    fun atualizarUsuario(id: Int, usuarioAtualizado: Usuario): Usuario? {
        val usuarioExistente = usuarioRepository.findById(id).orElse(null) ?: return null

        usuarioExistente.nome = usuarioAtualizado.nome
        usuarioExistente.email = usuarioAtualizado.email
        usuarioExistente.senha = usuarioAtualizado.senha
        usuarioExistente.isAtivo = usuarioAtualizado.isAtivo
        usuarioExistente.isAdm = usuarioAtualizado.isAdm

        return usuarioRepository.save(usuarioExistente)
    }

    fun deletarUsuario(id: Int) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
        }
    }


    fun buscarPorEmailESenha(email: String, senha: String): Usuario? {
        return usuarioRepository.findByEmailAndSenha(email, senha)
    }


    fun listarAdministradores(): List<Usuario> {
        return usuarioRepository.findAllByAdministradorNullOrderByIsAtivo()
    }
}
