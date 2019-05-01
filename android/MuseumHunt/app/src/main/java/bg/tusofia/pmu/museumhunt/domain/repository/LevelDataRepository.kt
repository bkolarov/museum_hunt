package bg.tusofia.pmu.museumhunt.domain.repository

import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import io.reactivex.Single

interface LevelDataRepository {

    fun getLevelData(levelNum: Int): Single<LevelData>

}

class LevelDataRepositoryImpl(private val resourceManager: ResourceManager) : LevelDataRepository {

    override fun getLevelData(levelNum: Int): Single<LevelData> {
        return try {
            val level1Answers = resourceManager.getStringArray(R.array.level_1_riddle_answers)
            val level2Answers = resourceManager.getStringArray(R.array.level_2_riddle_answers)
            val level3Answers = resourceManager.getStringArray(R.array.level_3_riddle_answers)

            val levelData = when (levelNum) {
                0 -> LevelData(
                    stageObstacle = StageObstacle(resourceManager.getStringArray(R.array.level_1_obstacle_hints).toList()),
                    stageRiddle = StageRiddle(
                        riddle = resourceManager.getString(R.string.level_1_riddle),
                        answers = listOf(
                            Answer(level1Answers[0]),
                            Answer(level1Answers[1]),
                            Answer(answer = level1Answers[2], isCorrect = true),
                            Answer(level1Answers[3])
                        )
                    ),
                    stageLocation = StageLocation(LocationCoordinates(42.6558745, 23.2654635))
                )
                1 -> LevelData(
                    stageObstacle = StageObstacle(resourceManager.getStringArray(R.array.level_2_obstacle_hints).toList()),
                    stageRiddle = StageRiddle(
                        riddle = resourceManager.getString(R.string.level_2_riddle),
                        answers = listOf(
                            Answer(answer = level2Answers[0], isCorrect = true),
                            Answer(level2Answers[1]),
                            Answer(level2Answers[2]),
                            Answer(level2Answers[3])
                        )
                    ),
                    stageLocation = StageLocation(LocationCoordinates(42.6552672, 23.2687588))
                )
                2 -> LevelData(
                    stageObstacle = StageObstacle(resourceManager.getStringArray(R.array.level_3_obstacle_hints).toList()),
                    stageRiddle = StageRiddle(
                        riddle = resourceManager.getString(R.string.level_3_riddle),
                        answers = listOf(
                            Answer(level3Answers[0]),
                            Answer(level3Answers[1]),
                            Answer(answer = level3Answers[2], isCorrect = true),
                            Answer(level3Answers[3])
                        )
                    ),
                    stageLocation = StageLocation(LocationCoordinates(42.687435, 23.335202))
                )
                else -> throw IllegalStateException("Wrong number level")
            }

            Single.just(levelData)
        } catch (e: Exception) {
            Single.error<LevelData>(e)
        }
    }
}

data class LevelData(
    val stageObstacle: StageObstacle,
    val stageRiddle: StageRiddle,
    val stageLocation: StageLocation
)

data class StageObstacle(val hintWords: List<String>)

data class StageRiddle(val riddle: String, val answers: List<Answer>)

data class Answer(val answer: String, val isCorrect: Boolean = false)

data class StageLocation(val locationCoordinates: LocationCoordinates)

data class LocationCoordinates(val longitude: Double, val latitude: Double)