package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.*
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.text.get

@WebMvcTest(LogEnvioLembreteController::class)
class LogEnvioLembreteControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var repository: LogEnvioLembreteRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    private fun criarCliente(): Cliente {
        return Cliente(
            idCliente = 1,
            nome = "Cliente Teste"
        )
    }

    private fun criarUsuario(): Usuario {
        return Usuario(
            idUsuario = 1,
            nome = "Usuário Teste"
        )
    }

    private fun criarConta(): Conta {
        return Conta(
            idConta = 1,
            usuario = criarUsuario(),
            dataCriacao = LocalDate.now(),
            etiqueta = "Etiqueta Teste",
            valor = 100.0,
            dataVencimento = LocalDate.now().plusDays(10),
            urlNuvem = "http://teste.com",
            descricao = "Descrição Teste",
            isPago = false
        )
    }

    private fun criarNotaFiscal(): NotaFiscal {
        return NotaFiscal(
            idNotaFiscal = 1,
            cliente = criarCliente(),
            dataCriacao = LocalDate.now(),
            etiqueta = "Etiqueta NF",
            valor = 200.0,
            dataVencimento = LocalDate.now().plusDays(15),
            descricao = "Descrição NF",
            urlNuvem = "http://notafiscal.com",
            isEmitida = true
        )
    }

    private fun criarAtendimento(): Atendimento {
        return Atendimento(
            idAtendimento = 1,
            cliente = criarCliente(),
            usuario = criarUsuario(),
            etiqueta = "Etiqueta Atendimento",
            valor = 300.0,
            descricao = "Descrição Atendimento",
            dataInicio = LocalDateTime.now(),
            dataFim = LocalDateTime.now().plusHours(2),
            dataVencimento = LocalDateTime.now().plusDays(5),
            isPago = true
        )
    }

    @Test
    fun `deve retornar lista de logs com sucesso`() {
        val tipoLembrete = TipoLembrete(1, "Aniversário")
        val logs: List<LogEnvioLembrete> = listOf(
            LogEnvioLembrete(
                idLogEnvioLembrete = 1,
                tipoLembrete = tipoLembrete,
                conta = criarConta(),
                notaFiscal = criarNotaFiscal(),
                atendimento = criarAtendimento(),
                dataHoraCriacao = LocalDateTime.parse("2023-10-01T10:00:00"),
                mensagem = "Envio realizado"
            ),
            LogEnvioLembrete(
                idLogEnvioLembrete = 2,
                tipoLembrete = tipoLembrete,
                conta = criarConta(),
                notaFiscal = criarNotaFiscal(),
                atendimento = criarAtendimento(),
                dataHoraCriacao = LocalDateTime.parse("2023-10-02T11:00:00"),
                mensagem = "Erro no envio"
            )
        )
        `when`(repository.findAll()).thenReturn(logs)

        mockMvc.perform(MockMvcRequestBuilders.get("/log-envio-lembrete")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].idLogEnvioLembrete").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].mensagem").value("Envio realizado"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao").value("2023-10-01T10:00:00"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].idLogEnvioLembrete").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].mensagem").value("Erro no envio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao").value("2023-10-02T11:00:00"))

        verify(repository).findAll()
    }

    @Test
    fun `deve retornar 404 ao buscar log por ID inexistente`() {
        `when`(repository.findById(999)).thenReturn(java.util.Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.get("/log-envio-lembrete/999")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `deve retornar 400 ao tentar criar log com dados inválidos`() {
        val logInvalidoJson = """
        {
            "mensagem": "",
            "dataHoraCriacao": "2023-10-01T10:00:00"
        }
    """.trimIndent()

        mockMvc.perform(MockMvcRequestBuilders.post("/log-envio-lembrete")
            .contentType(MediaType.APPLICATION_JSON)
            .content(logInvalidoJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `deve retornar 404 ao tentar excluir log inexistente`() {
        `when`(repository.findById(999)).thenReturn(java.util.Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.delete("/log-envio-lembrete/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }



}