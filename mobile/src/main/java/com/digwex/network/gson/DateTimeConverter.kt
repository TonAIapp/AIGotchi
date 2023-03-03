package com.digwex.network.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import java.lang.reflect.Type

class DateTimeConverter : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
  // No need for an InstanceCreator since DateTime provides a no-args constructor
  override fun serialize(src: DateTime, srcType: Type, context: JsonSerializationContext): JsonElement {
    return JsonPrimitive(src.toString(dateFormat))
  }

  @Throws(JsonParseException::class)
  override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): DateTime {
    return DateTime(json.asString)
  }

  companion object{
    private  val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS")
  }
}
