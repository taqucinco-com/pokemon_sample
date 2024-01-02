package com.taqucinco.pokemon_sample.feature.database

import android.content.SharedPreferences

interface SharedPref {
    interface Factory {
        fun build(): SharedPreferences
    }

    companion object {
        const val prefName = "MyPref"
    }

    object Keys {
        const val userId: String = "user_id"
    }
}