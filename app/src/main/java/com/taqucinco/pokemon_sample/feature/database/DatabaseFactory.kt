package com.taqucinco.pokemon_sample.feature.database

interface DatabaseFactory {
    /**
     * データベースを生成する
     *
     * @return データベース
     */
    fun build(): AppDatabase
}
