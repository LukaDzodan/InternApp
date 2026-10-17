package com.example.internapp.di

import android.content.Context
import androidx.room.Room
import com.example.internapp.data.local.AppDatabase
import com.example.internapp.data.local.Dao
import com.example.internapp.data.remote.Api
import com.example.internapp.data.remote.mappers.GeoMapper
import com.example.internapp.data.remote.mappers.GeoMapperImpl
import com.example.internapp.data.remote.mappers.ListerMapper
import com.example.internapp.data.remote.mappers.ListerMapperImpl
import com.example.internapp.data.remote.mappers.PriceMapper
import com.example.internapp.data.remote.mappers.PriceMapperImpl
import com.example.internapp.data.remote.mappers.PropertyMapper
import com.example.internapp.data.remote.mappers.PropertyMapperImpl
import com.example.internapp.data.remote.mappers.SearchResultMapper
import com.example.internapp.data.remote.mappers.SearchResultMapperImpl
import com.example.internapp.data.repository.PropertyRepositoryImpl
import com.example.internapp.domain.repository.PropertyRepository
import com.example.internapp.utils.Constants
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Repository
    @Provides
    @Singleton
    fun providePropertyRepository(
        api : Api,
        searchResultMapper: SearchResultMapper,
        dao: Dao
    ): PropertyRepository {
        return PropertyRepositoryImpl(api,searchResultMapper,dao)
    }


    //Mappers
    @Provides
    @Singleton
    fun provideSearchResultMapper(
        listerMapper: ListerMapper,
        propertyMapper: PropertyMapper
    ): SearchResultMapper {
        return SearchResultMapperImpl(
            listerMapper,
            propertyMapper
        )
    }

    @Provides
    @Singleton
    fun providePropertyMapper(
        geoMapper: GeoMapper,
        priceMapper: PriceMapper,
        listerMapper: ListerMapper
    ): PropertyMapper {
        return PropertyMapperImpl(
            geoMapper,
            priceMapper,
            listerMapper
        )
    }

    @Provides
    @Singleton
    fun providePriceMapper(): PriceMapper {
        return PriceMapperImpl()
    }

    @Provides
    @Singleton
    fun provideListerMapper(): ListerMapper {
        return ListerMapperImpl()
    }

    @Provides
    @Singleton
    fun provideGeoMapper(): GeoMapper {
        return GeoMapperImpl()
    }

    //Api
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    //Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    //Moshi
    @Provides
    @Singleton
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    //OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    //Room
    @Provides
    @Singleton
    fun provideAppDataBase(
        @ApplicationContext context: Context
    ) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "intern_app.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(
        database: AppDatabase
    ) : Dao {
        return database.favoriteDao()
    }
}
