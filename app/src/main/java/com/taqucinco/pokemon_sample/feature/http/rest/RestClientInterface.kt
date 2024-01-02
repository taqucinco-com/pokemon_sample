package com.taqucinco.pokemon_sample.feature.http.rest

interface RestClientInterface {

    /**
     * okhttp3.Responseは不要なプロパティーが多くインターフェースとして冗長なためリポジトリで使うのに十分な程度に絞る
     */
    data class Response(
        val status: Int,
        val headers: Map<String, String>,
        val data: ByteArray,
    )

    object Header {
        const val contentType = "Content-Type"
        const val accept = "Accept"

        /*
         * 対応するContentType
         * Content-Typeに対してリクエストボディがどのようになるコメントする。
         */
        object ContentType {
            // {"foo": "bar, "hoge: 1, "lorem": true}
            const val json = "application/json; charset=utf-8"

            // foo%3Dbar%26hoge%3D1%26lorem%3Dtrue # %3D > =, %26 > &
            const val formUrlEncoded = "application/x-www-form-urlencoded; charset=utf-8"

            // --AiGolf
            // Content-Disposition: form-data; name="foo"
            //
            // bar
            // --AiGolf
            // Content-Disposition: form-data; name="hoge"
            //
            // 1
            // --AiGolf
            // ...
            const val multipartFormData = "multipart/form-data; boundary=PokemonSample"
            const val octetStream = "application/octet-stream"
        }
    }

    /**
     * GETメソッド
     *
     * @param endpoint （例. https://yahoo.co.jp）
     * @param queryParams （例. mapOf("foo" to "bar", "hoge" to 1) # ?foo=bar&hoge=1）
     * @param headers （例. mapOf(Pair("Authorization", "Bearer: {token}"))
     */
    suspend fun get(
        endpoint: String,
        queryParams: Map<String, Any> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
    ): Response

    /**
     * POSTメソッド
     *
     * @param T bodyの型
     * @param endpoint （例. https://yahoo.co.jp）
     * @param body JSONObject | String | ByteString | ByteArray | File
     * @param headers （例. listOf(Pair("Authorization", "Bearer: {token}"))
     *
     * NOTE: ヘッダーに含まれるContent-Typeから適切なリクエストボディーをエンコードする
     */
    suspend fun <T> post(
        endpoint: String,
        body: T,
        headers: Map<String, String> = emptyMap(),
    ): Response
}