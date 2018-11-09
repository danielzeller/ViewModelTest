package no.danielzeller.friends.ui.friend.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.friends_list_item.view.*
import no.danielzeller.friends.R
import no.danielzeller.friendsservice.model.Friend
import java.util.ArrayList

class FriendsAdapter(private val friendClickUnit: (friendID: Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var friends: List<Friend> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.friends_list_item, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friendViewHolder = holder as FriendViewHolder
        val friend = friends[position]
        friendViewHolder.name.text = friend.name
        friendViewHolder.userName.text = friend.userName
        friendViewHolder.itemView.setOnClickListener { friendClickUnit.invoke(friend.id) }
    }

    open inner class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.nameTextView
        val userName: TextView = view.userNameTextView
    }
}
