package com.example.eventslogger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        var adapterWalkthrough = Walkthrough_Adapter(supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        viewpager.adapter  = adapterWalkthrough
        indicator.setViewPager(viewpager)

        adapterWalkthrough.registerDataSetObserver(indicator.dataSetObserver)

        button_getstarted.setOnClickListener {
            val intent : Intent = Intent(this,MainActivity::class.java);
            startActivity(intent)
            finish()

        }
    }
}