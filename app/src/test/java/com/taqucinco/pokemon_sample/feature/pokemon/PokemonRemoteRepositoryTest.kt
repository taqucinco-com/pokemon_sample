package com.taqucinco.pokemon_sample.feature.pokemon

import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PokemonRemoteRepositoryTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGetList_HappyPass() = runBlocking {
        // 想定レスポンスを返すRestClientのモックを生成
        val mock = mock<RestClientInterface> { }
        val body = """
{
    "count":100,
    "next": "https://pokeapi.co/api/v2/pokemon/?offset=2&limit=2",
    "previous": null,
    "results":[
        {"name":"bulbasaur","url":"https://pokeapi.co/api/v2/pokemon/1/"},
        {"name":"ivysaur","url":"https://pokeapi.co/api/v2/pokemon/2/"}
    ]
}
"""
        val stubResponse = RestClientInterface.Response(
            status = 200,
            headers = mapOf("Content-Type" to "application/json; charset=utf-8"),
            data = body.toByteArray(Charsets.UTF_8)
        )
        whenever(mock.get(anyOrNull(), anyOrNull(), anyOrNull())).doSuspendableAnswer {
            return@doSuspendableAnswer stubResponse
        }

        // リストを取得する
        val repo = PokemonRemoteRepositoryImpl(restClient = mock)
        val response = repo.getList()

        // 得られたレスポンスが正しいか検証する
        assertEquals(100, response.count)
        val bulbasaur = response.results[0]
        assertEquals("bulbasaur", bulbasaur.name)
        assertEquals("https://pokeapi.co/api/v2/pokemon/1/", bulbasaur.url)
        val ivysaur = response.results[1]
        assertEquals("ivysaur", ivysaur.name)
        assertEquals("https://pokeapi.co/api/v2/pokemon/2/", ivysaur.url)
    }

    @Test
    fun testGetPikachu_HappyPass() = runBlocking {
        // 想定レスポンスを返すRestClientのモックを生成
        val mock = mock<RestClientInterface> { }
        val body = PokemonRemoteMother.pikachuJson()
        val stubResponse = RestClientInterface.Response(
            status = 200,
            headers = mapOf("Content-Type" to "application/json; charset=utf-8"),
            data = body.toByteArray(Charsets.UTF_8)
        )
        whenever(mock.get(anyOrNull(), anyOrNull(), anyOrNull())).doSuspendableAnswer {
            return@doSuspendableAnswer stubResponse
        }

        // リストを取得する
        val repo = PokemonRemoteRepositoryImpl(restClient = mock)
        val response = repo.getPokemon(25)

        // 得られたレスポンスが正しいか検証する
        assertEquals("pikachu", response.name)
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png", response.sprites.frontDefault)
    }
}