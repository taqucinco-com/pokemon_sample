package com.taqucinco.pokemon_sample.feature.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.pokeBallTable

//@Entity(tableName = "pokemons")
//data class PokemonEntity(
//    @PrimaryKey override val id: Int,
//    @ColumnInfo(name = "name") override val name: String,
//    @ColumnInfo(name = "sprites") override val sprites: Sprites,
//): Pokemon {
//    data class Sprites(
//        override val frontDefault: String
//    ): Pokemon.PokemonSprites
//
//    // Spritesをprimitiveにしてデータベースで扱えるようにするコンバーターを定義
//    class Converters {
//        @TypeConverter
//        fun fromSprites(sprites: Sprites): String {
//            return sprites.frontDefault
//        }
//
//        @TypeConverter
//        fun toSprites(spritesString: String): Sprites {
//            return PokemonEntity.Sprites(spritesString)
//        }
//    }
//}

@Entity(tableName = "$pokeBallTable")
data class PokemonResourceEntity(
    @PrimaryKey override val name: String,
    @ColumnInfo(name = "url") override val url: String,
): PokemonResource
