package bg.tusofia.pmu.museumhunt.domain.db.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

open class ListTypeConverters<T : Any> {

    @TypeConverter
    open fun toString(list: List<T>): String = gson.toJson(list)

    @TypeConverter
    open fun toList(json: String): List<T> {
        if (json.isEmpty()) return emptyList()

        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson<List<T>>(json, type)
    }

}

class StringListTypeConverters : ListTypeConverters<String>()