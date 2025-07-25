package de.atennert.toggl.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class TimeEntryRequest(
  val created_with: String,
  val workspace_id: Long,
  @Contextual
  @Serializable(with = InstantSerializer::class)
  val start: Instant,
  val duration: Long,
  val tags: List<String> = emptyList(),
)
