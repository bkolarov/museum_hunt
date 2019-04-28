package bg.tusofia.pmu.museumhunt.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bg.tusofia.pmu.museumhunt.domain.db.dao.GameDao
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game

@Database(version = DB_VERSION, entities = [Game::class])
abstract class MuseumHuntRoomDB : RoomDatabase() {

    abstract fun gameDao(): GameDao

}