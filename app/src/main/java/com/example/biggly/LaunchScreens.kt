package com.example.biggly

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.relex.circleindicator.CircleIndicator3

class LaunchScreens : BaseActivity() {
    lateinit var next: Button
    lateinit var viewPager2 : ViewPager2;

    lateinit var indicatore : CircleIndicator3;
    lateinit var tabLayout : TabLayout;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        setUpViewPager();
       var  sharedPreferences = getSharedPreferences("sharedpreference", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor =sharedPreferences!!.edit()
        editor.putBoolean("IS_OPEN", true)
        editor.apply()
         next = findViewById(R.id.next)

        next.setOnClickListener {

            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            //Some implementation
        }.attach()
    }

    private fun setUpViewPager() {
        viewPager2 = findViewById(R.id.viewpager);
        indicatore = findViewById(R.id.indicator);
        tabLayout = findViewById(R.id.into_tab_layout);

        // Object of ViewPager2Adapter
        // this will passes the
        // context to the constructor
        // of ViewPager2Adapter
        var viewPager2Adapter = ViewPagerAdapter2(this);
        indicatore.setViewPager(viewPager2)

        // adding the adapter to viewPager2
        // to show the views in recyclerviewj
        viewPager2.setAdapter(viewPager2Adapter);

        // To get swipe event of viewpager2

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            // This method is triggered when there is any scrolling activity for the current page
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)


            }

            // triggered when you select a new page
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 5) {


                    next.setText(resources.getString(R.string.get_started))
//                    startActivity(Intent(this@LaunchScreens, MainActivity::class.java))
//
//                    finish()
                }else{
                    next.setText(resources.getString(R.string.skip))

                }




            }

            // triggered when there is
            // scroll state will be changed
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


    }


}