package com.example.eventslogger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Walkthrough_Adapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {



    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return OnboardingOne()
            1 -> return OnboardingTwo()
            2 -> return OnboardingThree()
        }
        return OnboardingOne()
    }

    override fun getCount(): Int {
        return 3
    }
}