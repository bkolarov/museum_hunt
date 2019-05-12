package bg.tusofia.pmu.museumhunt.location

import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import arrow.core.Either
import bg.tusofia.pmu.museumhunt.domain.repository.LocationCoordinates
import bg.tusofia.pmu.museumhunt.domain.repository.StageLocation
import bg.tusofia.pmu.museumhunt.util.arrow.onUpdate
import bg.tusofia.pmu.museumhunt.util.minDistanceToDestination
import com.google.android.gms.location.LocationRequest
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class TrackDestinationReachedUseCase @Inject constructor(private val locationService: LocationService) {

    private val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startTracking(
        destinationLocation: StageLocation,
        locationUpdateListener: MutableLiveData<Location>
    ): Single<Either<LocationError, LocationData>> {
        return locationService.requestUpdates(locationRequest)
            .observeOn(Schedulers.computation())
            .onUpdate {
                checkDestinationReached(it, destinationLocation.locationCoordinates)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onUpdate {
                Timber.d("location updated: ${it.location.latitude} | ${it.location.longitude}")
                locationUpdateListener.postValue(it.location)
            }
            .takeUntil { either ->
                when (either) {
                    is Either.Left<LocationError> -> false
                    is Either.Right<LocationData> -> checkDestinationReached(
                        either.b,
                        destinationLocation.locationCoordinates
                    )
                }
            }
            .onErrorReturn { Either.left(UnknownError(it)) }
            .lastOrError()
            .doOnSubscribe {
                Timber.d("Location updates started")
            }
    }

    private fun checkDestinationReached(
        currentLocation: LocationData,
        locationCoordinates: LocationCoordinates
    ): Boolean {
        return locationCoordinates.let {
            val results = floatArrayOf(0f)

            Location.distanceBetween(
                currentLocation.location.latitude,
                currentLocation.location.longitude,
                it.latitude,
                it.longitude,
                results
            )

            val distance = results[0]
            Timber.d("Distance to destination: $distance")
            distance <= minDistanceToDestination
        }
    }
}