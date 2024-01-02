package com.taqucinco.pokemon_sample.feature.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallDao
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResourceEntity
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MonsterBallTableTest {

    private lateinit var pokeBallDao: PokeBallDao
    private lateinit var db: AppDatabase

    @Before
    @Throws(IOException::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()
        pokeBallDao = db.pokeBallDao()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        // テスト同士が影響を及ぼさないようにレコードは各々のテストが終わったら削除する
        pokeBallDao.drop()
        db.close()
    }

    @Test
    fun testCreation() {
        val entity = PokemonResourceEntity(
            name = "foo",
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        )

        // データをデータベースに登録する
        pokeBallDao.insert(entity)

        // 登録したデータをデータベース経由で取り出せる
        val all = pokeBallDao.getAll()
        val id1 = all.firstOrNull()
        assertEquals("foo", id1?.name)
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", id1?.url ?: "")
    }

    @Test
    fun testGetAll() {
        val entity1 = PokemonResourceEntity(
            name = "foo",
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        )
        val entity2 = PokemonResourceEntity(
            name = "bar",
            url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png"
        )

        // データをデータベースに登録する
        pokeBallDao.insert(entity1)
        pokeBallDao.insert(entity2)

        // 登録したデータが全てデータベース経由で取り出せる
        val all = pokeBallDao.getAll()
        assertEquals(2, all.count())
    }

    @Test
    fun testReplace() {
        // データをデータベースに登録した後に削除する
        val entity1 = PokemonResourceEntity(
            name = "foo",
            url =  "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        )
        pokeBallDao.insert(entity1)
        pokeBallDao.delete(entity1)

        // 入れ替えるデータを挿入する
        val entity2 = PokemonResourceEntity(
            name = "foo",
            url =  "https://staging-raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
        )
        pokeBallDao.insert(entity2)

        // 登録したデータをデータベース経由で取り出した時入れ替えたデータになっている
        val all = pokeBallDao.getAll()
        val id1 = all.firstOrNull()
        assertEquals("foo", id1?.name)
        assertEquals("https://staging-raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", id1?.url ?: "")
    }
}