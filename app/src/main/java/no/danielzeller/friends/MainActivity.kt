package no.danielzeller.friends

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import no.danielzeller.friends.ui.friend.list.FriendsFragment
import no.danielzeller.friends.ui.friend.FriendsViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FriendsFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FriendsApplication.component().inject(this)
        val viewModel = ViewModelProviders.of(this, viewModelFactory)[FriendsViewModel::class.java]
        viewModel.dispose()
    }
}
