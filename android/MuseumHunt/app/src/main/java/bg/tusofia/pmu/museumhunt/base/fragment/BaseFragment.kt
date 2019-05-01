package bg.tusofia.pmu.museumhunt.base.fragment

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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

        lifecycle.addObserver(viewModel)
    }

    protected open fun performInjection() {
        AndroidSupportInjection.inject(this)
    }

    abstract fun instantiateViewModel(): ViewModelT

    protected fun binding(block: BindingT.(vm: ViewModelT) -> Unit) {
        binding?.block(viewModel)
    }
}