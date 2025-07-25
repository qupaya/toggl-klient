package com.qupaya.toggl.ui

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValuesOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import togglKlient.gtk_events_pending
import togglKlient.gtk_init
import togglKlient.gtk_main_iteration
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalForeignApi::class)
class GtkApplication(override val coroutineContext: CoroutineContext) : CoroutineScope {
  private var stop = false

  init {
    gtk_init(cValuesOf(0), null)
  }

  fun runMainLoop() = runBlocking {
    while(!stop) {
      while (gtk_events_pending() != 0) {
        gtk_main_iteration()
      }
      delay(100)
    }
  }

  fun quitMainLoop() {
    stop = true
  }

  companion object {
    fun create(f: suspend GtkApplication.() -> Unit) = runBlocking {
      f(GtkApplication(coroutineContext))
    }
  }
}
