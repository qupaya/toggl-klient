package com.qupaya.toggl.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalSerializationApi::class, ExperimentalTime::class)
@Serializable
@JsonIgnoreUnknownKeys
data class TimeEntry(
  val id: Long,
  val workspace_id: Long,
  @Contextual
  @Serializable(with = InstantSerializer::class)
  val start: Instant,
  @Contextual
  @Serializable(with = InstantSerializer::class)
  val at: Instant,
  val duration: Long,
  @Contextual
  @Serializable(with = InstantSerializer::class)
  val stop: Instant? = null,
)