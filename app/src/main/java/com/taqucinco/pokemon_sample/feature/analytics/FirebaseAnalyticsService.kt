package com.taqucinco.pokemon_sample.feature.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsService @Inject constructor(
    private val fa: FirebaseAnalytics
): FirebaseAnalyticsServable {
    override fun setUserID(userID: String?) {
        fa.setUserId(userID?.take(FirebaseAnalyticsServable.Constant.Limit.userId))
    }

    override fun setUserProperty(name: String, value: String?) {
        if (name in FirebaseAnalyticsServable.Constant.UserProperty.prohibited) { return }
        fa.setUserProperty(
            name.take(FirebaseAnalyticsServable.Constant.Limit.userPropsName),
            value?.take(FirebaseAnalyticsServable.Constant.Limit.userPropsValue)
        )
    }

    override fun logEvent(event: FirebaseAnalyticsServable.Event) {
        val bundle = Bundle()
        event.parameters?.toList()?.forEach {
            val key = it.first.take(FirebaseAnalyticsServable.Constant.Limit.eventParamName)
            when (val valueUnion = it.second) {
                is FirebaseAnalyticsServable.Event.Parameters.IntVal -> {
                    bundle.putInt(key, valueUnion.value)
                }
                is FirebaseAnalyticsServable.Event.Parameters.StringVal -> {
                    bundle.putString(key, (valueUnion.value).take(FirebaseAnalyticsServable.Constant.Limit.eventParamValue))
                }
                is FirebaseAnalyticsServable.Event.Parameters.DoubleVal -> {
                    bundle.putDouble(key, valueUnion.value)
                }
                is FirebaseAnalyticsServable.Event.Parameters.FloatVal -> {
                    bundle.putFloat(key, valueUnion.value)
                }
                else -> {}
            }
        }
        fa.logEvent(
            event.eventName.take(FirebaseAnalyticsServable.Constant.Limit.eventName),
            bundle
        )
    }
}