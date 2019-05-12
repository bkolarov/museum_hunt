package bg.tusofia.pmu.museumhunt.location

import arrow.core.Either
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.lang.Exception
import javax.inject.Inject

class CheckLocationSettingsUseCase @Inject constructor(private val locationSettingsClient: SettingsClient) {

    fun checkSettings(locationSettingsRequest: LocationSettingsRequest): Maybe<Either<LocationError, LocationSettingsResponse?>> {
        val publishSubject = PublishSubject.create<Either<LocationError, LocationSettingsResponse?>>()
        val settingsTask = locationSettingsClient.checkLocationSettings(locationSettingsRequest)

        settingsTask.addOnCompleteListener {
            try {
                publishSubject.onNext(Either.right(it.result))
            } catch (exception: Exception) {
                val cause = it.exception
                if (cause is ApiException) {
                    publishSubject.onNext(Either.left(SettingsFailedError(cause)))
                } else {
                    publishSubject.onNext(Either.left(UnknownError(cause)))
                }
            } finally {
                publishSubject.onComplete()
            }
        }

        settingsTask.addOnCanceledListener {
            publishSubject.onNext(Either.left(TaskCancelledError))
            publishSubject.onComplete()
        }

        settingsTask.addOnFailureListener {
            val error = if (it is ApiException) {
                SettingsFailedError(it)
            } else {
                UnknownError(it)
            }

            publishSubject.onNext(Either.left(error))
            publishSubject.onComplete()
        }

        return publishSubject.firstElement()
    }

}