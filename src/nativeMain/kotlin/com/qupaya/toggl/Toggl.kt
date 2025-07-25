package com.qupaya.toggl

import com.qupaya.toggl.api.TogglApi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class Toggl(config: Configuration) {
  private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
  private val firstOfMonth = LocalDate(today.year, today.month, 1)
  private val togglApi = TogglApi(config.apiKey)
  private val workspace = togglApi.getWorkspace()
  var currentEntry = togglApi.loadRunningTimeEntry()
    private set
  private val formerEntries = togglApi.getTimeEntriesAndTotals(workspace, firstOfMonth, today)
  private var doneSecondsToday = formerEntries.graph.last().seconds
  private val targetHours = config.workHoursPerDay * (formerEntries.tracked_days + (if (doneSecondsToday > 0) 0 else 1))

  fun hasRunningTimeEntry(): Boolean = currentEntry != null

  fun start() {
    currentEntry = togglApi.startATimeEntry(workspace, Clock.System.now())
  }

  fun stop() {
    currentEntry?.run {
      togglApi.stopTimeEntry(this)
    }
    val runningSeconds = getCurrentRuntimeSeconds()
    currentEntry = null
    updateDoneTime(runningSeconds)
  }

  fun switch() {
    currentEntry?.run {
      togglApi.stopTimeEntry(this)
    }
    val runningSeconds = getCurrentRuntimeSeconds()
    currentEntry = togglApi.startATimeEntry(workspace, Clock.System.now())
    updateDoneTime(runningSeconds)
  }

  private fun getCurrentRuntimeSeconds(): Long {
    return currentEntry?.let {
      val end = Clock.System.now()
      end.epochSeconds - it.start.epochSeconds
    } ?: 0
  }

  private fun updateDoneTime(runningSeconds: Long) {
    doneSecondsToday += runningSeconds
  }

  fun getTimeToDo(): Double {
    val doneHoursMonth = (formerEntries.seconds - formerEntries.graph.last().seconds) / 3600.0
    val doneHoursToday = doneSecondsToday / 3600.0
    val runningHours = currentEntry?.let {
      val end = Clock.System.now()
      val runningSeconds = (end.epochSeconds - it.start.epochSeconds)
      runningSeconds / 3600.0
    } ?: 0.0
    return targetHours - doneHoursMonth - doneHoursToday - runningHours
  }
}