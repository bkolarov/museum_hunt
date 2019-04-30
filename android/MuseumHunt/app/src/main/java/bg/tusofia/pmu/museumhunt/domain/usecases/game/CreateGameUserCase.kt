package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class CreateGameUserCase @Inject constructor(private val gameRepository: GameRepository) {

    fun createGame(name: String): Completable {
        val game = Game(
            name = name,
            lastModified = Date(),
            levelProgress = LevelProgress(0, LevelStage.INIT, emptyList())
        )

        return gameRepository.addGame(game)
    }
}