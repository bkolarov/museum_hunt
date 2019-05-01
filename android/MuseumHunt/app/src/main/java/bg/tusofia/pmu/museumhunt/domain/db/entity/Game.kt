package bg.tusofia.pmu.museumhunt.domain.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants
import java.util.*

@Entity(tableName = TableConstants.GAMES)
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val lastModified: Date,
    @Embedded val levelProgress: LevelProgress
)

data class LevelProgress(
    val number: Int,
    val stage: LevelStage,
    val hintWords: List<String>
)

enum class LevelStage {
    INIT, OBSTACLE_PASSED, RIDDLE_PASSED, COMPLETED
}

