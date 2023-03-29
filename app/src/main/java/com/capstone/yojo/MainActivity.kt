package com.capstone.yojo


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstone.yojo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import java.util.*


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter :FragmentPageAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generateUID(this)
        val uid = generateUID(this)
        Log.e("UID == ", uid)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabs
        adapter = FragmentPageAdapter(supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_img))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.hearts_img))

        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }
    // 어플 초기 구동시 UID 생성, 재실행시 다시 값 생성 안되게함
    fun generateUID(context: Context): String {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        var uid = prefs.getString("uid", null)
        if (uid == null) {
            uid = UUID.randomUUID().toString()
            saveUidToSharedPrefs(context, uid)
        }
        return uid
    }

    // UID SharedPreferences 에 저장시켜 어플 종료되도 유지되게
    fun saveUidToSharedPrefs(context: Context, uid: String) {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("uid", uid)
        editor.apply()
    }

    // SharedPreferences 에 저장된 UID 값 가져옴
    private fun getUidFromSharedPrefs(context: Context): String? {
        val prefs = context.getSharedPreferences("MyUID", Context.MODE_PRIVATE)
        return prefs.getString("uid", null)
    }

}