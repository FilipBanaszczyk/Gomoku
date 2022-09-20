package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_menu.*
import java.io.File
import java.io.FileOutputStream


class ActivityMainMenu : AppCompatActivity() {
    var mode=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)



        Log.d("ActiveMainmenu", "create")
        val bnv =  findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnv.selectedItemId = R.id.page_2
        bnv.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    startActivity(Intent(applicationContext, ActivitySettings::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_2 -> {


                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_3 -> {
                    startActivity(Intent(applicationContext, ActivityMatchHistory::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })


    }
    fun updateModeIcon() {

        if(mode==0) {
            val image_resource = resources.getIdentifier("@drawable/icon_pvp", null, packageName)
            val drawable_icon_resource = getResources().getDrawable(image_resource);
            val text_resource = resources.getIdentifier("@drawable/text_pvp", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource);

            icon_mode_image_view.setImageDrawable(drawable_icon_resource);
            text_mode_image_view.setImageDrawable(drawable_text_resource);
        }
        else if(mode==1) {
            val image_resource = resources.getIdentifier("@drawable/icon_pveb", null, packageName)
            val drawable_icon_resource = getResources().getDrawable(image_resource);
            val text_resource = resources.getIdentifier("@drawable/text_pveb", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource);

            icon_mode_image_view.setImageDrawable(drawable_icon_resource);
            text_mode_image_view.setImageDrawable(drawable_text_resource);
        }
        else if(mode==2) {
            val image_resource = resources.getIdentifier("@drawable/icon_pvmb", null, packageName)
            val drawable_icon_resource = getResources().getDrawable(image_resource);
            val text_resource = resources.getIdentifier("@drawable/text_pvmb", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource);

            icon_mode_image_view.setImageDrawable(drawable_icon_resource);
            text_mode_image_view.setImageDrawable(drawable_text_resource);
        }
        else if(mode==3) {
            val image_resource = resources.getIdentifier("@drawable/icon_pvhb", null, packageName)
            val drawable_icon_resource = getResources().getDrawable(image_resource);
            val text_resource = resources.getIdentifier("@drawable/text_pvhb", null, packageName)
            val drawable_text_resource = getResources().getDrawable(text_resource);

            icon_mode_image_view.setImageDrawable(drawable_icon_resource);
            text_mode_image_view.setImageDrawable(drawable_text_resource);
        }

    }

    fun rightArrowOnClick(v: View) {
        if(mode<3) {
            mode++;
        }
        else {
            mode=0;
        }
        val intent = Intent(this, ActivityGame::class.java)

        updateModeIcon();
    }

    fun leftArrowOnClick(v: View) {
        if(mode>0) {
            mode--;
        }
        else {
            mode=3;
        }
        val intent = Intent(this, ActivityGame::class.java)
        updateModeIcon();
    }


    fun goToGame(view: View?) {
        val intent = Intent(this, ActivityGame::class.java)
        intent.putExtra("GAMEMODE", mode)
        startActivity(intent)
    }

    fun goToSettings(item: MenuItem) {

        val intent = Intent(this, ActivitySettings::class.java)

        startActivity(intent)
    }
    fun goToMenu(item: MenuItem) {
        val intent = Intent(this, ActivityMainMenu::class.java)

        startActivity(intent)
    }
    fun goToMatchHistory(item: MenuItem) {
        val intent = Intent(this, ActivityMatchHistory::class.java)
        startActivity(intent)
    }
}
