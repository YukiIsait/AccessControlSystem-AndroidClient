package tech.youko.smartcard.network

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.youko.smartcard.R

object NetworkClient {
    private val converterFactory = buildGsonConverterFactory()
    private val okHttpClient = buildOkHttpClient()

    private lateinit var retrofitClient: Retrofit

    lateinit var api: NetworkApi
    var server: String = ""
        set(value) {
            field = value
            retrofitClient = buildRetrofitClient()
            api = createNetworkApi()
        }

    private fun buildRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://${server}")
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
//            .connectTimeout(500, TimeUnit.MILLISECONDS)
//            .readTimeout(500, TimeUnit.MILLISECONDS)
//            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .build()
    }

    private fun buildGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )
    }

    private fun createNetworkApi(): NetworkApi {
        return retrofitClient.create(NetworkApi::class.java)
    }

    fun initialize(context: Context) {
        this.server = context.getString(R.string.address_default_server_ip) +
                ":" +
                context.getString(R.string.address_default_server_port)
    }
}
