package com.digwex.api.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException

import java.lang.reflect.Type

//class ContentJsonDeserializer : JsonDeserializer<ContentJson> {
//
//  @Throws(JsonParseException::class)
//  override fun deserialize(json: JsonElement, typeOfT: Type,
//                           context: JsonDeserializationContext): ContentJson? {
////    if (json.isJsonNull) return null
////
////    val obj: JsonObject = json.asJsonObject ?: return null
////    return //if (obj.has("md5")) {
////      context.deserialize<ContentJson>(json, ContentJson::class.java)
////    //} //else //{
////      ContentJson(obj.get("id").asInt,
////        obj.get("type").asString,
////        obj.get("url").asString)
//    //}
//  }
//
//}