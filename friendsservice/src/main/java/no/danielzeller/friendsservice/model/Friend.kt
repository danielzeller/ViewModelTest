package no.danielzeller.friendsservice.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Friend(
    @PrimaryKey val id: Int,
    val name: String,

    @SerializedName("username")
    val userName: String,

    val email: String,
    val phone: String,
    val website: String
) {
    override fun toString(): String {
        return "Friend(id=$id, name='$name', userName='$userName', email='$email', phone='$phone', website='$website')"
    }
}