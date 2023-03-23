package com.coyotwilly.nomad.service

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserServiceImpl(
    private var client: HttpClient
) : UserService{
    override suspend fun getUser(): User {
        return try {
            client.get{
                url(HttpRoutes.GET_USER)
            }.body()
        } catch (e: RedirectResponseException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            User()
        } catch (e: ClientRequestException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            User()
        } catch (e: ServerResponseException) {
            Log.e("REQUEST_ERROR",e.response.status.description)
            User()
        } catch (e: Exception) {
            Log.e("REQUEST_ERROR", e.message.toString())
            User()
        }
    }

    override suspend fun createUser(user: User): User? {
        return try {
            client.post{
                url(HttpRoutes.NEW_USER)
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body<User>()
        } catch (e: RedirectResponseException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            null
        } catch (e: ClientRequestException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            null
        } catch (e: ServerResponseException) {
            Log.e("REQUEST_ERROR",e.response.status.description)
            null
        } catch (e: Exception) {
            Log.e("REQUEST_ERROR", e.message.toString())
            null
        }
    }
}