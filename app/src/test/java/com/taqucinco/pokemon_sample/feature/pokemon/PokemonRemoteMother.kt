package com.taqucinco.pokemon_sample.feature.pokemon

import java.io.BufferedReader
import java.io.InputStreamReader

class PokemonRemoteMother {
    companion object {
        fun pikachuJson(): String {
            val inputStream = ClassLoader.getSystemResourceAsStream("pikachu_25.json") ?: throw Exception("fail")
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                return reader.readText()
            }
        }
    }
}