package com.pashuaahar.data.local.database

import androidx.room.TypeConverter
import com.pashuaahar.data.local.entity.RecipeIngredientEntity
import org.json.JSONArray
import org.json.JSONObject

class Converters {

    @TypeConverter
    fun fromRecipeIngredients(value: List<RecipeIngredientEntity>): String {
        val jsonArray = JSONArray()
        value.forEach { ingredient ->
            val jsonObject = JSONObject()
                .put("grainId", ingredient.grainId)
                .put("grainName", ingredient.grainName)
                .put("quantityKg", ingredient.quantityKg)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toRecipeIngredients(value: String): List<RecipeIngredientEntity> {
        if (value.isBlank()) return emptyList()
        val jsonArray = JSONArray(value)
        return buildList {
            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(index)
                add(
                    RecipeIngredientEntity(
                        grainId = item.getString("grainId"),
                        grainName = item.getString("grainName"),
                        quantityKg = item.getDouble("quantityKg")
                    )
                )
            }
        }
    }
}
