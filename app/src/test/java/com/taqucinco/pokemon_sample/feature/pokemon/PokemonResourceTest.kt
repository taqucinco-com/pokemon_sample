package com.taqucinco.pokemon_sample.feature.pokemon

import org.junit.Assert.*
import org.junit.Test


class PokemonResourceTest {
    @Test
    fun testExtractId_CorrectId_FromUrl() {
        assertEquals(33,
            object : PokemonResource {
                override val name: String
                    get() = "foo"
                override val url: String
                    get() = "https://pokeapi.co/api/v2/pokemon/33/"
            }.extractId()
        )

        assertEquals(111,
            object : PokemonResource {
                override val name: String
                    get() = "foo"
                override val url: String
                    get() = "https://subdomein.pokeapi.co/api/v1000/pokemon/111/"
            }.extractId()
        )

        assertNull(
            object : PokemonResource {
                override val name: String
                    get() = "foo"
                override val url: String
                    get() = "https://yahoo.co.jp"
            }.extractId()
        )
    }
}