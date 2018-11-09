package no.danielzeller.friends.ui.friend

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import no.danielzeller.friendsservice.model.Friend
import no.danielzeller.friendsservice.repository.FriendsRepository
import no.danielzeller.friendsservice.repository.Resource
import javax.inject.Inject

class FriendsViewModel @Inject constructor(private val friendsRepository: FriendsRepository) : ViewModel() {

    fun getFriends(): LiveData<Resource<List<Friend>>> {
        return friendsRepository.getFriends()
    }

    fun saveFriend(friend: Friend): LiveData<Resource<Boolean>> {
        return friendsRepository.saveFriend(friend)
    }

    fun getFriendByID(id: Int): LiveData<Resource<Friend>> {
        return friendsRepository.getFriendByID(id)
    }

    fun getPreferredID(): LiveData<Resource<Int>> {
        return friendsRepository.getPreferredID()
    }

    fun dispose() {
        friendsRepository.dispose()
    }
}
