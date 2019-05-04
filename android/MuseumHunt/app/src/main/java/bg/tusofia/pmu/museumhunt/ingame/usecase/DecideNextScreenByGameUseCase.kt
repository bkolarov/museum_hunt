package bg.tusofia.pmu.museumhunt.ingame.usecase

import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.util.rx.toSingle
import io.reactivex.Completable
import javax.inject.Inject

class DecideNextScreenByGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByLevelUseCase
) {

    val openUnityModuleEvent = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent = decideNextScreenByLevelUseCase.openRiddleScreenEvent

    fun decideNextScreen(gameId: Long): Completable {
        return gameRepository.getGameById(gameId)
            .flatMap {
                decideNextScreenByLevelUseCase.decideNextScreenUseCase(it.levelId).toSingle()
            }
            .ignoreElement()
    }

}