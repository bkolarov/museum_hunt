package bg.tusofia.pmu.museumhunt.util.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(container: CompositeDisposable) = container.add(this)