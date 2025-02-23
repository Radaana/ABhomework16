package ru.sigenna.bgcounter.api

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.sigenna.bgcounter.model.BgData
import java.util.concurrent.TimeUnit


interface TeseraApi {
    @Headers("Accept: */*")
    @GET("search_items_wide/1,2,9,11,12,163,166,255,280,308,403/0/")
    fun getBGSuggestions(@Query("q") query: String): Call<String>
}

@Module
@InstallIn(SingletonComponent::class)
object TeseraApiService {
    private const val BASE_URL = "https://tesera.ru/"

    private val TAG = TeseraApiService::class.simpleName

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    val api: TeseraApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(TeseraApi::class.java)

    @Provides
    fun getBgSuggestions(search: String, callback: (result: List<BgData>) -> Unit): Boolean {
        Log.d(TAG, "search $search")
        api.getBGSuggestions(search).enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                val plain_text_response = response.body()
                if (plain_text_response != null) {
                    val result = plain_text_response
                        .split("\n")
                        .filter { it.contains("игры\tgame") }
                        .map(transform = fun(s: String): BgData {
//                            Log.d(TAG, s)
                            val splitString = s.split(Regex("\t"), 4)
//                            Log.d(TAG, splitString.toString())
                            val bgData = BgData(
                                id = splitString[0].trim(),
                                teseraStringId = splitString[1].trim(),
                                title = splitString[2].trim()
                            )
//                            Log.d(TAG, bgData.toString())
                            return bgData
                        })
                    callback(result)
                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                callback(emptyList())
            }
        })
        return true
    }
}