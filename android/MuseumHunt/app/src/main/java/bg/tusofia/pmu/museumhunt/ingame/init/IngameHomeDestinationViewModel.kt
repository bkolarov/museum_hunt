package bg.tusofia.pmu.museumhunt.ingame.init

import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.GameRepository
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import javax.inject.Inject

class IngameHomeDestinationViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val gameRepository: GameRepository) : BaseViewModel(resourceManager) {

    private val _openUnityModuleEvent = LiveEvent<Int>()

    val openUnityModuleEvent: LiveData<Int> = _openUnityModuleEvent

    fun decideAction(gameId: Int) {

        gameRepository.getGameById(gameId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                it?.let { game ->

                    when (game.levelProgress.stage) {
                        LevelStage.INIT -> _openUnityModuleEvent.value = game.levelProgress.number
                    }

                }
            })
            .addTo(container)
    }
}