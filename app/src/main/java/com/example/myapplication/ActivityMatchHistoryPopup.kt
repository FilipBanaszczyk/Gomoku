package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_match_history_popup.*


class ActivityMatchHistoryPopup  : AppCompatActivity(), GameBoardView.PositionClickedListener {

    lateinit var gameboard : Array<IntArray>
    var state_index =-1
    lateinit var  states:ArrayList<Array<IntArray>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_history_popup)
        if(this.resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.finish()

        }

        states = intent.extras?.get("board") as ArrayList<Array<IntArray>>


        state_index = states.size-1
        showGame(states[state_index])
        //popupGameBoard.background= ActivityCompat.getDrawable(this, resID)
        //popupGameBoard.setBackgroundColor(Color.rgb(33, 33, 51))

    }
    fun showGame(gameboard : Array<IntArray>){

        popupGameBoard.positionClickedListener=this
        popupGameBoard.drawFill=true
        popupGameBoard.resetGame()
        popupGameBoard.gameBoardArray = gameboard
        popupGameBoard.invalidate()

    }

    override fun onResume() {
        super.onResume()
        val match_details = findViewById<LinearLayout>(R.id.match_details)
        val tv_winner = findViewById<TextView>(R.id.tv_winner)
        val tv_date = findViewById<TextView>(R.id.tv_date)
        val tv_mode = findViewById<TextView>(R.id.tv_mode)
        val winner:String = intent.extras?.getString("winner","")!!
        val date:String = intent.extras?.getString("date","")!!
        val mode:String = intent.extras?.getString("mode","")!!
        tv_winner.text = winner
        tv_date.text = date
        tv_mode.text = mode
        if(winner.equals("AI")){
            val resID = this.resources.getIdentifier("lost_match", "drawable", this.packageName)
            match_details.background= ActivityCompat.getDrawable(this, resID)
        }
        else{
            val resID = this.resources.getIdentifier("match_details", "drawable", this.packageName)
            match_details.background= ActivityCompat.getDrawable(this, resID)
        }
        val prev = findViewById<ImageView>(R.id.prev)
        prev.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                toStart()
                return true
            }
        })
        val next = findViewById<ImageView>(R.id.next)
        next.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                toEnd()
                return true
            }
        })
    }
    fun prevState(view: View){
        //Log.d("arrow","prev $state_index  ")
        if(state_index>0 ){
            state_index--
            showGame(states[state_index])
        }

    }
    fun toStart(){
        state_index = 0
        //Log.d("arrow","start $state_index ")
        showGame(states[state_index])

    }
    fun toEnd(){
        state_index = states.size-1
        showGame(states[state_index])

    }
    fun nextState(view: View){
        //Log.d("arrow","next  $state_index  ")
        if(state_index<states.size-1 && state_index!=-1 ){
            state_index++
            showGame(states[state_index])
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        goBack()
        return true
    }

    override fun onPositionClicked(x: Int, y: Int) {
        goBack()
    }
    fun goBack(){
        val intent = Intent(this, ActivityMatchHistory::class.java)
        intent.removeExtra("chosen_id")
        Log.d("end popup", intent.extras?.getInt("chosen_id").toString())
        startActivity(intent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        goBack()
    }


}
