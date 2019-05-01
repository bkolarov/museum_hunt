package bg.tusofia.pmu.museumhunt.domain.db.typeconverters

import androidx.room.TypeConverter
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage

class LevelStageTypeConverters {

    @TypeConverter
    fun toEnum(index: Int): LevelStage = LevelStage.values()[index]

    @TypeConverter
    fun fromEnum(enum: LevelStage) = enum.ordinal

}