package no.danielzeller.friends.di

import dagger.Component
import no.danielzeller.friends.FriendsApplication

import javax.inject.Singleton

@Singleton
@Component(modules = [FriendsDIMainModule::class, ViewModelModule::class])
interface FriendsDIComponent : FriendsDIGraph {
    object Initializer {
        fun init(app: FriendsApplication): FriendsDIComponent {
            return DaggerFriendsDIComponent.builder()
                .friendsDIMainModule(FriendsDIMainModule(app))
                .build()
        }
    }
}