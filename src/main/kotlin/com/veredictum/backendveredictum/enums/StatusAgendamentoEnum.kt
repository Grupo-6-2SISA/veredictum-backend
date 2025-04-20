package com.veredictum.backendveredictum.enums

enum class StatusAgendamentoEnum(val id: Int, val descricao: String) {
    AGENDADO(1,"Agendado"),
    CONCLUIDO(2,"Concluído"),
    CANCELADO(3,"Cancelado"),
    ATRASADO(4,"Atrasado");

    companion object {
        fun fromDescricao(value: String): StatusAgendamentoEnum? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }

        fun fromId(id: Int): StatusAgendamentoEnum? {
            return entries.find { it.id == id }
        }

    }
}
