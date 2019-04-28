package bg.tusofia.pmu.museumhunt.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<BindingT : ViewDataBinding, ViewModelT : ViewModel> : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        performInjection()
        super.onCreate(savedInstanceState)
    }

    open fun performInjection() {
        AndroidInjection.inject(this)
    }

}