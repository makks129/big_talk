package com.makks.bigtalk.global.extensions

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * My Kotlin Extensions
 */


// Fragment --- start

inline fun <F : Fragment> F.putArgs(argsBuilder: Bundle.() -> Unit): F =
        apply { arguments = Bundle().apply(argsBuilder) }

// Fragment --- end


// LiveData --- start

class EmptyLiveData<T> : MutableLiveData<T>()

fun <T> LiveData<T>.isEmpty() = this is EmptyLiveData

fun <T> MediatorLiveData<T>.removeAndAddSource(@NonNull source: LiveData<T>, onChanged: (t: T) -> Unit) {
    removeSource(source)
    addSource(source, onChanged)
}

fun <List, Elem> LiveData<List>.addValue(additional: Elem) {
    if (value is MutableCollection<*>) (value as MutableCollection<Elem>).add(additional)
}

// LiveData --- end

// Dialog --- start

fun Dialog.showKeyboard() =
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

// Dialog --- end

// Views --- start

class TriStateViewGroups(parent: View, contentResId: Int, loadingResId: Int, errorResId: Int) {
    val content: View = parent.findViewById(contentResId)
    val loading: View = parent.findViewById(loadingResId)
    val error: View = parent.findViewById(errorResId)

    fun showContent() = arrayOf(content, loading, error).showFirstHideRest()
    fun showLoading() = arrayOf(loading, error, content).showFirstHideRest()
    fun showError() = arrayOf(error, content, loading).showFirstHideRest()
}

fun <V : View> Array<V>.showFirstHideRest() =
        this.map { it.apply { visibility = GONE } }.first().apply { visibility = VISIBLE }

// Views --- end