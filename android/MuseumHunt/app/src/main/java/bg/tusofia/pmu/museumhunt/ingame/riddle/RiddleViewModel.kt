package bg.tusofia.pmu.museumhunt.ingame.riddle

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import bg.tusofia.pmu.museumhunt.BR
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.dialog.DialogValues
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelProgress
import bg.tusofia.pmu.museumhunt.domain.db.entity.LevelStage
import bg.tusofia.pmu.museumhunt.domain.repository.Answer
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.SetRiddleAnsweredUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.UpdateLevelStageUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.ingame.IngameArgs
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RiddleViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val levelDataUseCase: GetLevelDataUseCase,
    private val setRiddleAnsweredUseCase: SetRiddleAnsweredUseCase
) : BaseViewModel(resourceManager) {

    private lateinit var ingameArgs: IngameArgs
    private var levelProgress: LevelProgress? = null

    private val _answersEvent = LiveEvent<List<Answer>>()
    private val _goBackEvent = LiveEvent<Unit>()
    private val _errorSnackBarEvent = LiveEvent<Unit>()
    private val _nextScreenEvent = LiveEvent<IngameArgs>()
    private val _showDialogEvent = LiveEvent<DialogValues>()

    val answersEvent: LiveData<List<Answer>> = _answersEvent
    val goBackEvent: LiveData<Unit> = _goBackEvent
    val errorSnackBarEvent: LiveData<Unit> = _errorSnackBarEvent
    val nextScreenEevent: LiveData<IngameArgs> = _nextScreenEvent
    val showDialogEvent: LiveData<DialogValues> = _showDialogEvent

    @get:Bindable
    var riddle: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.riddle)
        }

    fun init(ingameArgs: IngameArgs) {
        this.ingameArgs = ingameArgs
        getLevelProgressDataUseCase.getLevelProgressDataUseCase(ingameArgs.levelId)
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
        _showDialogEvent.value = DialogValues().apply {
            title = getString(R.string.dialog_riddle_hint_title)
            message = levelProgress?.hintWords?.joinToString(", ")
            positiveBtnTxt = getString(R.string.ok)
        }
    }

    fun onAnswerClick(answer: Answer) {
        if (!answer.isCorrect) {
            _errorSnackBarEvent.value = Unit
        } else {
            setRiddleAnsweredUseCase.setRiddleAnswered(ingameArgs.levelId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _nextScreenEvent.postValue(ingameArgs)
                }
                .addTo(container)
        }
    }

}