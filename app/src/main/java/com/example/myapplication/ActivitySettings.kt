package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_settings.*

class ActivitySettings : AppCompatActivity() {
    var gb_size_mode = 0
    var fm_mode = 0
    var left_mode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE) ?: return
        gb_size_mode = sharedPref.getInt("SIZE", 0)
        fm_mode = sharedPref.getInt("FIRSTMOVE", 0)
        left_mode = sharedPref.getInt("LEFT", 0)
        setContentView(R.layout.activity_settings)
        val bnv =  findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnv.selectedItemId = R.id.page_1
        bnv.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putInt("SIZE", gb_size_mode)
                        putInt("FIRSTMOVE", fm_mode)
                        putInt("LEFT", left_mode)
                        apply()
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_2 -> {
                    val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putInt("SIZE", gb_size_mode)
                        putInt("FIRSTMOVE", fm_mode)
                        putInt("LEFT", left_mode)
                        apply()
                    }
                    startActivity(Intent(applicationContext, ActivityMainMenu::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_3 -> {
                    val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putInt("SIZE", gb_size_mode)
                        putInt("FIRSTMOVE", fm_mode)
                        putInt("LEFT", left_mode)
                        apply()
                    }
                    startActivity(Intent(applicationContext, ActivityMatchHistory::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })

        updateFMModeIcon()
        updateLMModeIcon()
        updateGBSizeModeIcon()
    }
    fun rightArrowGBSizeOnClick(v : View) {
        if(gb_size_mode<2) {
            gb_size_mode++
        }
        else {
            gb_size_mode=0
        }
        updateGBSizeModeIcon()
    }
    fun leftArrowGBSizeOnClick(v : View) {
        if(gb_size_mode>0) {
            gb_size_mode--
        }
        else {
            gb_size_mode=2
        }
        updateGBSizeModeIcon()
    }
    fun rightArrowFMOnClick(v : View) {
        if(fm_mode<2) {
            fm_mode++
        }
        else {
            fm_mode=0
        }
        updateFMModeIcon()
    }
    fun leftArrowFMOnClick(v : View) {
        if(fm_mode>0) {
            fm_mode--
        }
        else {
            fm_mode=2
        }
        updateFMModeIcon()
    }
    fun rightArrowLMOnClick(v : View) {
        if(left_mode==1) {
            left_mode=0
        }
        else {
            left_mode=1
        }
        updateLMModeIcon()
    }
    fun leftArrowLMOnClick(v : View) {
        if(left_mode==1) {
            left_mode=0
        }
        else {
            left_mode=1
        }
        updateLMModeIcon()
    }
    fun updateGBSizeModeIcon() {
        Log.d("GBmode",""+gb_size_mode)
        if(gb_size_mode==0) {

            val text_resource = resources.getIdentifier("@drawable/text_8x8", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_gb_size_image_view.setImageDrawable(drawable_text_resource)
        }
        else if(gb_size_mode==1) {

            val text_resource = resources.getIdentifier("@drawable/text_10x10", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_gb_size_image_view.setImageDrawable(drawable_text_resource)
        }
        else if(gb_size_mode==2) {

            val text_resource = resources.getIdentifier("@drawable/text_12x12", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_gb_size_image_view.setImageDrawable(drawable_text_resource)
        }


    }
    fun updateLMModeIcon() {
        Log.d("GBmode",""+left_mode)
        if(left_mode==0) {

            val text_resource = resources.getIdentifier("@drawable/lf_off", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            lf_state.setImageDrawable(drawable_text_resource)
        }
        else if(left_mode==1) {

            val text_resource = resources.getIdentifier("@drawable/lf_on", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            lf_state.setImageDrawable(drawable_text_resource)
        }



    }
    fun updateFMModeIcon() {
        Log.d("FMmode",""+fm_mode)
        if(fm_mode==0) {

            val text_resource = resources.getIdentifier("@drawable/text_fm_rand", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_fm_image_view.setImageDrawable(drawable_text_resource)
        }
        else if(fm_mode==1) {

            val text_resource = resources.getIdentifier("@drawable/text_fm_pl1", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_fm_image_view.setImageDrawable(drawable_text_resource)
        }
        else if(fm_mode==2) {

            val text_resource = resources.getIdentifier("@drawable/text_fm_pl2", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource)


            text_fm_image_view.setImageDrawable(drawable_text_resource)
        }
    }

    override fun onBackPressed() {
        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("SIZE", gb_size_mode)
            putInt("FIRSTMOVE", fm_mode)
            putInt("LEFT", left_mode)
            apply()
        }
        super.onBackPressed()
    }
    fun goToSettings(item: MenuItem) {

        val intent = Intent(this, ActivitySettings::class.java)
        startActivity(intent)

    }
    fun goToMenu(item: MenuItem) {
        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("SIZE", gb_size_mode)
            putInt("FIRSTMOVE", fm_mode)
            putInt("LEFT", left_mode)
            apply()
        }
        val intent = Intent(this, ActivityMainMenu::class.java)
        startActivity(intent)
    }
    fun goToMatchHistory(item: MenuItem) {
        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("SIZE", gb_size_mode)
            putInt("FIRSTMOVE", fm_mode)
            putInt("LEFT", left_mode)
            apply()
        }
        val intent = Intent(this, ActivityMatchHistory::class.java)
        startActivity(intent)
    }
}
