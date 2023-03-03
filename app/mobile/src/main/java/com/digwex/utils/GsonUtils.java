package com.digwex.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;


public class GsonUtils {
    private static JsonObject deepCopy(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            result.add(entry.getKey(), deepCopy(entry.getValue()));
        }
        return result;
    }

    private static JsonArray deepCopy(JsonArray jsonArray) {
        JsonArray result = new JsonArray();
        for (JsonElement e : jsonArray) {
            result.add(deepCopy(e));
        }
        return result;
    }

    public static JsonElement deepCopy(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive() || jsonElement.isJsonNull()) {
            return jsonElement;       // these are immutables anyway
        } else if (jsonElement.isJsonObject()) {
            return deepCopy(jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonArray()) {
            return deepCopy(jsonElement.getAsJsonArray());
        } else {
            throw new UnsupportedOperationException("Unsupported element: " + jsonElement);
        }
    }
}
