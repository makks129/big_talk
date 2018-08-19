package com.makks.bigtalk.global.extensions

import androidx.lifecycle.Observer

/**
 * Event is a data wrapper that can only be handled once
 * Use together with {@link EventObserver}
 * For more see: <a href="https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150"/>
 */
open class Event<out T>(private val data: T) {
    var isUnhandled = true
        private set

    fun getUnhandledData() =
            if (isUnhandled) {
                isUnhandled = false
                data
            } else null

    fun peekData(): T? = data
}

/**
 * Observer for {@link Event} that wraps data handling operation
 * For more see: <a href="https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9"/>
 */
class EventObserver<T>(private val onHandleEvent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getUnhandledData()?.let(onHandleEvent)
    }
}

sealed class Resource<T> { // Type parameter IS used and IS necessary
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val t: Throwable) : Resource<T>()
}

sealed class EventResource<T> { // Type parameter IS used and IS necessary

    class Loading<T> : EventResource<T>()

    class Success<T>(val data: T?) : EventResource<T>() {
        var isUnhandled = true
            private set

        fun getUnhandledData() =
                if (isUnhandled) {
                    isUnhandled = false
                    data
                } else null

        fun peekData(): T? = data
    }

    class Error<T>(val t: Throwable) : EventResource<T>() {
        var isUnhandled = true
            private set

        fun getUnhandledError() =
                if (isUnhandled) {
                    isUnhandled = false
                    t
                } else null

        fun peekError(): Throwable = t
    }
}

class EventResourceObserver<T>(private val onHandleEvent: (EventResource<T>) -> Unit) : Observer<EventResource<T>> {
    override fun onChanged(it: EventResource<T>) {
        when (it) {
            is EventResource.Loading -> onHandleEvent(it)
            is EventResource.Success -> if (it.isUnhandled) {
                onHandleEvent(it)
                it.getUnhandledData() // ensure handling afterwards
            }
            is EventResource.Error -> if (it.isUnhandled) {
                onHandleEvent(it)
                it.getUnhandledError() // ensure handling afterwards
            }
        }
    }
}