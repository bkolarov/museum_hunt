package bg.tusofia.pmu.museumhunt.ingame.usecase

import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.init.ContinueGameInput
import bg.tusofia.pmu.museumhunt.ingame.init.IngameHomeDestinationInput
import bg.tusofia.pmu.museumhunt.ingame.init.NewGameInput
import bg.tusofia.pmu.museumhunt.util.rx.toSingle
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DecideNextScreenByGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByLevelUseCase
) {

    val openUnityModuleEvent = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent = decideNextScreenByLevelUseCase.openRiddleScreenEvent

    fun decideNextScreen(input: IngameHomeDestinationInput): Completable {
        return when (input) {
            is NewGameInput -> {
                Single.just(Unit).ignoreElement()
            }
            is ContinueGameInput -> {
                gameRepository.getGameById(input.gameId)
                    .flatMap { game ->
                        decideNextScreenByLevelUseCase.decideNextScreenUseCase(IngameArgs(game.id, game.levelId)).toSingle()
                    }
                    .ignoreElement()
            }
        }
    }

}