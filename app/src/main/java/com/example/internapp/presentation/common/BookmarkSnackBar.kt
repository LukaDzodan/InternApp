package com.example.internapp.presentation.common

import android.content.Context
import androidx.annotation.StringRes
import com.example.internapp.R

sealed class BookmarkSnackBar(@StringRes val snackMessage: Int) {
    data object Added : BookmarkSnackBar(R.string.favorite_added)
    data object Removed : BookmarkSnackBar(R.string.favorite_removed)
    data object Error : BookmarkSnackBar(R.string.favorite_error)
}

fun BookmarkSnackBar.toText(context: Context): String {
    return context.getString(snackMessage)
}
