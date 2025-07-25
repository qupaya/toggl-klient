package de.atennert.toggl.ui

import de.atennert.toggl.APP_NAME
import de.atennert.toggl.userHome
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import togglKlient.AppIndicatorCategory
import togglKlient.AppIndicatorStatus
import togglKlient._GtkMenu
import togglKlient.app_indicator_new
import togglKlient.app_indicator_set_icon
import togglKlient.app_indicator_set_menu
import togglKlient.app_indicator_set_status
import togglKlient.app_indicator_set_title

@OptIn(ExperimentalForeignApi::class)
object Indicator {
  @OptIn(ExperimentalForeignApi::class)
  val REF = app_indicator_new(
    "toggl-client-indicator",
    "${userHome}/.local/share/toggl/toggl.png",
    AppIndicatorCategory.APP_INDICATOR_CATEGORY_OTHER
  )

  fun init(menu: CValuesRef<_GtkMenu>?) {
    app_indicator_set_status(REF, AppIndicatorStatus.APP_INDICATOR_STATUS_ACTIVE)
    app_indicator_set_title(REF, APP_NAME)
    app_indicator_set_menu(REF, menu)
  }

  fun showInactive() {
    app_indicator_set_icon(REF, "${userHome}/.local/share/toggl/toggl-inactive.png")
  }

  fun showActive() {
    app_indicator_set_icon(REF, "${userHome}/.local/share/toggl/toggl.png")
  }

  fun setIndicatorTitle(title: String) {
    app_indicator_set_title(REF, title)
  }
}