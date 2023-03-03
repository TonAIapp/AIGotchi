package com.digwex.api.model

import java.io.Serializable

class PlaylistJson : Serializable {
  public var id: Int = 0
  public var schedules: Array<Int> = arrayOf()
  public var audios: Array<Int> = arrayOf()
  public var main: Array<MainJson> = arrayOf()
  public var widgets: Array<Long> = arrayOf()

//  var items: Array<Item> = arrayOf()
//
//  class Item : Serializable {
//    var duration: Int = 0
//    var main: LayoutMain = LayoutMain()
//    var widgets: Array<LayoutWidget> = arrayOf()
//
//    class LayoutMain : Serializable {
//      var id: Int = 0
//      var position: Array<Int>? = null
//    }
//
//    class LayoutWidget : Serializable {
//      var id: Int = 0
//      var position: Array<Int>? = null
//    }
//  }
}