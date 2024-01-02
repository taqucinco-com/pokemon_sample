package com.taqucinco.pokemon_sample.feature.user

import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

// NOTE: ユニットテストにてテスト用のContextを使うためRobolectricで実行する
@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {

    private val context = RuntimeEnvironment.getApplication()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}