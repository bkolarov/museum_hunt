package bg.tusofia.pmu.museumhunt.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bg.tusofia.pmu.museumhunt.domain.db.dao.GameDao
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.db.typeconverters.DateTypeConverters
import bg.tusofia.pmu.museumhunt.domain.db.typeconverters.LevelStageTypeConverters
import bg.tusofia.pmu.museumhunt.domain.db.typeconverters.StringListTypeConverters

@Database(version = DB_VERSION, entities = [Game::class])
@TypeConverters(
    value = [
        DateTypeConverters::class,
        LevelStageTypeConverters::class,
        StringListTypeConverters::class
    ]
)
abstract class MuseumHuntRoomDB : RoomDatabase() {

    abstract fun gameDao(): GameDao

}