package com.taqucinco.pokemon_sample.feature.user

interface UserRepository {
    sealed class RegistrationStatus {
        data class NewRegistration(val user: User): RegistrationStatus()
        data class AlreadyRegistered(val user: User): RegistrationStatus()
    }

    /**
     * ユーザー登録を行う
     *
     * 1. SharedPreferencesに該当のキーに対応する値が空文字ならUUIDを生成する
     * 2. UUIDをSharedPreferencesに上書きする
     * 3. 新規登録するときはFirebase AnalyticsのユーザーIDをセットする
     * 4. 新規登録ならNewRegistration, すでに登録済ならAlreadyRegisteredを返却
     * 5. すでに登録済なら何もせずに終わる
     * 6. Homeタブを表示するときに行われることを想定
     */
    suspend fun register(): RegistrationStatus

    suspend fun get(): User?
}