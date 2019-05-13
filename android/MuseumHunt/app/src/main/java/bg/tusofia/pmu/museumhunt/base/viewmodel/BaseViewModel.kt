package bg.tusofia.pmu.museumhunt.base.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bg.tusofia.pmu.museumhunt.base.dialog.DialogValues
import bg.tusofia.pmu.museumhunt.base.resources.ResourceManager
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(resourceManager: ResourceManager)
    : ViewModel(),
    LifecycleObserver,
    Observable,
    ResourceManager by resourceManager {

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    protected val container = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        container.clear()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks?.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.notifyCallbacks(this, fieldId, null)
    }

    protected fun DialogValues.postOn(liveData: MutableLiveData<DialogValues>) = liveData.postValue(this)

}