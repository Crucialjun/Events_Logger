package com.example.eventslogger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val preference : SharedPreferences = getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val firstRun : Boolean = preference.getBoolean("firstRun",true)

        if (!firstRun){
            startActivity(Intent(this,SignUpSignInActivity::class.java))
            finish()
        }

        val adapterWalkthrough = WalkthroughAdapter(supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        viewpager.adapter  = adapterWalkthrough
        indicator.setViewPager(viewpager)

        adapterWalkthrough.registerDataSetObserver(indicator.dataSetObserver)

        button_getstarted.setOnClickListener {
            val intent : Intent = Intent(this,SignUpSignInActivity::class.java);
            val editor : SharedPreferences.Editor = preference.edit()
            editor.putBoolean("firstRun",false)
            editor.apply()
            startActivity(intent)
            finish()

        }
    }


}