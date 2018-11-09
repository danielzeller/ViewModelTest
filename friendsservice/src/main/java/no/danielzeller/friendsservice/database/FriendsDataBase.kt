package no.danielzeller.friendsservice.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import no.danielzeller.friendsservice.model.Friend

@Database(entities = [Friend::class], version = 1, exportSchema = false)
abstract class FriendsDataBase : RoomDatabase() {
    abstract fun friendDao(): FriendsDao
}
