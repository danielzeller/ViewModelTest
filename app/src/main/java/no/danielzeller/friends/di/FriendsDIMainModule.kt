package no.danielzeller.friends.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import no.danielzeller.friends.FriendsApplication
import no.danielzeller.friendsservice.database.FriendsDataBase
import no.danielzeller.friendsservice.repository.FriendsRepository
import no.danielzeller.friendsservice.service.FriendsService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import javax.inject.Singleton

private const val ROOM_DB_NAME = "FriendsDataBaseName"

@Module
class FriendsDIMainModule(private val app: FriendsApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    fun provideContext(): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideFriendsService(): FriendsService {
        val retrofit = Retrofit.Builder()
            .baseUrl(FriendsRepository.FRIENDS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(FriendsService::class.java)
    }

    @Provides
    @Singleton
    fun provideFriendsDataBase(): FriendsDataBase {
        return Room.databaseBuilder(app.applicationContext, FriendsDataBase::class.java, ROOM_DB_NAME)
            .build()
    }
}