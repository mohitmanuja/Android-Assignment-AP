package com.image.assignment.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object CustomDispatchers {
    val IO: CoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
}