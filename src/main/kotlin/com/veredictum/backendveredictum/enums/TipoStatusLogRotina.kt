package com.veredictum.backendveredictum.enums

enum class TipoStatusLogRotina(val id: Int, val tipo: String) {

    EM_ANDAMENTO(1, "Em Andamento"),
    CONCLUIDA(2, "Concluída"),
    CANCELADA(3, "Cancelada");

    companion object {
        fun fromTipo(value: String): TipoStatusLogRotina? {
            return entries.find { it.tipo.equals(value, ignoreCase = true) }
        }

        fun fromId(id: Int): TipoStatusLogRotina? {
            return entries.find { it.id == id }
        }
    }

}