@file:OptIn(ExperimentalForeignApi::class)

package com.qupaya.toggl

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import platform.posix.getenv

@ExperimentalForeignApi
val userHome = getenv("HOME")?.toKString()

const val APP_NAME = "Toggl Klient"

@Serializable
data class Configuration(val apiKey: String, val workHoursPerDay: Double) {
  companion object {
    const val CONFIGURATION_FILE = "/.config/toggl-klient/config.json"
    val userConfigFile = "$userHome$CONFIGURATION_FILE"

    fun load(): Configuration {
      if (!PosixFiles.exists(userConfigFile)) {
        throw RuntimeException("Configuration file not found: $userConfigFile")
      }

      var configContent = ""
      PosixFiles.readLines(userConfigFile) { s -> configContent += s }
      return Json.decodeFromString<Configuration>(configContent)
    }
  }
}
