package bg.tusofia.pmu.museumhunt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.databinding.ActivityMainBinding
import com.tusofia.pmu.bgquest.UnityPlayerActivity
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.viewModel = viewModel

        viewModel.newGameLiveEvent.observe(this, Observer {
            val intent = Intent(this, UnityPlayerActivity::class.java).apply {
                putExtra("levelProgress-data", "{ \"HintWords\": [\"Pesho\", \"Kircho\", \"Mariika\", \"Tisho\", \"Cecka\"] }")
            }

            Timber.d("new  game with name $it")
            startActivityForResult(intent, 1)
        })

        viewModel.continueGameLiveEvent.observe(this, Observer {

        })

        viewModel.browseGamesLiveEvent.observe(this, Observer {

        })
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
