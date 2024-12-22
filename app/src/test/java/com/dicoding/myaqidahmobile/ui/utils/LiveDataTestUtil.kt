package com.dicoding.myaqidahmobile.ui.utils

import androidx.lifecycle.*
import java.util.concurrent.*

@Suppress("UNCHECKED_CAST")
fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }

    }
    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)

    return data as T
}


//observe Livedata sampai block selesai dieksekusi
suspend fun <T> LiveData<T>.observeForTesting(block: suspend () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}