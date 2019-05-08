package bg.tusofia.pmu.museumhunt.ingame.location

import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates
import bg.tusofia.pmu.museumhunt.domain.repository.StageLocation
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MapViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val levelDataUseCase: GetLevelDataUseCase
) : BaseViewModel(resourceManager) {

    private val _showDestinationEvent = LiveEvent<LocationCoordinates>()
    private val _showMyLocationEvent = LiveEvent<Boolean>()
    private val _requestLocationPermissionEvent = LiveEvent<Unit>()
    private val _openMaps = LiveEvent<Uri>()

    val showDestinationEvent: LiveData<LocationCoordinates> = _showDestinationEvent
    val showMyLocationEvent: LiveData<Boolean> = _showMyLocationEvent
    val requestLocationPermissionEvent: LiveData<Unit> = _requestLocationPermissionEvent
    val openMaps: LiveData<Uri> = _openMaps

    private var stageLocation: StageLocation? = null

    private val loccationPermissionGrantedSubject = PublishSubject.create<Boolean>()
    private val mapReadySubject = PublishSubject.create<Boolean>()
    private val dataLoadedSubject = PublishSubject.create<StageLocation>()

    private val loaderObservable =
        Observable.combineLatest(
            arrayOf(
                loccationPermissionGrantedSubject,
                mapReadySubject
            )
        ) { (permissionGrantedAny, mapReadyAny) ->
            val permissionGranted = permissionGrantedAny as Boolean
            val mapReady = mapReadyAny as Boolean
            val locationReady = permissionGranted && mapReady

            if (locationReady) {
                _showMyLocationEvent.postValue(locationReady)
            }

            locationReady
        }
            .withLatestFrom(
                dataLoadedSubject.cache(),
                BiFunction<Boolean, StageLocation, Pair<Boolean, StageLocation>> { locationReady, stageLocation ->
                    Pair(locationReady, stageLocation)
                })

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loaderObservable
            .subscribe { (locationReady, stageLocation) ->
                if (locationReady) {
                    _showDestinationEvent.postValue(stageLocation.locationCoordinates)
                }
            }
            .addTo(container)

        _requestLocationPermissionEvent.postValue(Unit)
    }

    fun initWithLevelId(levelId: Long) {
        getLevelProgressDataUseCase.getLevelProgressDataUseCase(levelId)
            .observeOn(Schedulers.computation())
            .flatMap {
                levelDataUseCase.getLevelData(it.number)
            }
            .map {
                it.stageLocation
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                stageLocation = it
                dataLoadedSubject.onNext(it)
            })
            .addTo(container)
    }

    fun onMapReady() {
        mapReadySubject.onNext(true)
    }

    fun onLocationPermissionGranted(granted: Boolean) {
        loccationPermissionGrantedSubject.onNext(granted)
    }

    fun onOpenInMapsClick() {
        stageLocation?.let {
            _openMaps.postValue(Uri.parse(it.locationCoordinates.url))
        }
    }

    fun onHelpClick() {

    }

    fun onToolbarNavigationClick() {

    }

}