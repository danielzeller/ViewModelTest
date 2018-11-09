package no.danielzeller.friendsservice.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import no.danielzeller.friendsservice.database.FriendsDataBase
import no.danielzeller.friendsservice.model.Friend
import no.danielzeller.friendsservice.service.FriendsService
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.inject.Inject


class FriendsRepository @Inject constructor(
    private val friendsService: FriendsService,
    private val friendsDataBase: FriendsDataBase
) : IFriendsRepository {

    private val disposables = CompositeDisposable()
    private val friendsLiveData = MutableLiveData<Resource<List<Friend>>>()

    override fun getFriends(): LiveData<Resource<List<Friend>>> {
        friendsLiveData.value = Resource.loading()

        val remoteObservable = createFriendsRemoteRequest()
        val localObservable = createFriendsDBRequest()

        disposables.add(
            Observable.mergeDelayError(remoteObservable, localObservable)
                .observeOn(AndroidSchedulers.mainThread(), true).subscribe(
                    { friends -> friendsLiveData.value = Resource.success(friends) },
                    { throwable -> friendsLiveData.value = Resource.error(throwable, friendsLiveData.value?.data) })
        )

        return friendsLiveData
    }

    override fun saveFriend(friend: Friend): LiveData<Resource<Boolean>> {
        val saveResponse = MutableLiveData<Resource<Boolean>>()
        saveResponse.value = Resource.loading()

        disposables.add(
            Observable.fromCallable<MutableLiveData<Resource<Boolean>>> {
                friendsDataBase.friendDao().saveSingleFriend(friend)
                return@fromCallable saveResponse
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { saveResponse.value = Resource.success(true) },
                    { throwable -> saveResponse.value = Resource.error(throwable, false) })
        )
        return saveResponse
    }

    override fun getFriendByID(id: Int): LiveData<Resource<Friend>> {
        val friendResponse = MutableLiveData<Resource<Friend>>()

        disposables.add(Observable.fromCallable<Friend> { friendsDataBase.friendDao().loadFriendById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ friend ->
                friendResponse.value = Resource.success(friend)
            }, { throwable -> friendResponse.value = Resource.error(throwable, null) }))
        return friendResponse
    }

    override fun getPreferredID(): LiveData<Resource<Int>> {
        val idResponse = MutableLiveData<Resource<Int>>()

        disposables.add(Observable.fromCallable<Friend> { friendsDataBase.friendDao().loadFriendWithHighestID() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ friend ->
                idResponse.value = Resource.success(friend.id + 1)
            }, { throwable -> idResponse.value = Resource.error(throwable, null) }))
        return idResponse
    }

    override fun dispose() {
        disposables.dispose()
    }

    private fun createFriendsDBRequest(): Observable<List<Friend>>? {
        return Observable.fromCallable<List<Friend>> { friendsDataBase.friendDao().loadAllFriendsFromDB() }
            .subscribeOn(
                Schedulers.io()
            )
    }

    private fun createFriendsRemoteRequest(): Observable<MutableList<Friend>>? {
        return friendsService.listFriends
            .map { remoteFriends ->

                val localFriends = friendsDataBase.friendDao().loadAllFriendsFromDB()
                val localAndRemoteFriends = Stream.concat(localFriends.stream(), remoteFriends.stream())
                    .distinct()
                    .sorted(Comparator.comparing(Friend::id).reversed())
                    .collect(Collectors.toList())

                friendsDataBase.friendDao().saveAllFriends(localAndRemoteFriends)
                return@map localAndRemoteFriends
            }
            .subscribeOn(Schedulers.io())
    }

    companion object {
        const val FRIENDS_BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}