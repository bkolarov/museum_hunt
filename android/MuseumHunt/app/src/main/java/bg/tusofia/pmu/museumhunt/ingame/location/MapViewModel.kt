package bg.tusofia.pmu.museumhunt.ingame.location

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.functions.Consumer
import javax.inject.Inject

class MapViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val levelDataUseCase: GetLevelDataUseCase
) : BaseViewModel(resourceManager) {

    private val _showDestinationEvent = LiveEvent<LocationCoordinates>()

    val showDestinationEvent: LiveData<LocationCoordinates> = _showDestinationEvent

    fun initWithLevelId(levelId: Long) {
        getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            .flatMap {
                levelDataUseCase.getLevelData(it.number)
            }
            .map {
                it.stageLocation
            }
            .subscribe(Consumer {
                _showDestinationEvent.postValue(it.locationCoordinates)
            })
            .addTo(container)
    }

    fun onOpenInMapsClick() {

    }

    fun onHelpClick() {

    }

    fun onToolbarNavigationClick() {

    }

}