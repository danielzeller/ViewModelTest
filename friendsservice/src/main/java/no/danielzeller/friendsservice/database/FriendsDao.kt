package no.danielzeller.friendsservice.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import no.danielzeller.friendsservice.model.Friend

import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.RoomWarnings

@Dao
interface FriendsDao {

    @Insert(onConflict = REPLACE)
    fun saveAllFriends(friends: List<Friend>)

    @Insert(onConflict = REPLACE)
    fun saveSingleFriend(friend: Friend)

    @Query("SELECT * FROM Friend ORDER BY id DESC")
    fun loadAllFriendsFromDB(): List<Friend>

    @Query("SELECT * FROM Friend WHERE id = :id")
    fun loadFriendById(id: Int): Friend

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT *, MAX(id) FROM Friend")
    fun loadFriendWithHighestID(): Friend

}
