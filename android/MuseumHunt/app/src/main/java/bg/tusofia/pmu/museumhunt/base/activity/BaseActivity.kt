package bg.tusofia.pmu.museumhunt.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<BindingT : ViewDataBinding, ViewModelT : ViewModel> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory

    protected lateinit var viewModel: ViewModelT

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection()
        super.onCreate(savedInstanceState)

        viewModel = instantiateViewModel()
    }

    abstract fun instantiateViewModel(): ViewModelT

    open fun performInjection() {
        AndroidInjection.inject(this)
    }

}