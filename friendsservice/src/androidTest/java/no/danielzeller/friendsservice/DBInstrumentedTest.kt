package no.danielzeller.friendsservice

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import no.danielzeller.friendsservice.database.FriendsDataBase
import no.danielzeller.friendsservice.model.Friend

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class DBInstrumentedTest {

    @Test
    fun testSaveFriend() {
        val db = createDB()

        val id = 1
        db.friendDao().saveSingleFriend(
            Friend(
                id,
                "Simon Howden",
                "SimonKingsman",
                "daniel@test.no",
                "91305136",
                "danielzeller.no"
            )
        )
        val friend = db.friendDao().loadFriendById(id)
        assertNotNull(friend)
    }


    @Test
    fun testGetHighestID() {
        val db = createDB()


        db.friendDao().saveSingleFriend(
            Friend(
                1,
                "Simon Howden",
                "SimonKingsman",
                "daniel@test.no",
                "91305136",
                "danielzeller.no"
            )
        )
        db.friendDao().saveSingleFriend(
            Friend(
                150,
                "Simon Howden",
                "SimonKingsman",
                "daniel@test.no",
                "91305136",
                "danielzeller.no"
            )
        )
        db.friendDao().saveSingleFriend(
            Friend(
                10,
                "Simon Howden",
                "SimonKingsman",
                "daniel@test.no",
                "91305136",
                "danielzeller.no"
            )
        )
        val highestID = db.friendDao().loadFriendWithHighestID()
        assertEquals(highestID.id, 150)
    }

    @Test
    fun testGetFields() {
        val db = createDB()


        val id = 1
        db.friendDao().saveSingleFriend(
            Friend(
                id,
                "Simon Howden",
                "SimonKingsman",
                "daniel@test.no",
                "91305136",
                "danielzeller.no"
            )
        )
        val friend = db.friendDao().loadFriendById(id)
        assertEquals(friend.id, 1)
        assertEquals(friend.name, "Simon Howden")
        assertEquals(friend.userName, "SimonKingsman")
        assertEquals(friend.email, "daniel@test.no")
        assertEquals(friend.phone, "91305136")
        assertEquals(friend.website, "danielzeller.no")
    }

    private fun createDB(): FriendsDataBase {
        val appContext = InstrumentationRegistry.getTargetContext()
        return Room.databaseBuilder(appContext.applicationContext, FriendsDataBase::class.java, "FriendsDataBaseName")
            .build()
    }
}
