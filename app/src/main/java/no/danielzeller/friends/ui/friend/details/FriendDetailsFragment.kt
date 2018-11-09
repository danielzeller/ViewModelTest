package no.danielzeller.friends.ui.friend.details

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.friend_details_layout.view.*
import kotlinx.android.synthetic.main.friend_details_item.view.*

import no.danielzeller.friends.R
import no.danielzeller.friends.ui.friend.FriendsViewModelFragment
import no.danielzeller.friendsservice.model.Friend
import no.danielzeller.friendsservice.repository.Status

private const val FRIEND_ID_KEY = "friend_id_key"

class FriendDetailsFragment : FriendsViewModelFragment() {

    private var friendID = -1
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            friendID = it.getInt(FRIEND_ID_KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.friend_details_layout, container, false)
        getSelectedFriend()
        return rootView
    }

    private fun getSelectedFriend() {
        viewModel.getFriendByID(friendID).observe(this, Observer { friendResponse ->
            if (friendResponse != null) {
                if (friendResponse.status == Status.ERROR) {
                    Snackbar.make(rootView, R.string.error_generic, Toast.LENGTH_LONG).show()
                } else {
                    setupUI(friendResponse.data)
                }
            }
        })
    }

    private fun setupUI(friend: Friend?) {
        val scrollViewContainer = rootView.scrollContainer
        scrollViewContainer.removeAllViews()
        addDetailItem(scrollViewContainer, friend?.name, R.string.name)
        addDetailItem(scrollViewContainer, friend?.userName, R.string.user_name)
        addDetailItem(scrollViewContainer, friend?.email, R.string.email)
        addDetailItem(scrollViewContainer, friend?.phone, R.string.phone_number)
        addDetailItem(scrollViewContainer, friend?.website, R.string.website)
    }

    private fun addDetailItem(scrollViewContainer: LinearLayout, friendDetail: String?, headingResourceID: Int) {
        val itemView = LayoutInflater.from(requireContext())
            .inflate(R.layout.friend_details_item, scrollViewContainer, false)

        itemView.heading.text = resources.getString(headingResourceID)
        itemView.detail.text = friendDetail
        scrollViewContainer.addView(itemView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }

    companion object {

        @JvmStatic
        fun newInstance(friendID: Int) =
            FriendDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRIEND_ID_KEY, friendID)
                }
            }
    }
}
