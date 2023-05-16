import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class ExchangeRate(
    val ccy: String,
    val base_ccy: String,
    val buy: String,
    val sale: String
)

interface ApiService {
    @GET("pubinfo?exchange&coursid=11")
    suspend fun getExchangeRates(): List<ExchangeRate>


    object ApiServiceBuilder {
        private const val BASE_URL = "https://api.privatbank.ua/p24api/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }


}



