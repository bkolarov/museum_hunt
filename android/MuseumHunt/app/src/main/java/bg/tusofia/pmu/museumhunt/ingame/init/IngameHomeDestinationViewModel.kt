package bg.tusofia.pmu.museumhunt.ingame.init

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.ingame.usecase.DecideNextScreenByGameUseCase
import bg.tusofia.pmu.museumhunt.ingame.usecase.NextScreenArgs
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import javax.inject.Inject

class IngameHomeDestinationViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val gameRepository: GameRepository,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByGameUseCase
) : BaseViewModel(resourceManager) {

    val openUnityModuleEvent: LiveData<NextScreenArgs> = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent: LiveData<NextScreenArgs> = decideNextScreenByLevelUseCase.openRiddleScreenEvent

    fun decideAction(gameId: Long) {
        decideNextScreenByLevelUseCase.decideNextScreen(gameId)
            .subscribe()
            .addTo(container)
    }
}