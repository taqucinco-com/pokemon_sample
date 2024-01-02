package com.taqucinco.pokemon_sample.feature.analytics

sealed class FirebaseAnalyticsEvent {
    data class PokeBall(
        val pokemonName: String,
        val captured: Boolean,
    ) : FirebaseAnalyticsServable.Event.Generatable {
        override val generated: FirebaseAnalyticsServable.Event
            get() {
                return object : FirebaseAnalyticsServable.Event {
                    override val eventName: String = "PokeBall"
                    override val parameters: Map<String, FirebaseAnalyticsServable.Event.Parameters?> = mapOf(
                        "name" to FirebaseAnalyticsServable.Event.Parameters.StringVal(pokemonName),
                        "capturing" to FirebaseAnalyticsServable.Event.Parameters.IntVal(if (captured) 1 else 0),
                    )
                }
            }
    }
}
