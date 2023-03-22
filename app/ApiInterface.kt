import retrofit2.http.GET

interface ApiInterface {
    fun getUser(): Call
}