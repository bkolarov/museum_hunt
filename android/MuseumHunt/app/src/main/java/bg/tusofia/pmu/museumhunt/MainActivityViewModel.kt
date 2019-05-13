package bg.tusofia.pmu.museumhunt

import androidx.databinding.Bindable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.usecases.game.CreateGameUserCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGamesSortedByDateUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGamesUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getGamesUseCase: GetGamesUseCase,
    private val createGameUserCase: CreateGameUserCase,
    private val getGamesSortedByDateUseCase: GetGamesSortedByDateUseCase
) : BaseViewModel(resourceManager) {

    private val _newGameEvent = LiveEvent<Pair<Long, Long>>()
    private val _continueGameEvent = LiveEvent<Unit>()
    private val _requireGameNameEvent = LiveEvent<Unit>()

    val newGameLiveEvent: LiveData<Pair<Long, Long>> = _newGameEvent
    val continueGameLiveEvent: LiveData<Unit> = _continueGameEvent
    val requireGameNameEvent: LiveData<Unit> = _requireGameNameEvent


    private var games: List<Game>? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.btnContinueVisible)
        }

    @get:Bindable
    val btnContinueVisible
        get() = games?.isNotEmpty() ?: false

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getGamesUseCase.getGames()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                games = it
            })
            .addTo(container)
    }

    fun onNewGameClick() {
        _requireGameNameEvent.postValue(Unit)
    }

    fun onCreateGameWithName(name: String?) {
        if (name.isNullOrBlank()) return

        createGameUserCase.createGame(name)
            .subscribe(Consumer {
                _newGameEvent.postValue(it.id to it.levelId)
            })
            .addTo(container)
    }

    fun onContinueClick() {
        _continueGameEvent.value = Unit
    }

}