package com.tsdc.vinilos.di

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.data.repositories.AlbumRepository as AlbumRepositoryImpl
import com.tsdc.vinilos.domain.repositories.AlbumRepository
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    private const val BASE_URL = "http://10.0.2.2:3000/"
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiService: VinilosApiService by lazy {
        retrofit.create(VinilosApiService::class.java)
    }
    
    val serviceAdapter: ServiceAdapter by lazy {
        ServiceAdapter(apiService)
    }
    
    val albumRepository: AlbumRepository by lazy {
        AlbumRepositoryImpl(serviceAdapter) as AlbumRepository
    }
    
    val getAlbumsUseCase: GetAlbumsUseCase by lazy {
        GetAlbumsUseCase(albumRepository)
    }
}
