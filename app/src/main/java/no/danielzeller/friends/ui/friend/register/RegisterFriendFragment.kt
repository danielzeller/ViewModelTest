package no.danielzeller.friends.ui.friend.register

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.friend_register_layout.view.*
import no.danielzeller.friends.R
import no.danielzeller.friends.ui.friend.FriendsViewModelFragment
import no.danielzeller.friends.ui.friend.FriendsViewModel
import no.danielzeller.friends.util.FieldValidator
import no.danielzeller.friendsservice.model.Friend
import no.danielzeller.friendsservice.repository.Status

private const val ERROR_MESSAGE_DURATION = 3000

class RegisterFriendFragment : FriendsViewModelFragment() {

    private lateinit var rootView: View
    private var saveFriendID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPreferredUserID()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.friend_register_layout, container, false)
        rootView.registerButton.setOnClickListener { registerFriend() }

        return rootView
    }

    /** The app should not really be handling ID logic, but since we can't register the friend
     * remotely on a server this will have to do. Get friend with highest id + 1.
     */
    private fun getPreferredUserID() {
        viewModel.getPreferredID().observe(this, Observer { saveResponse ->
            if (saveResponse?.status == Status.SUCCESS) {
                saveFriendID = saveResponse.data!!
            }
        })
    }

    private fun registerFriend() {

        var friend: Friend? = null

        try {
            friend = createFriend()
        } catch (exception: RegisterFriendException) {
            Snackbar.make(rootView, exception.displayMessage, ERROR_MESSAGE_DURATION).show()
        }

        if (friend != null) {
            saveFriendToDB(viewModel, friend)
        }
    }

    private fun saveFriendToDB(viewModel: FriendsViewModel, friend: Friend) {
        viewModel.saveFriend(friend).observe(this, Observer { saveResponse ->
            if (saveResponse?.status == Status.SUCCESS) {
                Snackbar.make(rootView, R.string.save_success, ERROR_MESSAGE_DURATION).show()
                requireActivity().onBackPressed()
            } else if (saveResponse?.status == Status.ERROR) {
                Snackbar.make(rootView, R.string.error_generic, ERROR_MESSAGE_DURATION).show()
            }
        })
    }

    //There should be more checks here, I just added a few for the demo.
    private fun createFriend(): Friend {
        return Friend(
            saveFriendID,
            FieldValidator.checkNullOrEmpty(rootView.nameEditText),
            FieldValidator.checkNullOrEmpty(rootView.userNameEditText),
            FieldValidator.checkEmail(rootView.emailEditText),
            FieldValidator.checkNullOrEmpty(rootView.emailEditText),
            rootView.webSiteEditText.text.toString()
        )
    }
}
