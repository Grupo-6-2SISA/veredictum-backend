import com.fasterxml.jackson.databind.ObjectMapper
import com.veredictum.backendveredictum.controller.LogEnvioLembreteController
import com.veredictum.backendveredictum.entity.*
import com.veredictum.backendveredictum.repository.LogEnvioLembreteRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class LogEnvioLembreteControllerMockTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var repository: LogEnvioLembreteRepository

    @InjectMocks
    private lateinit var controller: LogEnvioLembreteController

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    private fun criarCliente() = Cliente(
        idCliente = 1,
        indicador = null,
        nome = "Cliente Teste",
        email = "cliente@teste.com",
        rg = "123456",
        cpf = "12345678901",
        cnpj = null,
        telefone = "11999999999",
        dataNascimento = LocalDate.now().minusYears(30),
        dataInicio = LocalDate.now(),
        endereco = "Rua Teste",
        cep = "12345678",
        descricao = "Cliente de teste",
        inscricaoEstadual = "123456789",
        isProBono = false,
        isAtivo = true,
        isJuridico = false
    )

    private fun criarUsuario() = Usuario(
        idUsuario = 1,
        nome = "Usuário Teste",
        email = "usuario@teste.com",
        senha = "senha123",
        isAtivo = true,
        isAdm = false,
        administrador = null
    )

    private fun criarConta() = Conta(
        idConta = 1,
        dataCriacao = LocalDate.now(),
        etiqueta = "Etiqueta Teste",
        valor = 100.0,
        dataVencimento = LocalDate.now().plusDays(10),
        urlNuvem = "http://teste.com",
        descricao = "Descrição Teste",
        isPago = false
    )

    private fun criarNotaFiscal() = NotaFiscal(
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

    private fun criarAtendimento() = Atendimento(
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

    @Test
    fun `deve retornar lista de logs com sucesso`() {
        val tipoLembrete = TipoLembrete(1, "Aniversário")
        val logs = listOf(
            LogEnvioLembrete(
                idLogEnvioLembrete = 1,
                tipoLembrete = tipoLembrete,
                notaFiscal = criarNotaFiscal(),
                conta = criarConta(),
                atendimento = criarAtendimento(),
                cliente = criarCliente(),
                dataHoraCriacao = LocalDateTime.parse("2023-10-01T10:00:00"),
                mensagem = "Envio realizado"
            ),
            LogEnvioLembrete(
                idLogEnvioLembrete = 2,
                tipoLembrete = tipoLembrete,
                notaFiscal = criarNotaFiscal(),
                conta = criarConta(),
                atendimento = criarAtendimento(),
                cliente = criarCliente(),
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
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao[0]").value(2023))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao[1]").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao[2]").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao[3]").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].dataHoraCriacao[4]").value(0))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].idLogEnvioLembrete").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].mensagem").value("Erro no envio"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao[0]").value(2023))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao[1]").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao[2]").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao[3]").value(11))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].dataHoraCriacao[4]").value(0))

        verify(repository).findAll()
    }

    @Test
    fun `deve retornar 404 ao buscar log por ID inexistente`() {
        `when`(repository.findById(999)).thenReturn(Optional.empty())

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
        `when`(repository.findById(999)).thenReturn(Optional.empty())

        mockMvc.perform(MockMvcRequestBuilders.delete("/log-envio-lembrete/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}