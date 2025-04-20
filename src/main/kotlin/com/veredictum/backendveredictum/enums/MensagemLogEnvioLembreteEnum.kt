package com.veredictum.backendveredictum.enums

enum class MensagemLogEnvioLembreteEnum (val id: Int, val mensagem: String) {
    ENVIADO(1, "Lembrete enviado com sucesso"),
    FALHA_AO_ENVIAR(2, "Falha ao enviar lembrete");

    companion object {
        fun fromMensagem(value: String): MensagemLogEnvioLembreteEnum? {
            return entries.find { it.mensagem.equals(value, ignoreCase = true) }
        }

        fun fromId(id: Int): MensagemLogEnvioLembreteEnum? {
            return entries.find { it.id == id }
        }
    }
}