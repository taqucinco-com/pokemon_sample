package com.taqucinco.pokemon_sample.feature.http

import com.taqucinco.pokemon_sample.error.HttpError
import com.taqucinco.pokemon_sample.feature.http.rest.RestClient
import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RestClientTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGet_madeCorrectRequest() = runTest {
        val clientMock = mock<OkHttpClient> { }
        val callMock = mock<Call> { }

        // mockからRestClientのcontinuationにはアクセスできないのですぐにthrowして処理を終わらせる
        // NOTE: このテストではあくまでRequestの検証を行うだけなので検査対象以外については関知しなくて良い > testGet_CorrectResponseの方で検証
        whenever(callMock.enqueue(anyOrNull())).doAnswer {
            throw Exception("immediately throw")
        }

        // OkHttpClientがnewCallメソッドを実行したらcallMockを返却するようにstubを作る
        whenever(clientMock.newCall(anyOrNull())).doReturn(callMock)

        val sut = RestClient(okHttpClient = clientMock)
        try {
            sut.get(
                endpoint = "https://yahoo.co.jp",
                queryParams = mapOf("foo" to "bar", "hoge" to 1), // # ?foo=bar&hoge=1
                headers = mapOf(RestClientInterface.Header.contentType to RestClientInterface.Header.ContentType.json)
            )
        } catch (t: Throwable) {
            // 想定しないエラーならばfail
            if (t.message != "immediately throw") { Assert.fail() }
        }

        // 対象の処理により想定した通りのリクエストが生成されているか
        verify(clientMock, times(1)).newCall(argThat {
            if (this.method.lowercase() != "get") return@argThat false
            if (this.headers.first() != Pair("Content-Type", "application/json; charset=utf-8")) { return@argThat false }
            val fullPath = this.url.toUrl().toString()
            return@argThat fullPath.matches(Regex("^https://yahoo\\.co\\.jp/\\?foo=bar&hoge=1"))
        })
    }

    @Test
    fun testGet_CorrectResponse() = runBlocking {
        // 想定のレスポンスを返すモックサーバーをたてる
        val interceptor = Interceptor { chain ->
            val body = "foobar"
            Response.Builder()
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .message(body)
                .body(
                    body
                        .toByteArray(Charsets.UTF_8)
                        .toResponseBody("text/plain".toMediaTypeOrNull())
                )
                .addHeader("Content-Type", "text/plain")
                .request(chain.request())
                .build()
        }

        // NOTE: 実際のOkHttpClientを使用するのでspyとして生成する
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        val clientSpy = spy(builder.build())

        // モックサーバーに対してリクエストする
        val sut = RestClient(okHttpClient = clientSpy)
        val response = sut.get(endpoint = "https://yahoo.co.jp")

        // RestClientInterface.Responseに変換できているか
        assertEquals(200, response.status)
        val header = response.headers.toList().first()
        assertEquals("Content-Type", header.first)
        assertEquals("text/plain", header.second)
        assertEquals("foobar", String(response.data, Charsets.UTF_8))
    }

    @Test
    fun testGet_HandleThrowingHttpError_When4xxStatus() = runBlocking {
        // OAuthによる認証失敗を想定したレスポンスを返すモックサーバーをたてる
        // NOTE: OAuthによる認証失敗を想定する > https://openid-foundation-japan.github.io/rfc6750.ja.html#anchor1
        val interceptor = Interceptor { chain ->
            val body = "401 Unauthorized"
            Response.Builder()
                .code(401)
                .protocol(Protocol.HTTP_1_1)
                .message(body)
                .body(
                    body
                        .toByteArray(Charsets.UTF_8)
                        .toResponseBody("text/plain;charset=utf-8".toMediaTypeOrNull())
                )
                .addHeader("WWW-Authenticate", "Bearer realm=example,error=invalid_token,error_description=The access token expired")
                .request(chain.request())
                .build()
        }
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
        val clientSpy = spy(builder.build())

        // モックサーバーに対してリクエストする
        val sut = RestClient(okHttpClient = clientSpy)

        val jwt = "header.payload.signature"
        try {
            sut.get(
                endpoint = "https://yahoo.co.jp",
                headers = mapOf("Authorization" to "Bearer $jwt")
            )
        } catch (httpError: HttpError.Status) {
            // 認証失敗に対してHttpError.Statusをスローするか
            assertEquals(401, httpError.code)
            assertEquals("401 Unauthorized", String(httpError.body, Charsets.UTF_8))
            val value = httpError.headers["WWW-Authenticate"]
            assertNotNull(value)
            value?.let { assertEquals("Bearer realm=example,error=invalid_token,error_description=The access token expired", it) }
            return@runBlocking
        } catch (t: Throwable) {
            fail("should throw HttpError.Status")
        }
        fail("should throw HttpError.Status")
    }
}