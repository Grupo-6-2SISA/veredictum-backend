package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.entity.LogExecucaoRotina
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogExecucaoRotinaRepository: JpaRepository<LogExecucaoRotina, Int>  {

}