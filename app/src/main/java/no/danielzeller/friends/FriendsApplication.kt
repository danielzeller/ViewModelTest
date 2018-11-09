package no.danielzeller.friends

import android.app.Application
import no.danielzeller.friends.di.FriendsDIComponent
import no.danielzeller.friends.di.FriendsDIGraph

class FriendsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildComponentAndInject()
    }

    companion object {
        private lateinit var graph: FriendsDIGraph
        private lateinit var instance: FriendsApplication

        fun component(): FriendsDIGraph {
            return graph
        }

        fun buildComponentAndInject() {
            graph = FriendsDIComponent.Initializer.init(instance)
        }
    }
}
