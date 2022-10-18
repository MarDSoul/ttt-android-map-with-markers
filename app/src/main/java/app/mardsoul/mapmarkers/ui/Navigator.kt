package app.mardsoul.mapmarkers.ui

import androidx.fragment.app.Fragment

interface Navigator {
    fun navigateToFragment(fragment: Fragment)
    fun goBack()
}