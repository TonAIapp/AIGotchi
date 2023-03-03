package com.digwex

import com.digwex.service.TimeService

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object CorrectTime {

  val utcNow: DateTime
    get() {
      return DateTime(DateTimeZone.UTC).plus(TimeService.delta)
    }

  val now: DateTime
    get() {
      return DateTime(DateTimeZone.UTC).plus(TimeService.delta + TimeService.offset)
    }
}
