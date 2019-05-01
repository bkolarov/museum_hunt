package bg.tusofia.pmu.museumhunt

import androidx.databinding.Bindable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.usecases.game.CreateGameUserCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGameByNameUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGamesUseCase
import bg.tusofia.pmu.museumhunt.util.displayDateFormat
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import bg.tusofia.pmu.museumhunt.util.rx.toSingle
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getGamesUseCase: GetGamesUseCase,
    private val createGameUserCase: CreateGameUserCase,
    private val getGameByNameUseCase: GetGameByNameUseCase
) : BaseViewModel(resourceManager) {

    private val _newGameEvent = LiveEvent<Int>()
    private val _continueGameEvent = LiveEvent<Unit>()
    private val _browseGamesEvent = LiveEvent<Unit>()

    val newGameLiveEvent: LiveData<Int> = _newGameEvent
    val continueGameLiveEvent: LiveData<Unit> = _continueGameEvent
    val browseGamesLiveEvent: LiveData<Unit> = _browseGamesEvent

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
        val gameName = SimpleDateFormat(displayDateFormat, Locale.getDefault()).format(Date())
        createGameUserCase
            .createGame(gameName)
            .observeOn(Schedulers.computation())
            .toSingle()
            .flatMap {
                getGameByNameUseCase.getGameByName(gameName)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                _newGameEvent.value = it?.id
            })
            .addTo(container)
    }

    fun onContinueClick() {
        when {
            games?.size ?: 0 == 1 -> _continueGameEvent.value = Unit
            games?.size ?: 0 > 1 -> _browseGamesEvent.value = Unit
        }
    }

}