package com.digwex.api.model

import com.google.gson.JsonElement
import java.io.Serializable

open class  DataJson : Serializable {
  public var id: Long = 0
  public var version: Long = 0
  public var data: JsonElement? = null
  public var settings: JsonElement? = null
  public var sources: HashMap<Int, ContentJson>? = null
  public var path: String = ""
}