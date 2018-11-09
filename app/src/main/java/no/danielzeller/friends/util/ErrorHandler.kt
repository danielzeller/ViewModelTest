package no.danielzeller.friends.util

import android.support.design.widget.Snackbar
import android.view.View
import no.danielzeller.friends.R
import java.net.UnknownHostException


class ErrorHandler private constructor() {
    companion object {
        fun handleError(containerView: View, throwable: Throwable?) {
            var errorMessageID = R.string.error_generic

            if (throwable is UnknownHostException) {
                errorMessageID = R.string.error_network
            }

            Snackbar.make(containerView, errorMessageID, Snackbar.LENGTH_LONG).show()
        }
    }
}
