package com.taqucinco.pokemon_sample.feature.pokemon

import androidx.room.Room
import com.taqucinco.pokemon_sample.error.PokemonError
import com.taqucinco.pokemon_sample.feature.database.AppDatabase
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

// NOTE: ユニットテストにてテスト用のContextを使うためRobolectricで実行する
@RunWith(RobolectricTestRunner::class)
class PokemonLocalRepositoryTest {

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
    fun testCaptureAndGet() = runTest {
        val repo = PokemonLocalRepositoryImpl(
            dbFactory = DatabaseFactoryImpl(context = context)
        )

        val pokemon = object : PokemonResource {
            override val name = "foo"
            override val url = "https://example.com/1"
        }

        // ポケモンをモンスターボールに捕獲して、それをrepo経由で取り出せるか
        repo.captureWithPokeBall(pokemon)
        val captured = repo.getPokemonsInPokeBall().first()
        assertEquals("foo", captured.name)
        assertEquals("https://example.com/1", captured.url)
    }

    @Test
    fun testCapture_ThrowFullFilledError_when3PokemonsAlreadyCaptured() = runTest {
        val repo = PokemonLocalRepositoryImpl(
            dbFactory = DatabaseFactoryImpl(context = context)
        )

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

        pokemons.forEach {
            repo.captureWithPokeBall(it)
        }

        // 3つ以上のポケモンを入れようとしてエラーになるか
        try {
            repo.captureWithPokeBall(object : PokemonResource {
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
    fun testRelease() = runTest {
        val repo = PokemonLocalRepositoryImpl(
            dbFactory = DatabaseFactoryImpl(context = context)
        )

        // 捕獲した後に解放する
        repo.captureWithPokeBall(object : PokemonResource {
            override val name = "foo"
            override val url = "https://example.com/1"
        })
        repo.releaseFromPokeBall("foo")

        // モンスターボールが空になっているか
        assertEquals(0, repo.getPokemonsInPokeBall().size)
    }
}