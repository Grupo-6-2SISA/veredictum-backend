package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import org.springframework.data.jpa.repository.JpaRepository

interface LogEnvioLembreteRepository: JpaRepository<LogEnvioLembrete, Int> {

}