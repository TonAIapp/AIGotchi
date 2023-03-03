package com.digwex.api.model

import org.joda.time.DateTime

class TimeJson {
  public lateinit var utc: DateTime
  public var offset: Int = 0
}