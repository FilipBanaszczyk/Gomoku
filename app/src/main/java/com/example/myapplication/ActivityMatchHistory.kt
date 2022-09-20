package com.example.myapplication

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_match_history.*
import kotlinx.android.synthetic.main.activity_match_history.view.*
import java.text.SimpleDateFormat


class ActivityMatchHistory : AppCompatActivity(),AdapterMatchHistory.OnClickListener, GameBoardView.PositionClickedListener {

    var matches=ArrayList<GameLog>()
    lateinit var adapter: AdapterMatchHistory
    var state_index: Int = -1
    var position = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_history)
        val matches_rv =  matches_recycler as RecyclerView
        val bnv =  findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bnv.selectedItemId = R.id.page_3
        bnv.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    startActivity(Intent(applicationContext, ActivitySettings::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_2 -> {
                    startActivity(Intent(applicationContext, ActivityMainMenu::class.java))
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.page_3 -> {

                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
        no_match_text.visibility= View.GONE


        val loader=GamesHistory()
        try{
            matches=loader.load(this)
        }
        catch (e: Exception){
            no_match_text.visibility= View.VISIBLE
        }
        if(matches.size==0){
            no_match_text.visibility= View.VISIBLE
            matches_rv.visibility = View.INVISIBLE
        }

//        val arr:Array<IntArray> = arrayOf(
//            intArrayOf(0,0,0,0,0,0,0,0),
//            intArrayOf(0,0,0,2,0,0,0,0),
//            intArrayOf(0,0,0,2,0,0,0,0),
//            intArrayOf(0,0,0,2,1,0,0,0),
//            intArrayOf(0,0,0,2,0,0,0,0),
//            intArrayOf(0,0,0,2,0,0,0,0),
//            intArrayOf(0,0,0,0,0,0,0,0),
//            intArrayOf(0,0,0,0,0,0,0,0)
//        )
//        matches.add(GameLog("today","hard","you",arr))

        matches.sortByDescending { SimpleDateFormat("yyyy/MM/dd/hh/mm/ss").parse(it.date) }
        adapter =  AdapterMatchHistory(matches, this)

        adapter.setOnClickListener(this)
        matches_rv.adapter = adapter
        matches_rv.layoutManager= LinearLayoutManager(this)

    }
    override fun onResume() {
        super.onResume()

        matches_recycler.visibility= View.VISIBLE
        bottomNavigationView.visibility= View.VISIBLE
        if(this.resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
        val old_index: Int? = intent.extras?.getInt("chosen_id")

        if(old_index==null){
           // Log.d("chosen id", old_index.toString())
        }
        else{

                position = old_index
                state_index = matches[position].states.size-1
                adapter.choose(position)
                showGame(matches[position].states[matches[position].states.size-1])


        }


        }

    override fun onItemClick(index: Int) {
        state_index = matches[index].states.size-1
        position = index
        adapter.choose(index)

        intent.putExtra("chosen_id", position)
        //Log.d("chosen id after put", intent.extras?.getInt("chosen_id").toString())
        val gameBoardArray = matches[index].states[matches[index].states.size-1]

        showGame(gameBoardArray)



    }
    fun showGame(gameBoardArray: Array<IntArray>){
        if(this.resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            matches_recycler.visibility= View.INVISIBLE
            bottomNavigationView.visibility= View.GONE
            val popup = ActivityMatchHistoryPopup()


            val intent = Intent(this, popup::class.java)
            intent.putExtra("board", matches[position].states)
            intent.putExtra("winner", matches[position].winner)
            intent.putExtra("date", SimpleDateFormat("yyyy/MM/dd").format(SimpleDateFormat("yyyy/MM/dd/hh/mm/ss").parse(matches[position].date)))
            intent.putExtra("mode", matches[position].diffLevel)

            startActivity(intent)
        }
        else if(this.resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            val gameBoard = findViewById<GameBoardView>(R.id.gameBoardView2)
            gameBoard.visibility = View.VISIBLE
            gameBoard.positionClickedListener =  this

            gameBoard.drawFill=true
            gameBoard.resetGame()
            gameBoard.gameBoardArray = gameBoardArray
            gameBoard.invalidate()
        }
    }
    fun prevState(view: View){
        Log.d("arrow","prev $state_index  $position")
        if(state_index>0 && position>=0){
            state_index--
            showGame(matches[position].states[state_index])
        }

    }
    fun toStart(){
        state_index = 0
        //Log.d("arrow","start $state_index  $position")
        showGame(matches[position].states[state_index])

    }
    fun toEnd(){
        state_index = matches[position].states.size-1
        showGame(matches[position].states[state_index])

    }
    fun nextState(view: View){
        //Log.d("arrow","next  $state_index  $position")
        if(state_index<matches[position].states.size-1 && state_index!=-1 && position>=0){
            state_index++
            showGame(matches[position].states[state_index])
        }

    }
    fun print(states: ArrayList<Array<IntArray>>){
        for(i in 0 until states.size){
            //Log.d("States ",i.toString())
            for(j in 0 until states[i].size){

                var line = "$j: "
                for(k in 0 until states[i][j].size){
                    line +=" " + states[i][j][k].toString()
                }
                Log.d("States",line)


            }
            Log.d("States","\n")
        }
    }


    override fun onPositionClicked(x: Int, y: Int) {

    }

}
