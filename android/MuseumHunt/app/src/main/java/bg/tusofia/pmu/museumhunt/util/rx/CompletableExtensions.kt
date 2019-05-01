package bg.tusofia.pmu.museumhunt.util.rx

import io.reactivex.Completable

fun Completable.toSingle() =
        toSingleDefault(true)
            .onErrorReturnItem(false)
            .map {
                if (it.not()) throw CompletableFailException()
                it
            }

class CompletableFailException : Exception()