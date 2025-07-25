package com.qupaya.toggl.api

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TimeEntryTotalsRequest(
  @Contextual
  @Serializable(with = LocalDateSerializer::class)
  val start_date: LocalDate,
  @Contextual
  @Serializable(with = LocalDateSerializer::class)
  val end_date: LocalDate,
  val rounding: Int = 0,
  val rounding_minutes: Int = 0,
  val with_graph: Boolean = true
)
