package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.StatusAgendamento
import org.springframework.data.jpa.repository.JpaRepository

interface StatusAgendamentoRepository: JpaRepository<StatusAgendamento, Int> {

}
