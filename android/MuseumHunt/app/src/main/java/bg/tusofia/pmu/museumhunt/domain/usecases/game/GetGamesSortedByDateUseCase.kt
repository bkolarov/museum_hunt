package bg.tusofia.pmu.museumhunt.domain.usecases.game

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGamesSortedByDateUseCase @Inject constructor(private val getGamesUseCase: GetGamesUseCase) {

    fun getGamesSortedByDate() = getGamesUseCase.getGames()
        .observeOn(Schedulers.computation())
        .map {  games ->
            games.sortedByDescending { it.lastModified }
        }

}