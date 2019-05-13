package bg.tusofia.pmu.museumhunt.ingame.init

import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.ingame.usecase.DecideNextScreenByGameUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import javax.inject.Inject

class IngameHomeDestinationViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByGameUseCase
) : BaseViewModel(resourceManager) {

    val openUnityModuleEvent get() = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent get() = decideNextScreenByLevelUseCase.openRiddleScreenEvent
    val openMapScreenEvent get() = decideNextScreenByLevelUseCase.openMapScreenEvent
    val openBrowseGamesEvent get() = decideNextScreenByLevelUseCase.openBrowseGamesEvent
    val openGameFinishedEvent get() = decideNextScreenByLevelUseCase.openGameFinishedScreenEvent

    fun decideAction(input: IngameHomeDestinationInput) {
        decideNextScreenByLevelUseCase.decideNextScreen(input)
            .subscribe()
            .addTo(container)
    }
}