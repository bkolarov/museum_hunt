package bg.tusofia.pmu.museumhunt.ingame.unity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bg.tusofia.pmu.museumhunt.base.fragment.BaseFragment
import com.tusofia.pmu.bgquest.UnityPlayerActivity
import timber.log.Timber

class UnityLauncherFragment : BaseFragment<ViewDataBinding, UnityLauncherViewModel>() {

    companion object {
        private const val requestCodeUnityPlayer = 1
    }

    private val input: UnityLauncherFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.launchUnityModuleLiveEvent.observe(viewLifecycleOwner, Observer { unityData ->
            val intent = Intent(context, UnityPlayerActivity::class.java).apply {
                putExtra(UnityPlayerActivity.KEY_LEVEL_DATA, unityData)
            }

            Timber.d("start unity with data: $unityData")
            startActivityForResult(intent, requestCodeUnityPlayer)
        })

        viewModel.goBackEvent.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.openRiddleScreenEvent.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(UnityLauncherFragmentDirections.actionUnityLauncherFragmentToRiddleFragment(it.levelId))
        })

        viewModel.initForLevel(input.levelId)
    }

    override fun instantiateViewModel(): UnityLauncherViewModel =
        ViewModelProviders.of(this, viewModelFactory)[UnityLauncherViewModel::class.java]

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestCodeUnityPlayer) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val result = data?.getStringExtra(UnityPlayerActivity.KEY_UNITY_RESULT) ?: ""
                    Timber.d(result)

                    viewModel.onObstaclesPassed(result)
                }
                else -> {
                    viewModel.onObstacleExit()
                }
            }
        }
    }

}