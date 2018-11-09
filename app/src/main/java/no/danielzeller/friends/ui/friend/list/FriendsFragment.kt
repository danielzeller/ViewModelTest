package no.danielzeller.friends.ui.friend.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.friends_fragment.view.*
import no.danielzeller.friends.R
import no.danielzeller.friendsservice.model.Friend
import no.danielzeller.friendsservice.repository.Status
import android.arch.lifecycle.Observer
import no.danielzeller.friends.ui.friend.FriendsViewModelFragment
import no.danielzeller.friends.ui.friend.details.FriendDetailsFragment
import no.danielzeller.friends.ui.friend.register.RegisterFriendFragment
import no.danielzeller.friends.util.ErrorHandler

class FriendsFragment : FriendsViewModelFragment() {

    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.friends_fragment, container, false)

        setupRecyclerView(rootView)
        setupRegisterButton()
        subscribeToFriends()

        return rootView
    }

    private fun setupRegisterButton() {
        rootView.addFriendFloatingActionButton.setOnClickListener {
            val detailsFragment = RegisterFriendFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, detailsFragment)
                .addToBackStack(null).commit()
        }
    }

    private fun setupRecyclerView(rootView: View) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        friendsAdapter = FriendsAdapter(friendClickUnit)

        rootView.friendsRecyclerView.layoutManager = linearLayoutManager
        rootView.friendsRecyclerView.adapter = friendsAdapter
    }

    private val friendClickUnit: ((friendID: Int) -> Unit) = { friendID: Int ->
        val detailsFragment = FriendDetailsFragment.newInstance(friendID)
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, detailsFragment)
            .addToBackStack(null).commit()
    }

    private fun subscribeToFriends() {
        viewModel.getFriends().observe(this, Observer { friendsListResponse ->
            when {
                friendsListResponse?.status == Status.LOADING -> showProgressBar()
                friendsListResponse?.status == Status.SUCCESS -> {
                    updateFriendsListAdapter(friendsListResponse.data!!)
                    hideProgressBar()
                }
                else -> {
                    ErrorHandler.handleError(rootView, friendsListResponse?.throwable)
                    hideProgressBar()
                }
            }
        })
    }

    private fun updateFriendsListAdapter(friends: List<Friend>) {
        friendsAdapter.friends = friends
        friendsAdapter.notifyDataSetChanged()
    }

    private fun hideProgressBar() {
        rootView.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        rootView.progressBar.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = FriendsFragment()
    }
}
