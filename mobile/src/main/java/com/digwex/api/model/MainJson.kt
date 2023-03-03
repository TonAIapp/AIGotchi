package com.digwex.api.model

import java.io.Serializable

open class MainJson : Serializable {
  public var id: Int = 0
  public var duration: Double = 0.0
  public var audio: Int? = null
  public var content: Long = 0
  public var filter: Int = 0

  constructor()

  constructor(id: Int, duration: Double, audio: Int?, content: Long, filter: Int) {
    this.id = id
    this.audio = audio
    this.content = content
    this.duration = duration
    this.filter = filter
  }
}