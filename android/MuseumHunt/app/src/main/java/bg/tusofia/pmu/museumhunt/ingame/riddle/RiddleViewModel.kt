package bg.tusofia.pmu.museumhunt.ingame.riddle

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.BR
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.repository.Answer
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RiddleViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val levelDataUseCase: GetLevelDataUseCase
) : BaseViewModel(resourceManager) {

    private var levelProgress: LevelProgress? = null

    private val _answersEvent = LiveEvent<List<Answer>>()
    private val _goBackEvent = LiveEvent<Unit>()

    val answersEvent: LiveData<List<Answer>> = _answersEvent
    val goBackEvent: LiveData<Unit> = _goBackEvent

    @get:Bindable
    var riddle: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.riddle)
        }

    fun init(levelId: Long) {
        getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                levelProgress = it
            }
            .observeOn(Schedulers.computation())
            .flatMap {
                levelDataUseCase.getLevelData(it.number)
            }
            .map { it.stageRiddle }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                _answersEvent.postValue(it.answers)
                riddle = it.riddle
            })
            .addTo(container)
    }

    fun onToolbarNavigationClick() {
        _goBackEvent.value = Unit
    }

    fun onHelpClick() {

    }

    fun onAnswerClick(answer: Answer) {

    }

}