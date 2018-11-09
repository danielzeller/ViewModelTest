package no.danielzeller.friendsservice.repository

import android.arch.lifecycle.LiveData
import no.danielzeller.friendsservice.model.Friend

interface IFriendsRepository {

    fun getFriends(): LiveData<Resource<List<Friend>>>

    fun saveFriend(friend: Friend): LiveData<Resource<Boolean>>

    fun getFriendByID(id: Int): LiveData<Resource<Friend>>

    fun getPreferredID(): LiveData<Resource<Int>>

    fun dispose()

}