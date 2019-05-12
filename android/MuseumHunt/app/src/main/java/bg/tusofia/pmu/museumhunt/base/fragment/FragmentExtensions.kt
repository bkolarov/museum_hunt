package bg.tusofia.pmu.museumhunt.base.fragment

import androidx.activity.OnBackPressedCallback

fun BaseFragment<*, *>.finishOnBackPressed() {

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }

    })

}