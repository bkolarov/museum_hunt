package bg.tusofia.pmu.museumhunt.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<BindingT : ViewDataBinding, ViewModelT : BaseViewModel> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: ViewModelT

    protected val binding: BindingT? get() = view?.let { DataBindingUtil.findBinding(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection()
        super.onCreate(savedInstanceState)

        viewModel = instantiateViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(viewModel)
    }

    protected open fun performInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun instantiateViewModel(): ViewModelT

    protected fun binding(block: BindingT.(vm: ViewModelT) -> Unit) {
        binding?.block(viewModel)
    }

    protected fun Intent.start() {
        startActivity(this)
    }

    protected fun <T> LiveData<T>.observe(observer: Observer<T>) = observe(viewLifecycleOwner, observer)
}