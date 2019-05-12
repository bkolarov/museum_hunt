package bg.tusofia.pmu.museumhunt.ingame.init

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.usecase.DecideNextScreenByGameUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import javax.inject.Inject

class IngameHomeDestinationViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByGameUseCase
) : BaseViewModel(resourceManager) {

    val openUnityModuleEvent: LiveData<IngameArgs> = decideNextScreenByLevelUseCase.openUnityModuleEvent
    val openRiddleScreenEvent: LiveData<IngameArgs> = decideNextScreenByLevelUseCase.openRiddleScreenEvent

    fun decideAction(input: IngameHomeDestinationInput) {
        decideNextScreenByLevelUseCase.decideNextScreen(input)
            .subscribe()
            .addTo(container)
    }
}