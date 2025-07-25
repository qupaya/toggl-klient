package com.qupaya.toggl.ui

import com.qupaya.toggl.APP_NAME
import com.qupaya.toggl.userHome
import kotlinx.cinterop.ExperimentalForeignApi
import togglKlient.notify_init
import togglKlient.notify_notification_new
import togglKlient.notify_notification_show

@OptIn(ExperimentalForeignApi::class)
object Notification {
  init {
    notify_init(APP_NAME)
  }

  fun show(message: String) {
    val notification = notify_notification_new(
      APP_NAME,
      message,
      "${userHome}/.local/share/toggl/toggl.png"
    )
    notify_notification_show(notification, null)
  }
}