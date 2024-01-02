package com.taqucinco.pokemon_sample.extension

/*
 * Mapをクエリパラメータに変換する
 * （例. mapOf("foo" to "bar") # ?foo=bar
 */
fun Map<String, Any>.buildQueryParameters(): String {
    val queryString = StringBuilder()
    for ((key, value) in this) {
        if (queryString.isNotEmpty()) {
            queryString.append("&")
        }
        queryString.append(key).append("=").append(value.toString())
    }
    val built = queryString.toString()
    return if (built != "") "?${built}" else ""
}
