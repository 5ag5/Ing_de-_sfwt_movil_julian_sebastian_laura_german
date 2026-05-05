package com.tsdc.vinilos.di

import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.data.repositories.ArtistRepository as ArtistRepositoryImpl
import com.tsdc.vinilos.data.repositories.AlbumRepository as AlbumRepositoryImpl
import com.tsdc.vinilos.domain.repositories.AlbumRepository
import com.tsdc.vinilos.domain.repositories.ArtistRepository
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import com.tsdc.vinilos.domain.usecases.GetArtistsUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val BASE_URL = "https://backvynils-production-5c50.up.railway.app/"
    
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
        AlbumRepositoryImpl(serviceAdapter)
    }
    
    val getAlbumsUseCase: GetAlbumsUseCase by lazy {
        GetAlbumsUseCase(albumRepository)
    }

    val getAlbumByIdUseCase: GetAlbumByIdUseCase by lazy {
        GetAlbumByIdUseCase(albumRepository)
    }

    val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(serviceAdapter)
    }

    val getArtistsUseCase: GetArtistsUseCase by lazy {
        GetArtistsUseCase(artistRepository)
    }
}
