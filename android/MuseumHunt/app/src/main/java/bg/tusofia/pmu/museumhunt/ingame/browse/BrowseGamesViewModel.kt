package bg.tusofia.pmu.museumhunt.ingame.browse

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.db.entity.Game
import bg.tusofia.pmu.museumhunt.domain.usecases.game.CreateGameUserCase
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetGamesUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BrowseGamesViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getGamesUseCase: GetGamesUseCase,
    private val createGameUserCase: CreateGameUserCase
) : BaseViewModel(resourceManager) {

    private val _gamesLiveData = MutableLiveData<List<Game>>()
    private val _continueLevelEvent = LiveEvent<Pair<Long, Long>>()
    private val _requireGameNameEvent = LiveEvent<Unit>()

    val gamesLiveData: LiveData<List<Game>> = _gamesLiveData
    val continueLevelEvent: LiveData<Pair<Long, Long>> = _continueLevelEvent
    val requireGameNameEvent: LiveData<Unit> = _requireGameNameEvent

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getGamesUseCase.getGames()
            .observeOn(Schedulers.computation())
            .map {  games ->
                games.sortedByDescending { it.lastModified }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                _gamesLiveData.postValue(it)
            })
            .addTo(container)
    }

    fun onItemClick(game: Game) {
        _continueLevelEvent.postValue(Pair(game.id, game.levelId))
    }

    fun onCreateGameClick() {
        _requireGameNameEvent.postValue(Unit)
    }

    fun onCreateGameWithName(name: String?) {
        if (name.isNullOrBlank()) return

        createGameUserCase.createGame(name)
            .subscribe(Consumer {
                _continueLevelEvent.postValue(it.id to it.levelId)
            })
            .addTo(container)
    }
}