package bg.tusofia.pmu.museumhunt.ingame.usecase

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGamesUseCase
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueGameInput
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueLevelInput
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationInput
import bg.tusofia.pmu.museumhunt.ingame.init.NewGameInput
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DecideNextScreenByGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByLevelUseCase,
    private val getGamesUseCase: GetGamesUseCase
) {

    private val _openBrowseGamesEvent = LiveEvent<Unit>()

    val openUnityModuleEvent = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent = decideNextScreenByLevelUseCase.openRiddleScreenEvent
    val openMapScreenEvent = decideNextScreenByLevelUseCase.openMapScreenEvent
    val openBrowseGamesEvent: LiveData<Unit> = _openBrowseGamesEvent
    val openGameFinishedScreenEvent: LiveData<Unit> = decideNextScreenByLevelUseCase.openGameFinishedScreenEvent

    fun decideNextScreen(input: IngameHomeDestinationInput): Completable {
        return when (input) {
            is NewGameInput -> {
                Single.just(Unit).ignoreElement()
            }
            is ContinueGameInput -> {
                getGamesUseCase.getGames()
                    .flatMapCompletable {
                        if (it.size == 1) {
                            gameRepository.getGameById(it[0].id)
                                .flatMapCompletable { game ->
                                    decideNextScreenByLevelUseCase.decideNextScreenUseCase(
                                        IngameArgs(game.id, game.levelId)
                                    )
                                }
                        } else {
                            _openBrowseGamesEvent.postValue(Unit)
                            Completable.complete()
                        }
                    }
            }
            is ContinueLevelInput -> {
                decideNextScreenByLevelUseCase.decideNextScreenUseCase(input.ingameArgs)
            }
        }
    }

}