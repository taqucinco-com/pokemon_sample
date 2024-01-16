package com.taqucinco.pokemon_sample.feature.pokemon

import androidx.room.Room
import com.taqucinco.pokemon_sample.error.PokemonError
import com.taqucinco.pokemon_sample.feature.database.AppDatabase
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class PokeBallServiceTest {

    private val context = RuntimeEnvironment.getApplication()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO) {
            val db = Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java
            ).build()
            val dao = db.pokeBallDao()
            dao.drop()
            db.close()
        }
    }

    @Test
    fun testCapture_ThrowFullFilledError_when3PokemonsAlreadyCaptured() = runTest {

        val mock = mock<PokeBallRepository> { }

        whenever(mock.getAll()).doSuspendableAnswer {
            val pokemons = listOf(
                object : PokemonResource {
                    override val name = "foo"
                    override val url = "https://example.com/1"
                },
                object : PokemonResource {
                    override val name = "bar"
                    override val url = "https://example.com/2"
                },
                object : PokemonResource {
                    override val name = "foobar"
                    override val url = "https://example.com/3"
                },
            )
            return@doSuspendableAnswer pokemons
        }
        val service = PokeBallService(mock)

        // 3つ以上のポケモンを入れようとしてエラーになるか
        try {
            service.captureWithPokeBall(object : PokemonResource {
                override val name = "barfoo"
                override val url = "https://example.com/4"
            })
            fail("should throw PokemonError.PokeBallFullFilled")
        } catch (t: Throwable) {
            assertTrue(t is PokemonError.PokeBallFullFilled)
            return@runTest
        }
    }

    @Test
    fun testRelease_Holding0Pokemons_WhenOneExist() = runTest {

        val mock = mock<PokeBallRepository> { }
        val service = PokeBallService(mock)

        val pokemon = object : PokemonResource {
            override val name = "foo"
            override val url = "https://example.com/1"
        }
        val entity = PokemonResourceEntity(name = pokemon.name, url = pokemon.url)
        whenever(mock.findBy(pokemon.name)).doSuspendableAnswer {
            return@doSuspendableAnswer entity
        }
        service.releaseFromPokeBall(pokemon.name)

        verify(mock, times(1)).findBy(pokemon.name)
        verify(mock, times(1)).delete(entity)
    }

    @Test
    fun testRelease_Holding0Pokemons_WhenDbInserting() = runTest {
        val repo = PokeBallRepositoryImpl(dbFactory = DatabaseFactoryImpl(context = context))
        val service = PokeBallService(repo)

        // 捕獲した後に解放する
        service.captureWithPokeBall(object : PokemonResource {
            override val name = "foo"
            override val url = "https://example.com/1"
        })
        assertEquals(1, repo.getAll().size)

        // モンスターボールが空になっているか
        service.releaseFromPokeBall("foo")
        assertEquals(0, repo.getAll().size)
    }
}