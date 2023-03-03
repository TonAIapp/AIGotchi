package com.digwex.api.model

import java.io.Serializable

class ScheduleJson : Serializable {
    var id = 0
    var weekDays: Map<Int, String?>? = null
    var timestamps: Map<Long, String?>? = null
}
