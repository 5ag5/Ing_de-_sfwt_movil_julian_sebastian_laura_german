package com.tsdc.vinilos.di

import android.content.Context
import com.tsdc.vinilos.data.local.AppDatabase
import com.tsdc.vinilos.data.remote.network.ServiceAdapter
import com.tsdc.vinilos.data.remote.network.VinilosApiService
import com.tsdc.vinilos.data.repositories.ArtistRepository as ArtistRepositoryImpl
import com.tsdc.vinilos.data.repositories.AlbumRepository as AlbumRepositoryImpl
import com.tsdc.vinilos.data.repositories.CollectorRepository as CollectorRepositoryImpl
import com.tsdc.vinilos.domain.repositories.AlbumRepository
import com.tsdc.vinilos.domain.repositories.ArtistRepository
import com.tsdc.vinilos.domain.repositories.CollectorRepository
import com.tsdc.vinilos.domain.usecases.AddTrackToAlbumUseCase
import com.tsdc.vinilos.domain.usecases.CreateAlbumUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumTracksUseCase
import com.tsdc.vinilos.domain.usecases.GetAlbumsUseCase
import com.tsdc.vinilos.domain.usecases.GetArtistByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetArtistsUseCase
import com.tsdc.vinilos.domain.usecases.GetCollectorByIdUseCase
import com.tsdc.vinilos.domain.usecases.GetCollectorsUseCase
import com.tsdc.vinilos.domain.usecases.IsFavoriteArtistUseCase
import com.tsdc.vinilos.domain.usecases.ToggleFavoriteArtistUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {

    private const val BASE_URL = "https://backvynils-production-5c50.up.railway.app/"

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context
    }

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

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(appContext)
    }

    val albumRepository: AlbumRepository by lazy {
        AlbumRepositoryImpl(serviceAdapter, database.albumDao())
    }

    val getAlbumsUseCase: GetAlbumsUseCase by lazy {
        GetAlbumsUseCase(albumRepository)
    }

    val getAlbumByIdUseCase: GetAlbumByIdUseCase by lazy {
        GetAlbumByIdUseCase(albumRepository)
    }

    val createAlbumUseCase: CreateAlbumUseCase by lazy {
        CreateAlbumUseCase(albumRepository)
    }

    val getAlbumTracksUseCase: GetAlbumTracksUseCase by lazy {
        GetAlbumTracksUseCase(albumRepository)
    }

    val addTrackToAlbumUseCase: AddTrackToAlbumUseCase by lazy {
        AddTrackToAlbumUseCase(albumRepository)
    }

    val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(serviceAdapter, database.favoriteArtistDao(), database.artistDao())
    }

    val getArtistsUseCase: GetArtistsUseCase by lazy {
        GetArtistsUseCase(artistRepository)
    }

    val getArtistByIdUseCase: GetArtistByIdUseCase by lazy {
        GetArtistByIdUseCase(artistRepository)
    }

    val toggleFavoriteArtistUseCase: ToggleFavoriteArtistUseCase by lazy {
        ToggleFavoriteArtistUseCase(artistRepository)
    }

    val isFavoriteArtistUseCase: IsFavoriteArtistUseCase by lazy {
        IsFavoriteArtistUseCase(artistRepository)
    }

    val collectorRepository: CollectorRepository by lazy {
        CollectorRepositoryImpl(serviceAdapter, database.collectorDao())
    }

    val getCollectorsUseCase: GetCollectorsUseCase by lazy {
        GetCollectorsUseCase(collectorRepository)
    }

    val getCollectorByIdUseCase: GetCollectorByIdUseCase by lazy {
        GetCollectorByIdUseCase(collectorRepository)
    }
}
