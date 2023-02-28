package com.reemmousa.eduapp.app

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.reemmousa.eduapp.R
import com.squareup.picasso.Picasso

const val TAG = "Extensions"

fun Application.hasInternetConnection(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> return false
    }
}

fun Activity.showAppDialog(body: String, posClicked: () -> Unit, negClicked: () -> Unit) {
    AlertDialog.Builder(this)
        .setMessage(body)
        .setPositiveButton(getString(R.string.ok)) { _, _ -> posClicked() }
        .setNegativeButton(getString(R.string.cancel)) { _, _ -> negClicked() }
        .create()
        .show()
}

fun Activity.showSnackBar(
    message: String,
    actionTxt: String = getString(R.string.ok),
    actionFn: () -> Unit = {}
) {
    try {
        Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionTxt) {
                actionFn()
            }
            .show()
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
    }
}

fun ImageView.loadImageWithPicasso(imageUri: String) {
    Picasso.with(context).load(imageUri).fit().centerCrop()
        .placeholder(R.drawable.image_gallery)
        .error(R.drawable.error)
        .into(this);
}

fun Application.loadDrawable(@DrawableRes id: Int): Drawable {
    return AppCompatResources.getDrawable(applicationContext, id)!!
}

fun Context.loadColor(@ColorRes id: Int): Int {
    return try {
        getColor(id)
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        0
    }
}

fun Context.viewLinkOnUdemy(url: String) {
    try {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.UDEMY_BASE_URL}${url}"))
        startActivity(browserIntent)
    } catch (e: Exception) {
        Log.e(TAG, e.message, e)
    }
}

fun Activity.updateStatusBarColor(@ColorRes colorId: Int, isLight: Boolean = true) {

    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = loadColor(colorId)
    if (isLight) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
fun Context.viewLink(url: String) {
    try {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {
        Log.e(TAG, e.message, e)
    }
}