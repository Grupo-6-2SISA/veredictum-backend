package com.veredictum.backendveredictum.enums

enum class TipoLembreteEnum(val id: Int, val tipo: String) {

    ATENDIMENTO(1, "Atendimento"),
    NOTA_FISCAL(2, "Nota Fiscal"),
    CONTA(3, "Conta"),
    ANIVERSARIO(4, "Aniversário");

    companion object {
        fun fromTipo(value: String): TipoLembreteEnum? {
            return entries.find { it.tipo.equals(value, ignoreCase = true) }
        }

        fun fromId(id: Int): TipoLembreteEnum? {
            return entries.find { it.id == id }
        }
    }

}