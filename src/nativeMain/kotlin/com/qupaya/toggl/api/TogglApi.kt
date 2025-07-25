package com.qupaya.toggl.api

import com.qupaya.toggl.APP_NAME
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.curl.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TogglApi(private val apiKey: String) {
  private val client = HttpClient(Curl) {
    install(ContentNegotiation) {
      json()
    }
    install(Auth) {
      basic {
        credentials {
          BasicAuthCredentials(username = apiKey, password = "api_token")
        }
        sendWithoutRequest { true }
      }
    }
  }

  fun getWorkspace(): Workspace = runBlocking {
    val response = client.get("https://api.track.toggl.com/api/v9/workspaces")

    return@runBlocking response.body<Array<Workspace>>()[0]
  }

  fun getTimeEntriesAndTotals(workspace: Workspace, start: LocalDate, end: LocalDate): TimeEntryTotals = runBlocking {
    val response = client.post("https://api.track.toggl.com/reports/api/v3/workspace/${workspace.id}/search/time_entries/totals") {
      contentType(ContentType.Application.Json)
      setBody(TimeEntryTotalsRequest(start, end))
    }

    return@runBlocking response.body()
  }

  fun loadRunningTimeEntry(): TimeEntry? = runBlocking {
    try {
      val response = client.get("https://api.track.toggl.com/api/v9/me/time_entries/current")

      return@runBlocking response.body<TimeEntry>()
    } catch (_: Exception) {
      return@runBlocking null
    }
  }

  @OptIn(ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
  fun startATimeEntry(workspace: Workspace, start: Instant): TimeEntry = runBlocking {
    val response = client.post("https://api.track.toggl.com/api/v9/workspaces/${workspace.id}/time_entries") {
      contentType(ContentType.Application.Json)
      setBody(TimeEntryRequest(APP_NAME, workspace.id, start, -1))
    }

    return@runBlocking response.body()
  }

  fun stopTimeEntry(timeEntry: TimeEntry): TimeEntry = runBlocking {
    val response = client.patch("https://api.track.toggl.com/api/v9/workspaces/${timeEntry.workspace_id}/time_entries/${timeEntry.id}/stop") {
      contentType(ContentType.Application.Json)
      setBody("")
    }

    return@runBlocking response.body()
  }
}