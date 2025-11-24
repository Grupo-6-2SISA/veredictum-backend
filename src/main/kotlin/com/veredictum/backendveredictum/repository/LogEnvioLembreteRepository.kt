package com.veredictum.backendveredictum.repository

import com.veredictum.backendveredictum.dto.ContasPorAnoDTO
import com.veredictum.backendveredictum.dto.ListagemLogsDTO
import com.veredictum.backendveredictum.entity.LogEnvioLembrete
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface LogEnvioLembreteRepository: JpaRepository<LogEnvioLembrete, Int> {

    @Query("""
    SELECT C.ID_TIPO_LEMBRETE AS idTipo, 
    C.TIPO AS tipo, 
    DATE_FORMAT(A.DATA_HORA_CRIACAO, '%d/%m/%Y - %H:%i:%s')AS dataEnvio, 
    A.MENSAGEM AS mensagem, 
    B.NOME AS clienteRelacionado
	FROM LOG_ENVIO_LEMBRETE A 
	JOIN CLIENTE B
    ON A.FK_CLIENTE = B.ID_CLIENTE
    JOIN TIPO_LEMBRETE C
    ON A.FK_TIPO_LEMBRETE = C.ID_TIPO_LEMBRETE
    ORDER BY A.DATA_HORA_CRIACAO DESC;
""", nativeQuery = true)
    fun listagemLogs(): List<ListagemLogsDTO>

    @Query("""
        select id_log_envio_lembrete as id, funcionou, fk_atendimento as id_atendimento, fk_cliente as id_cliente, fk_conta as id_conta, fk_tipo_lembrete as id_tipo_lembrete, b.tipo, mensagem, data_hora_criacao from log_envio_lembrete a join tipo_lembrete b on b.id_tipo_lembrete = a.fk_tipo_lembrete
    """, nativeQuery = true)
    fun csvLogs(): List<Array<Any>>

}