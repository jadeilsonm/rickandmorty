package com.example.rickandmorty.data.datasource.api

import com.example.rickandmorty.data.model.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMorty {
    @GET("api/character")
    suspend fun getCharacter(@Query("page") page: Int) : Response<Character>
}