package bg.tusofia.pmu.museumhunt.domain.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants

@Entity(tableName = TableConstants.LEVEL_PROGRESS)
data class LevelProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val number: Int,
    val stage: LevelStage,
    val hintWords: MutableList<String> = mutableListOf()
)

enum class LevelStage {
    INIT, OBSTACLE_PASSED, RIDDLE_PASSED, COMPLETED
}