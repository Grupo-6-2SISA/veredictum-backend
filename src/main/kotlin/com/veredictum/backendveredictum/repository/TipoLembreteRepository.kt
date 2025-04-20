package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.TipoLembrete
import org.springframework.data.jpa.repository.JpaRepository

interface TipoLembreteRepository: JpaRepository<TipoLembrete, Int> {

}