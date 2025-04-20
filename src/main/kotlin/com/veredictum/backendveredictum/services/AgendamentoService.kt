package com.veredictum.backendveredictum.services

import com.veredictum.backendveredictum.repository.*
import org.springframework.stereotype.Service

@Service
class AgendamentoService (
    val agendamentoRepository: AtendimentoRepository,
    val clienteRepository: ClienteRepository,
    val usuarioRepository: UsuarioRepository,
    val historicoAgendamentoRepository: HistoricoAgendamentoRepository,
) {



}