package bg.tusofia.pmu.museumhunt.domain.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants

@Entity(tableName = TableConstants.GAMES)
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)