package com.taqucinco.pokemon_sample.feature.pokemon

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.internal.toImmutableMap
import com.taqucinco.pokemon_sample.feature.pokemon.Pokemon as PokemonModel

sealed class PokemonApi {

    sealed class PokemonList {
        companion object {
            // https://pokeapi.co/docs/v2#resource-listspagination-section
            const val endpoint: String = "https://pokeapi.co/api/v2/pokemon/"
            const val method = "GET"
        }

        data class Request(
            val limit: Int? = null,
            val offset: Int? = null,
        ) {
            val queryParameter: Map<String, Any>
                get() {
                    val map = mutableMapOf<String, Any>()
                    limit?.let { map.put("limit", it) }
                    offset?.let { map.put("offset", it) }
                    return map.toImmutableMap()
                }
        }

        @Serializable
        data class Response(
            @Required val count: Int,
            @Required val results: List<Result>,
        ) {
            @Serializable
            data class Result(
                @Required override val name: String,
                @Required override val url: String,
            ): PokemonResource
        }
    }

    sealed class Pokemon {
        companion object {
            // https://pokeapi.co/docs/v2#pokemon
            const val endpoint: String = "https://pokeapi.co/api/v2/pokemon/{id_or_name}/"
            const val method = "GET"
        }

        data class Request(
            val id: Int? = null,
            val name: String? = null,
        ) {
            init {
                if (id == null && name == null) throw Exception("one of props must be non-null.")
                if (id != null && name != null) throw Exception("both of props should not be non-null simultaneously.")
            }

            fun url(): String {
                val regex = """\{id_or_name\}\/""".toRegex()
                return endpoint.replace(regex, (id?.toString() ?: name) ?: "") + "/"
            }
        }

        @Serializable
        data class Response(
            @Required override val id: Int,
            @Required override val name: String,
            @Required override val sprites: Sprites,
        ): PokemonModel {
            @Serializable
            data class Sprites(
                @Required @SerialName("front_default") override val frontDefault: String // URL
            ): PokemonModel.PokemonSprites
        }
    }
}
