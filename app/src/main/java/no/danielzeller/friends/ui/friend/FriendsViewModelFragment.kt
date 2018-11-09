package no.danielzeller.friends.ui.friend

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import no.danielzeller.friends.FriendsApplication
import javax.inject.Inject

abstract class FriendsViewModelFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    internal lateinit var viewModel: FriendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FriendsApplication.component().inject(this)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[FriendsViewModel::class.java]
    }
}