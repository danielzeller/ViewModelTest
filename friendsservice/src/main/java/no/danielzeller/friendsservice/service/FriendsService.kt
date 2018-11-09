package no.danielzeller.friendsservice.service

import io.reactivex.Observable
import no.danielzeller.friendsservice.model.Friend
import retrofit2.http.GET

interface FriendsService {
    @get:GET("users")
    val listFriends: Observable<List<Friend>>
}