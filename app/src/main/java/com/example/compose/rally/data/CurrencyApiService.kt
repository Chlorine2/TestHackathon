import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

data class ExchangeRate(
    val ccy: String,
    val base_ccy: String,
    val buy: String,
    val sale: String
)
data class RegistryModel(
    val username: String,
    val password: String,
)
data class Token(
    val token : String
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


interface ApiCustomService {
    @POST("auth/register")
    suspend fun Register(@Body data: RegistryModel): Token
    @POST("auth/authenticate")
    suspend fun Authenticate(@Body data: RegistryModel): Token
    object ApiServiceBuilder2 {
        private const val BASE_URL = "https://azn-server.azurewebsites.net/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiService: ApiCustomService by lazy {
            retrofit.create(ApiCustomService::class.java)
        }
    }


}
