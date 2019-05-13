package bg.tusofia.pmu.museumhunt.ingame.unity

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.usecases.game.SetObstaclesPassedUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.ConvertUnityDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetUnityModuleDataUseCase
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.ingame.usecase.DecideNextScreenByLevelUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import timber.log.Timber
import javax.inject.Inject

class UnityLauncherViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getUnityModuleDataUseCase: GetUnityModuleDataUseCase,
    private val convertUnityDataUseCase: ConvertUnityDataUseCase,
    private val setObstaclesPassedUseCase: SetObstaclesPassedUseCase,
    private val decideNextScreenByLevelUseCase: DecideNextScreenByLevelUseCase
) : BaseViewModel(resourceManager) {

    private lateinit var ingameArgs: IngameArgs

    private val _launchUnityModuleEvent = LiveEvent<String>()
    private val _goBackEvent = LiveEvent<Unit>()

    val launchUnityModuleLiveEvent: LiveData<String> = _launchUnityModuleEvent
    val openRiddleScreenEvent: LiveData<IngameArgs> = decideNextScreenByLevelUseCase.openRiddleScreenEvent

    val goBackEvent: LiveData<Unit> = _goBackEvent

    fun initForLevel(ingameArgs: IngameArgs) {
        this.ingameArgs = ingameArgs

        getUnityModuleDataUseCase.getUnityModuleData(ingameArgs.levelId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                _launchUnityModuleEvent.value = it
            })
            .addTo(container)
    }

    fun onObstaclesPassed(unityModuleResult: String) {
        Timber.d("obstacles passed: $unityModuleResult")
        val convertedResult = convertUnityDataUseCase.convertUnityData(unityModuleResult)

        setObstaclesPassedUseCase.setObstaclesPassedUseCase(ingameArgs.levelId, convertedResult)
            .observeOn(AndroidSchedulers.mainThread())
            .andThen(decideNextScreenByLevelUseCase.decideNextScreenUseCase(ingameArgs))
            .subscribe()
            .addTo(container)
    }

    fun onObstacleExit() {
        _goBackEvent.value = Unit
    }


}