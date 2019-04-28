package bg.tusofia.pmu.museumhunt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.databinding.ActivityMainBinding
import bg.tusofia.pmu.museumhunt.di.viewmodel.factory.ViewModelProviderFactory
import com.tusofia.pmu.bgquest.UnityPlayerActivity
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val intent = Intent(this, UnityPlayerActivity::class.java).apply {
            putExtra("level-data", "{ \"HintWords\": [\"Pesho\", \"Kircho\", \"Mariika\", \"Tisho\", \"Cecka\"] }")
        }

        startActivityForResult(intent, 1)


    }

    override fun instantiateViewModel(): MainActivityViewModel = ViewModelProviders.of(this, viewModeFactory)[MainActivityViewModel::class.java]

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("UNITY_RESULT") ?: ""
            Timber.d(result)
        }
    }
}
