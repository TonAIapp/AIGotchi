package com.digwex.api.model

import java.io.Serializable

open class ContentJson : Serializable {
  public var id: Int = 0
  public var type: String = ""
  public var url: String = ""
  public var size: Long = 0
  public var ext: String = ""
  public var md5: String = ""

  constructor()

  constructor(id: Int, type: String, url: String, size: Long, md5: String) {
    this.id = id
    this.type = type
    this.url = url
    this.size = size
    this.md5 = md5
  }
}