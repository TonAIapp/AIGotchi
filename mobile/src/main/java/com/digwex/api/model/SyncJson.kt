package com.digwex.api.model

import com.google.gson.stream.JsonToken
import java.io.Serializable

class SyncJson : Serializable {
    var id: Int = 0
    var force: Boolean = false
    var schedules: Array<ScheduleJson> = arrayOf()
    var playlists: Array<PlaylistJson> = arrayOf()
    var positions: Array<Int> = arrayOf()
    var templates: Array<JsonToken> = arrayOf()
    var contents: Array<ContentJson> = arrayOf()
    var data: Array<DataJson>? = arrayOf()
}

