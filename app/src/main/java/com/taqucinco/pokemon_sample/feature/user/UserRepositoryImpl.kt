package com.taqucinco.pokemon_sample.feature.user

import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.database.SharedPref
import com.taqucinco.pokemon_sample.feature.env.UUIDGeneratable
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val uuidGeneratable: UUIDGeneratable,
    private val sharedPrefFactory: SharedPref.Factory,
    private val firebaseAnalyticsService: FirebaseAnalyticsServable
): UserRepository {
    override suspend fun register(): UserRepository.RegistrationStatus {
        val shared = sharedPrefFactory.build()
        val userId = shared.getString(SharedPref.Keys.userId, "")
        return if (userId == "") {
            val editor = shared.edit()
            val uuid = uuidGeneratable.generate()
            val user = object : User {
                override val id: UUID = uuid
            }
            editor.putString(SharedPref.Keys.userId, uuid.toString())
            editor.apply()
            firebaseAnalyticsService.setUserID(uuid.toString())
            UserRepository.RegistrationStatus.NewRegistration(user)
        } else {
            val user = object : User {
                override val id: UUID = UUID.fromString(userId)
            }
            UserRepository.RegistrationStatus.AlreadyRegistered(user)
        }
    }

    override suspend fun get(): User? {
        val shared = sharedPrefFactory.build()
        val userId = shared.getString(SharedPref.Keys.userId, "")
        return if (userId == "") {
            null
        } else {
            object : User {
                override val id: UUID = UUID.fromString(userId)
            }
        }
    }
}