package com.taqucinco.pokemon_sample.feature.user

import android.content.Context
import android.content.SharedPreferences
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.database.SharedPref
import com.taqucinco.pokemon_sample.feature.env.UUIDGeneratable
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.UUID

// NOTE: ユニットテストにてテスト用のContextを使うためRobolectricで実行する
@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {

    private val context = RuntimeEnvironment.getApplication()
    private val sharedPrefFactory = object: SharedPref.Factory {
        override fun build(): SharedPreferences {
            return context.getSharedPreferences(SharedPref.prefName, Context.MODE_PRIVATE)
        }
    }
    val uuidString = "00000000-0000-0000-0000-000000000000"
    private val uuidGenMock = object: UUIDGeneratable {
        override fun generate(): UUID = UUID.fromString(uuidString)
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
        val sharedPref = sharedPrefFactory.build()
        sharedPref.edit().remove(SharedPref.Keys.userId)
    }

    @Test
    fun testRegister_NewRegistrationDone_WhenNoUserRegistered() = runBlocking {
        val fbMock = mock<FirebaseAnalyticsServable> { }

        val repo = UserRepositoryImpl(
            uuidGeneratable = uuidGenMock,
            sharedPrefFactory = sharedPrefFactory,
            firebaseAnalyticsService = fbMock
        )

        // 新規登録が成功したか
        when (val status = repo.register()) {
            is UserRepository.RegistrationStatus.NewRegistration -> {
                assertEquals(uuidString, status.user.id.toString())
            }
            else -> {
                fail("should be RegistrationStatus.NewRegistration")
            }
        }
        // FBがsetUserIDを1回実行したか
        verify(fbMock, Mockito.times(1)).setUserID(argThat {
            return@argThat this == uuidString
        })
        // SharedPrefにkey=SharedPref.Keys.userId value=uuidStringで永続化されたか
        val sharedPref = sharedPrefFactory.build()
        assertEquals(uuidString, sharedPref.getString(SharedPref.Keys.userId, ""))
    }

    @Test
    fun testRegister_GetAlreadyRegisteredUser_WhenAlreadyRegistered() = runBlocking {
        val repo = UserRepositoryImpl(
            uuidGeneratable = uuidGenMock,
            sharedPrefFactory = sharedPrefFactory,
            firebaseAnalyticsService = mock<FirebaseAnalyticsServable> { }
        )

        // 新規登録があらかじめしておく
        repo.register()

        // 登録済みの状態からさらに登録しようとする
        val duplicatedRegistration = repo.register()

        when (duplicatedRegistration) {
            is UserRepository.RegistrationStatus.AlreadyRegistered -> {
                // すでに登録済みのユーザーのIDが得られるか
                assertEquals(uuidString, duplicatedRegistration.user.id.toString())
            }
            else -> {
                fail("should be RegistrationStatus.NewRegistration")
            }
        }
    }
}