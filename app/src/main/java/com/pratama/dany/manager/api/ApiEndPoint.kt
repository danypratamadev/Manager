package com.pratama.dany.manager.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiEndPoint {

    companion object {

        private val BASE_URL = "https://testing.kuahpanas.co.id/"
        val KEY_ACCESS = "sfYZONlALBQsBMU6dGzlJVWG17M6qx27"
        val BUILDER_RETROFIT = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val REGISTER_API = BUILDER_RETROFIT.create(RegisterApi::class.java)

    }

}