package bg.tusofia.pmu.museumhunt.util.arrow

import arrow.core.Either
import bg.tusofia.pmu.museumhunt.location.LocationError
import bg.tusofia.pmu.museumhunt.location.TaskCancelledError
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

inline fun <L, R> Observable<Either<L, R>>.onSuccess(crossinline onSuccess: (R) -> Unit): Observable<Either<L, R>> =
    map {
        mapRight(it, onSuccess)
    }

inline fun <L, R> Observable<Either<L, R>>.onError(crossinline onError: (L) -> Unit) =
    map {
        mapLeft(it, onError)
    }

inline fun <L, R> Flowable<Either<L, R>>.onUpdate(crossinline onSuccess: (R) -> Unit) =
    map {
        mapRight(it, onSuccess)
    }

inline fun <L, R> Flowable<Either<L, R>>.onError(crossinline onError: (L) -> Unit) =
    map {
        mapLeft(it, onError)
    }

inline fun <L, R> Single<Either<L, R>>.onSuccess(crossinline onSuccess: (R) -> Unit) =
    map {
        mapRight(it, onSuccess)
    }

inline fun <L, R> Single<Either<L, R>>.onError(crossinline onError: (L) -> Unit) =
    map {
        mapLeft(it, onError)
    }

inline fun <L, R> Maybe<Either<L, R>>.onSuccess(crossinline onSuccess: (R) -> Unit) =
    map {
        mapRight(it, onSuccess)
    }

inline fun <L, R> Maybe<Either<L, R>>.onError(crossinline onError: (L) -> Unit) =
    map {
        mapLeft(it, onError)
    }

inline fun <R> Maybe<Either<LocationError, R>>.onLocationError(crossinline onError: (LocationError) -> Unit) =
    map { either ->
        mapLeft(either) { error ->
            if (error !is TaskCancelledError) onError(error)
        }
    }

inline fun <R> Maybe<Either<LocationError, R>>.onCancel(crossinline onCancel: () -> Unit) = onError {
    if (it is TaskCancelledError) onCancel()
}

inline fun <L, R> mapLeft(it: Either<L, R>, onError: (L) -> Unit): Either<L, R> {
    if (it is Either.Left<L>) {
        onError(it.a)
    }
    return it
}

inline fun <L, R> mapRight(it: Either<L, R>, onSuccess: (R) -> Unit): Either<L, R> {
    if (it is Either.Right<R>) {
        onSuccess(it.b)
    }
    return it
}