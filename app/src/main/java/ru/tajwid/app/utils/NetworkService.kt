package ru.tajwid.app.utils

import androidx.core.os.bundleOf
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object NetworkService {
    private fun getBaseUrl() = "https://api.telegram.org/${prefs.getBotId()}/"
    private const val DEFAULT_PARSE_MODE = "MarkdownV2"
    private val prefs by lazy { PreferencesHelper.get() }

    private val service by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(getBaseUrl())
            .client(
                OkHttpClient().newBuilder()
                    .callTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(Api::class.java)
    }

    suspend fun sendMessage(
        userName: String,
        phone: String,
        email: String,
        needIndividual: Boolean
    ): Response? {
        val userInfo = """
            *Имя:* $userName 
            *Почта:* ${email.replace(".", "\\.")} 
            *Телефон:* ${phone.replace("+", "\\+")}
            *Примечания:* ${if (needIndividual) "Нужны идивидуальные занятия" else "\\-"}
        """.trimIndent()
        val resp = service.writeUserInfo(userInfo, prefs.getChatId().orEmpty(), DEFAULT_PARSE_MODE)

        if (!resp.isSuccessful) {
            val error = Gson().fromJson(resp.errorBody()?.charStream(), Response::class.java)
            throw IllegalStateException(error.error.orEmpty())
        }

        Firebase.analytics.logEvent(
            "user_register_data", bundleOf(
                "info" to "$userName:$email:$phone"
            )
        )

        return resp.body()
    }

}

interface Api {
    @FormUrlEncoded
    @POST("sendMessage")
    suspend fun writeUserInfo(
        @Field("text") userInfo: String,
        @Field("chat_id") chatId: String,
        @Field("parse_mode") parseMode: String
    ): retrofit2.Response<Response>
}

data class Response(
    @SerializedName("ok") val result: Boolean,
    @SerializedName("description") val error: String?,
    @SerializedName("error_code") val code: Int?,
)