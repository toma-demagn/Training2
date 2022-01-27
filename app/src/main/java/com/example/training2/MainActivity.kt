package com.example.training2

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.training2.ui.main.SectionsPagerAdapter
import com.example.training2.databinding.ActivityMainBinding
import com.example.training2.ui.main.ItemFragment
import com.example.training2.ui.main.MyItemRecyclerViewAdapter
import com.example.training2.ui.main.placeholder.PlaceholderContent
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.example.training2.LocationService.Companion.isServiceStarted
import com.example.training2.ui.main.ItemFragment.Companion.theAdapter
import com.example.training2.ui.main.ItemFragment.Companion.theList


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var sectionsPagerAdapter : SectionsPagerAdapter
    lateinit var fragmentMain : MainFragment
    lateinit var fragmentList : ItemFragment
    var isDark = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDark = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean("isDark", false)
        if (!isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        LOCATION_REFRESH_TIME = PreferenceManager.getDefaultSharedPreferences(applicationContext).getInt("locationRefreshTime", 500)
        fragmentMain = MainFragment()
        fragmentList = ItemFragment()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Toast.makeText(applicationContext, "NOW GOING THROUGH THIS CODE", Toast.LENGTH_SHORT).show()
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, fragmentMain, fragmentList)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        val view = findViewById<Button>(R.id.settings_button)
        view.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra("isDark", isDark)
            startActivity(intent)
        })
        val textView = findViewById<TextView>(R.id.title)
        textView.setOnClickListener(View.OnClickListener {
            //fragmentList.addItem()
            theList.add(0, Pair(2, "zero"))
            theAdapter.notifyDataSetChanged()
        })
        if (!checkPermission()) {
            requestPermission()
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter("AFFICHER"))

    }

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            handleChange(intent?.getStringExtra("location").toString())
        }

        private fun handleChange(extra: String) {
            theList.add(0, Pair(theList.size+1, extra))
            theAdapter.notifyDataSetChanged()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    fun toggleService() {
        if (!isServiceStarted){
            ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java).putExtra("refresh_time", LOCATION_REFRESH_TIME))
        } else {
            stopService(Intent(this, LocationService::class.java))
        }
    }

    companion object {
        var LOCATION_REFRESH_TIME = 500
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LocationService::class.java))
    }
}