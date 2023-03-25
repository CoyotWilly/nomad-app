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
    override suspend fun getUser(id: Long): User {
        return try {
            client.get{
                url(HttpRoutes.getUser(id))
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

    override suspend fun canLogin(user: LoginCredentials): Boolean {
        return try {
            client.post {
                url(HttpRoutes.LOGIN_VERIFIER)
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body()
        } catch (e: RedirectResponseException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            false
        } catch (e: ClientRequestException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            false
        } catch (e: ServerResponseException) {
            Log.e("REQUEST_ERROR",e.response.status.description)
            false
        } catch (e: Exception) {
            Log.e("REQUEST_ERROR", e.message.toString())
            false
        }
    }

    override suspend fun idChecker(user: LoginCredentials): Long {
        return try {
            client.post {
                url(HttpRoutes.ID_MATCHER)
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body()
        } catch (e: RedirectResponseException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            0L
        } catch (e: ClientRequestException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            0L
        } catch (e: ServerResponseException) {
            Log.e("REQUEST_ERROR",e.response.status.description)
            0L
        } catch (e: Exception) {
            Log.e("REQUEST_ERROR", e.message.toString())
            0L
        }
    }

    override suspend fun userPasswordReset(user: LoginCredentials): User {
        return try {
            client.post {
                url(HttpRoutes.PASSWORD_RESET)
                contentType(ContentType.Application.Json)
                setBody(user)
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

    override suspend fun deleteUserAccount(id: Long): String {
        return try {
            client.delete {
                url(HttpRoutes.getUser(id))
            }.body()
        } catch (e: RedirectResponseException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            "DELETE USER ERROR"
        } catch (e: ClientRequestException){
            Log.e("REQUEST_ERROR",e.response.status.description)
            "DELETE USER ERROR"
        } catch (e: ServerResponseException) {
            Log.e("REQUEST_ERROR",e.response.status.description)
            "DELETE USER ERROR"
        } catch (e: Exception) {
            Log.e("REQUEST_ERROR", e.message.toString())
            "DELETE USER ERROR"
        }
    }
}