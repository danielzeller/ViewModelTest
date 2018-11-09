package no.danielzeller.friends.di

import no.danielzeller.friends.MainActivity
import no.danielzeller.friends.ui.friend.FriendsViewModelFragment

interface FriendsDIGraph {
    fun inject(mainActivity: MainActivity)
    fun inject(friendDetailsFragment: FriendsViewModelFragment)
}
