package com.dicoding.myaqidahmobile.ui.onboarding

import androidx.fragment.app.*
import androidx.viewpager2.adapter.*
import com.dicoding.myaqidahmobile.ui.onboarding.fragment.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2  // Jumlah slide onboarding
    }

    override fun createFragment(position: Int): Fragment {
        // Return fragment sesuai posisi
        return when (position) {
            0 -> OnboardingFragment1()
            else -> OnboardingFragment2()
        }
    }
}
