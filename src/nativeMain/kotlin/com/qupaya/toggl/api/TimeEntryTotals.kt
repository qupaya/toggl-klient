package com.qupaya.toggl.api

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class TimeEntryTotals(
  val seconds: Long,
  val tracked_days: Int,
  val resolution: String,
  val graph: List<GraphEntry>
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class GraphEntry(val seconds: Long)
