package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CreateGameUserCase @Inject constructor(private val gameRepository: GameRepository) {

    fun createGame(name: String): Single<Long> {
        val levelProgress = LevelProgress(number = 0, stage = LevelStage.INIT)

        return gameRepository.addLevelProgress(levelProgress)
            .flatMap { levelId ->
                val game = Game(
                    name = name,
                    lastModified = Date(),
                    levelId = levelId
                )

                gameRepository.addGame(game)
            }
    }
}