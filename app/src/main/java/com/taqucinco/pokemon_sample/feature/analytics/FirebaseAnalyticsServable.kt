package com.taqucinco.pokemon_sample.feature.analytics

interface FirebaseAnalyticsServable {

//    https://firebase.google.com/docs/analytics/events?hl=ja&platform=android
    interface Event {
        val eventName: String
        val parameters: Map<String, Parameters?>

        // パラメータがとりうる値 > Int | String | Double | Float
        sealed class Parameters {
            data class IntVal(val value: Int) : Parameters()
            data class StringVal(val value: String) : Parameters()
            data class DoubleVal(val value: Double) : Parameters()
            data class FloatVal(val value: Float) : Parameters()
        }

        interface Generatable {
            val generated: Event
        }
    }

//    https://support.google.com/analytics/answer/9267744?hl=en
    sealed class Constant {
        object Limit: Constant() {
            const val eventName = 40
            const val eventParamName = 40
            const val userPropsName = 24
            const val eventParamValue = 100
            const val userPropsValue = 36
            const val userId = 256
        }

        sealed class UserProperty: Constant() {
            companion object {
                val prohibited = listOf("Age", "Gender", "Interest")
            }
        }
    }

    /**
     * ユーザーIDを設定
     * https://firebase.google.com/docs/analytics/userid?hl=ja
     *
     * @param userID　ユーザーID
     */
    fun setUserID(userID: String?)

    /**
     * ユーザープロパティを設定
     * https://firebase.google.com/docs/analytics/user-properties?hl=ja&platform=android
     *
     * @param name プロパティ名 > プロパティ名はAge/Gender/Interestは予約語になっているため使用できない
     * @param value プロパティ値 nullを代入すると該当ユーザーのプロパティーを消去する
     */
    fun setUserProperty(name: String, value: String?)

    /**
     * ログイベント
     * https://firebase.google.com/docs/analytics/events?hl=ja&platform=android
     *
     * @param event イベントとそれに付随するパラメータ
     */
    fun logEvent(event: Event)
}