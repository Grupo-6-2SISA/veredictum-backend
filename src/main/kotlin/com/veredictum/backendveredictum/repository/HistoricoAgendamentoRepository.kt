package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import org.springframework.data.jpa.repository.JpaRepository

interface HistoricoAgendamentoRepository: JpaRepository<HistoricoStatusAgendamento, Int> {

}