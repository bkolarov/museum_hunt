package bg.tusofia.pmu.museumhunt.location

import android.location.Location
import androidx.annotation.RequiresPermission
import arrow.core.Either
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

interface LocationService {

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun requestUpdates(
        locationRequest: LocationRequest
    ): Flowable<Either<LocationError, LocationData>>
}

class LocationServiceImpl(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationService {

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun requestUpdates(
        locationRequest: LocationRequest
    ): Flowable<Either<LocationError, LocationData>> {
        val resultSubject = PublishSubject.create<MHLocationResult>()

        val locationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                resultSubject.onNext(Either.right(LocationData(locationResult.lastLocation)))
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
                locationAvailability ?: return

                if (!locationAvailability.isLocationAvailable) {
                    resultSubject.onNext(Either.left(LocationAvailabilityError(locationAvailability)))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        return resultSubject
            .doOnDispose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
            .onErrorReturn { Either.left(UnknownError(it)) }
            .toFlowable(BackpressureStrategy.DROP)
    }
}

typealias MHLocationResult = Either<LocationError, LocationData>

sealed class LocationError

class SettingsFailedError(val cause: ApiException) : LocationError()

class LocationAvailabilityError(val locationAvailability: LocationAvailability) : LocationError()

class UnknownError(val cause: Throwable?) : LocationError()

object TaskCancelledError : LocationError()

class LocationData(val location: Location)