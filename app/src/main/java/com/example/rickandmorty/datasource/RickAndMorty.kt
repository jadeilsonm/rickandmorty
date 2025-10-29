package com.example.rickandmorty.datasource

import com.example.rickandmorty.models.Character
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface RickAndMorty {
    @GET("api/character")
    suspend fun getCharacter(@Query("page") page: Int) : Response<Character>
}