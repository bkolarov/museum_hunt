package bg.tusofia.pmu.museumhunt.ingame

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import bg.tusofia.pmu.museumhunt.R
import bg.tusofia.pmu.museumhunt.base.activity.BaseActivity
import bg.tusofia.pmu.museumhunt.databinding.ActivityIngameBinding


class IngameActivity : BaseActivity<ActivityIngameBinding, IngameViewModel>() {

    companion object {
        const val KEY_HOME_INPUT = "homeInput"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityIngameBinding>(this, R.layout.activity_ingame)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.setGraph(R.navigation.ingame_navigation, Bundle().apply {
            putParcelable(KEY_HOME_INPUT, intent.getParcelableExtra(KEY_HOME_INPUT))
        })

        binding.root
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun instantiateViewModel(): IngameViewModel =
        ViewModelProviders.of(this, viewModeFactory)[IngameViewModel::class.java]

}