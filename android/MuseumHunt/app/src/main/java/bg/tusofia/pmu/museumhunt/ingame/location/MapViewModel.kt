package bg.tusofia.pmu.museumhunt.ingame.location

import android.annotation.SuppressLint
import android.location.Location
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.dialog.DialogValues
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates
import bg.tusofia.pmu.museumhunt.domain.repository.StageLocation
import bg.tusofia.pmu.museumhunt.domain.usecases.game.GetLevelProgressDataUseCase
import bg.tusofia.pmu.museumhunt.domain.usecases.level.GetLevelDataUseCase
import bg.tusofia.pmu.museumhunt.location.*
import bg.tusofia.pmu.museumhunt.util.arrow.onCancel
import bg.tusofia.pmu.museumhunt.util.arrow.onError
import bg.tusofia.pmu.museumhunt.util.arrow.onLocationError
import bg.tusofia.pmu.museumhunt.util.arrow.onSuccess
import bg.tusofia.pmu.museumhunt.util.arrow.onUpdate
import bg.tusofia.pmu.museumhunt.util.location.toLocation
import bg.tusofia.pmu.museumhunt.util.rx.addTo
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.hadilq.liveevent.LiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class MapViewModel @Inject constructor(
    resourceManager: ResourceManager,
    private val getLevelProgressDataUseCase: GetLevelProgressDataUseCase,
    private val levelDataUseCase: GetLevelDataUseCase,
    private val locationService: LocationService,
    private val checkLocationSettingsUseCase: CheckLocationSettingsUseCase,
    private val trackDestinationReachedUseCase: TrackDestinationReachedUseCase
) : BaseViewModel(resourceManager) {

    // Begin region Events
    private val _goBackEvent = LiveEvent<Unit>()
    private val _showDialog = LiveEvent<DialogValues>()
    private val _showDestinationEvent = LiveEvent<LocationCoordinates>()
    private val _showMyLocationEvent = LiveEvent<Boolean>()
    private val _requestLocationPermissionEvent = LiveEvent<Unit>()
    private val _openMaps = LiveEvent<Uri>()
    private val _resolveLocationSettingsEvent = LiveEvent<ResolvableApiException>()
    private val _currentLocationEvent = MutableLiveData<Location>()

    val goBackEvent: LiveData<Unit> = _goBackEvent
    val showDialog: LiveData<DialogValues> = _showDialog
    val showDestinationEvent: LiveData<LocationCoordinates> = _showDestinationEvent
    val showMyLocationEvent: LiveData<Boolean> = _showMyLocationEvent
    val requestLocationPermissionEvent: LiveData<Unit> = _requestLocationPermissionEvent
    val openMaps: LiveData<Uri> = _openMaps
    val resolveLocationSettingsEvent: LiveData<ResolvableApiException> = _resolveLocationSettingsEvent
    val currentLocationEvent: LiveData<Location> = _currentLocationEvent

    // End region Events

    // Begin region data

    private var destinationLocation: StageLocation? = null

    // End region data

    // Begin region Load
    private val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationSettingsRequest = LocationSettingsRequest.Builder().apply {
        addLocationRequest(locationRequest)
    }.build()

    private var shouldCheckLocationSettingsOnResume = true

    // Begin region inputs
    private val locationPermissionGrantedSubject = PublishSubject.create<Boolean>()
    private val locationSettingsSubject = PublishSubject.create<Boolean>()
    private val mapReadySubject = PublishSubject.create<Boolean>()
    private val dataLoadedSubject = PublishSubject.create<StageLocation>()

    // End region inputs

    private var locationSettingsDisposable: Disposable? = null
    private var locationUpdatesDisposable: Disposable? = null

    private val subjectsList =
        listOf(locationPermissionGrantedSubject, locationSettingsSubject, mapReadySubject, dataLoadedSubject)

    private val loaderObservable =
        Observable.combineLatest(subjectsList.toTypedArray()) {
            val permissionGranted = it[locationPermissionGrantedSubject] as Boolean
            val locationSettingsEnabled = it[locationSettingsSubject] as Boolean
            val mapReady = it[mapReadySubject] as Boolean
            val destinationLocation = it[dataLoadedSubject] as StageLocation

            val locationReady = permissionGranted && locationSettingsEnabled && mapReady

            if (locationReady) {
                this.destinationLocation = destinationLocation
                _showDestinationEvent.postValue(destinationLocation.locationCoordinates)
            }

            _showMyLocationEvent.postValue(locationReady)

            Pair(locationReady, destinationLocation)
        }

    // End region Load

    private operator fun Array<out Any>.get(subject: PublishSubject<out Any>): Any = this[subjectsList.indexOf(subject)]

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        loaderObservable
            .subscribe { (locationReady, _) ->
                if (locationReady) {
                    startLocationUpdates()
                } else {
                    stopLocationUpdates()
                }
            }
            .addTo(container)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (shouldCheckLocationSettingsOnResume) {
            checkLocationSettings()
        }
        _requestLocationPermissionEvent.postValue(Unit)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopLocationUpdates()
    }

    private fun checkLocationSettings() {
        if (locationSettingsDisposable?.isDisposed == false) return

        shouldCheckLocationSettingsOnResume = false

        locationSettingsDisposable = checkLocationSettingsUseCase.checkSettings(locationSettingsRequest)
            .onSuccess {
                locationSettingsSubject.onNext(true)
            }
            .onLocationError {
                locationSettingsSubject.onNext(false)
                handleLocationSettingsError(it)
            }
            .onCancel {
                locationSettingsSubject.onNext(false)
            }
            .subscribe()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Timber.d("Starting location updates")
//        locationUpdatesDisposable = locationUpdatesDisposable ?: locationService.requestUpdates(locationRequest)
//            .observeOn(AndroidSchedulers.mainThread())
//            .onUpdate {
//                Timber.d("location updated: ${it.location.latitude} | ${it.location.longitude}")
//                checkDestinationReached(it)
//                _currentLocationEvent.postValue(it.location)
//            }
//            .onError {
//                when (it) {
//                    is LocationAvailabilityError,
//                    is SettingsFailedError -> checkLocationSettings()
//                }
//            }
//            .doOnSubscribe {
//                Timber.d("Location updates started")
//            }
//            .subscribe()

        if (locationUpdatesDisposable?.isDisposed == false) return

        locationUpdatesDisposable = destinationLocation?.let { destinationLocation ->
            trackDestinationReachedUseCase.startTracking(destinationLocation, _currentLocationEvent)
                .observeOn(AndroidSchedulers.mainThread())
                .onSuccess {
                    Timber.d("Destination reached")
                }
                .onError { error ->
                    when (error) {
                        is LocationAvailabilityError,
                        is SettingsFailedError -> checkLocationSettings()
                    }
                }
                .subscribe()
        }
    }

    private fun stopLocationUpdates() {
        Timber.d("Stop location updates")
        locationUpdatesDisposable?.dispose()
        locationUpdatesDisposable = null
    }

    private fun handleLocationSettingsError(it: LocationError) {
        when (it) {
            is SettingsFailedError -> {
                when (it.cause.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> promptEnableLocationDialog {
                        _resolveLocationSettingsEvent.postValue(it.cause as ResolvableApiException)
                    }
                }
            }
        }
    }

    private fun promptEnableLocationDialog(proceed: () -> Unit) {
        DialogValues().apply {
            message = getString(R.string.map_dialog_location_settings_change_needed_message)
            positiveBtnTxt = getString(R.string.ok)
            positiveBtnCallback = proceed
            negativeBtnText = getString(R.string.cancel)
            modal = true
        }.postOn(_showDialog)
    }

    fun onMapReady() {
        mapReadySubject.onNext(true)
    }

    fun onLocationPermissionGranted(granted: Boolean) {
        locationPermissionGrantedSubject.onNext(granted)
    }

    fun onOpenInMapsClick() {
        destinationLocation?.let {
            _openMaps.postValue(Uri.parse(it.locationCoordinates.url))
        }
    }

    fun onLocationSettingsChanged(states: LocationSettingsStates) {
        if (states.isLocationPresent && states.isLocationUsable) {
            locationSettingsSubject.onNext(true)
        } else {
            checkLocationSettings()
        }
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
                destinationLocation = it
                dataLoadedSubject.onNext(it)
            })
            .addTo(container)
    }

    fun onHelpClick() {
        DialogValues().apply {
            message = getString(R.string.map_help)
            positiveBtnTxt = getString(R.string.ok)
        }.postOn(_showDialog)
    }

    fun onToolbarNavigationClick() {

    }

}