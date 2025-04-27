package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.HistoricoStatusAgendamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface HistoricoStatusAgendamentoRepository: JpaRepository<HistoricoStatusAgendamento, Int> {


}