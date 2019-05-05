package bg.tusofia.pmu.museumhunt.base.fragment

import androidx.activity.OnBackPressedCallback

fun BaseFragment<*, *>.finishOnBackPressed() {

    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, OnBackPressedCallback {
        activity?.finish()
        true
    })

}