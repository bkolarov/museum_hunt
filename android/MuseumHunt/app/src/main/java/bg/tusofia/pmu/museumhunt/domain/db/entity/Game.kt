package bg.tusofia.pmu.museumhunt.domain.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import bg.tusofia.pmu.museumhunt.domain.db.TableConstants
import java.util.*

@Entity(
    tableName = TableConstants.GAMES,
    foreignKeys = [
        ForeignKey(
            entity = LevelProgress::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            parentColumns = ["id"],
            childColumns = ["levelId"]
        )],
    indices = [Index("levelId", unique = true)]
)
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val lastModified: Date,
    val levelId: Long
)
