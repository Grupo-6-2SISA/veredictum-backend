package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.Atendimento
import org.springframework.data.jpa.repository.JpaRepository

interface AtendimentoRepository: JpaRepository<Atendimento, Int> {

}