package bg.tusofia.pmu.museumhunt.domain.usecases.game

import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGameByNameUseCase @Inject constructor(private val gameRepository: GameRepository) {

    fun getGameByName(name: String): Single<Game?> {
        return gameRepository.getGames()
            .observeOn(Schedulers.computation())
            .map { games ->
                games.find { it.name == name }
            }
    }

}