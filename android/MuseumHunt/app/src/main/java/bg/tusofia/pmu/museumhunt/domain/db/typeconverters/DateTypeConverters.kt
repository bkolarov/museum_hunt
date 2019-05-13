package bg.tusofia.pmu.museumhunt.domain.db.typeconverters

import androidx.room.TypeConverter
import java.util.*

class DateTypeConverters {
    @TypeConverter
    fun toLong(dateTime: Date): Long = dateTime.time

    @TypeConverter
    fun toDate(dateMillis: Long): Date = Date(dateMillis)
}
