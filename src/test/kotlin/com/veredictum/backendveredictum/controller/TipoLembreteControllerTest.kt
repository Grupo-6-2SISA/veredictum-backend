package com.veredictum.backendveredictum.controller

import com.veredictum.backendveredictum.entity.TipoLembrete
import com.veredictum.backendveredictum.repository.TipoLembreteRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*
import kotlin.test.assertEquals

@WebMvcTest(TipoLembreteController::class)
class TipoLembreteControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var repository: TipoLembreteRepository


    @Test
    fun `deve retornar 204 quando lista de tipos estiver vazia`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get("/tipo-lembrete")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(repository).findAll()
    }

    @Test
    fun `deve retornar tipo por ID com sucesso`() {
        val tipo = TipoLembrete(1, "Aniversário")
        `when`(repository.findById(1)).thenReturn(Optional.of(tipo))

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/tipo-lembrete/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        verify(repository).findById(1)
        assertEquals("{\"idTipoLembrete\":1,\"tipo\":\"Aniversário\"}", result.response.contentAsString)
    }

    @Test
    fun `deve retornar 404 quando tipo por ID nao for encontrado`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.get("/tipo-lembrete/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(repository).findById(1)
    }
    @Test
    fun `deve excluir um tipo existente com sucesso`() {
        val tipoExistente = TipoLembrete(1, "Aniversário")
        `when`(repository.findById(1)).thenReturn(Optional.of(tipoExistente))
        doNothing().`when`(repository).delete(tipoExistente)

        mockMvc.perform(MockMvcRequestBuilders.delete("/tipo-lembrete/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        verify(repository).findById(1)
        verify(repository).delete(tipoExistente)
    }

    @Test
    fun `deve retornar 404 ao tentar excluir um tipo inexistente`() {
        `when`(repository.findById(1)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.delete("/tipo-lembrete/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(repository).findById(1)
    }
}