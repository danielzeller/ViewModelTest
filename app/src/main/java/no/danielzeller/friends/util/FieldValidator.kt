@file:Suppress("RegExpRedundantEscape")

package no.danielzeller.friends.util

import android.widget.EditText
import no.danielzeller.friends.R
import no.danielzeller.friends.ui.friend.register.RegisterFriendException
import java.util.regex.Pattern


class FieldValidator private constructor() {
    companion object {

        fun checkNullOrEmpty(editText: EditText): String {
            val text = editText.text
            if (text.isNullOrEmpty()) {
                throw RegisterFriendException(editText.resources.getString(R.string.fill_in, editText.hint))
            }
            return text.toString()
        }

        fun checkEmail(editText: EditText): String {
            val text = checkNullOrEmpty(editText)
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(text)

            if (!matcher.matches()) {
                throw RegisterFriendException(editText.resources.getString(R.string.email_not_valid))
            }
            return text
        }
    }
}
