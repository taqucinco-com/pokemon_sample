package com.taqucinco.pokemon_sample.feature.pokemon

import androidx.room.Room
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
class PokeBallRepositoryTest {

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
    fun testGetAndInsert() = runTest {
        val repo = PokeBallRepositoryImpl(dbFactory = DatabaseFactoryImpl(context = context))

        val pokemon = PokemonResourceEntity(name = "foo", url = "https://example.com/1")

        // ポケモンをモンスターボールに捕獲して、それをrepo経由で取り出せるか
        repo.insert(pokemon)
        val captured = repo.getAll().first()
        assertEquals("foo", captured.name)
        assertEquals("https://example.com/1", captured.url)
    }

    @Test
    fun testDelete() = runTest {
        val repo = PokeBallRepositoryImpl(dbFactory = DatabaseFactoryImpl(context = context))

        val pokemon = PokemonResourceEntity(name = "foo", url = "https://example.com/1")

        repo.insert(pokemon)
        val inserted = repo.getAll()
        assertEquals(1, inserted.size)

        repo.delete(pokemon)
        val deleted = repo.getAll()
        assertEquals(0, deleted.size)
    }
}