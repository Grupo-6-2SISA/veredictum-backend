package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Rotina
import com.veredictum.backendveredictum.entity.StatusAgendamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RotinaRepository: JpaRepository<Rotina, Int> {
}