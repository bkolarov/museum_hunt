package bg.tusofia.pmu.museumhunt.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import bg.tusofia.pmu.museumhunt.base.viewmodel.BaseViewModel
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<BindingT : ViewDataBinding, ViewModelT : BaseViewModel> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: ViewModelT

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection()
        super.onCreate(savedInstanceState)

        viewModel = instantiateViewModel()

        lifecycle.addObserver(viewModel)
    }

    abstract fun instantiateViewModel(): ViewModelT

    open fun performInjection() {
        AndroidInjection.inject(this)
    }

}