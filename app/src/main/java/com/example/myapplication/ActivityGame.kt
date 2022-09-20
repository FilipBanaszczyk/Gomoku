package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.res.Configuration

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.GameBoardView.PositionClickedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_game.*


class ActivityGame : AppCompatActivity(), PositionClickedListener, GamePresenter.GameListener {

    private lateinit var presenter: GamePresenter
    var sec_timer=0
    var l_mode = 0
    private var token:Int = 0
    var wining_text = ""
    var is_over = false
    var winning: Token? = null
    private var states = ArrayList<Array<IntArray>>()
    var timer = object: CountDownTimer(3600000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            sec_timer+=1000

            val time_string=String.format("%02d:%02d", (sec_timer/1000) / 60, (sec_timer/1000) % 60)
            tv_time.text="TIME: "+time_string
        }

        override fun onFinish() {
            tv_time.text="TIME: ETERNITY"
        }
    }
    fun modeToString(mode : Int): String{
        if(mode==0){
            return "P2"
        }
        else{
            return "AI"
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE) ?: return
        val gb_size_mode = sharedPref.getInt("SIZE", 0)
        val fm_mode = sharedPref.getInt("FIRSTMOVE", 0)
        l_mode = sharedPref.getInt("LEFT", 0)
        val game_mode = intent.extras?.getInt("GAMEMODE") ?: 0
        wining_text = modeToString(game_mode)
        if(l_mode==1){
            setContentView(R.layout.activity_game_left)
        }
        else{
            setContentView(R.layout.activity_game)
        }
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
                    startActivity(Intent(applicationContext, ActivityMainMenu::class.java))
                    overridePendingTransition(0, 0)
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
        gameBoardView.positionClickedListener=this
        presenter = GamePresenter(this, fm_mode, gb_size_mode, game_mode)
        tv_lvl.text="MODE:\nMEDIUM"
        tv_moves.text="MOVES:\n0"
        tv_time.text="TIME:\n0:00"
        tv_wining.text="P1: 0%\nAI: 0%"
        tv_best_moves.text="BEST: P1: \n $wining_text"



        if(savedInstanceState==null){
            presenter?.startGame()
        }
        else{
            var size = 0
            size = when (gb_size_mode) {
                1 -> 10
                2 -> 12
                else -> 8
            }
            val board : Array<IntArray> = Array<IntArray>(size) { IntArray(size) }
            for(i in 0..(board.size-1)){
                board[i] = savedInstanceState.getIntArray("board_row$i") ?: intArrayOf()
                var line = "$i: "
                for(j in 0..(board[i].size-1)){
                    line +=" " + board[i][j].toString()
                }
                //Log.d("board_restore",line)


            }
            winning = Token(savedInstanceState.getInt("winner"))
            val over = savedInstanceState.getBoolean("over")
            Log.d("game restoring ",over.toString())
            is_over = over
            token = savedInstanceState.getInt("token")
            states.clear()
            val states_size = savedInstanceState.getInt("states_q")
            //Log.d("game restoring size",states_size.toString())
            for(i in 0 until states_size ){
                val state : Array<IntArray> = Array<IntArray>(size) { IntArray(size) }
                for(j in 0..(state.size-1)){
                    state[j] = savedInstanceState.getIntArray("state_row$i $j") ?: intArrayOf()


                    // Log.d("board_restore",line)


                }
                //Log.d("game restoring states",states.size.toString())
                states.add(state)
            }
            presenter.setStates(states)
            presenter?.restore(board)
        }
    }

    override fun saveWinner(win: Token) {
        winning = win
    }

    override fun getWinner(): Token? {
        return winning
    }
    override fun saveOver(over: Boolean){
        this.is_over = over
    }
    override fun getOver(): Boolean{
        Log.d("game act","$is_over")
        return is_over
    }
    override fun updateWining(p1: Int, p2: Int){
        if(p1==-1){
            tv_wining.text="P1: ...\n$wining_text: WON!"
        }
        else if(p2==-1){
            tv_wining.text="P1: WON!\n$wining_text: ..."
        }
        else{
            tv_wining.text="P1: $p1%\n$wining_text: $p2%"
        }


    }
    override fun updateBestMoves(p1: String, p2: String){
        tv_best_moves.text = "P1: $p1\n$wining_text: $p2"
    }



    override fun getToken():Int { return this.token }

    override fun getContext(): Context = this.applicationContext

    override fun onStart() {
        super.onStart()

        //presenter?.startGame()
        //Log.d("start","start")
        gameBoardView.positionClickedListener=this
        presenter?.refreshWiningLine()
    }
    override fun resetTimer() {
        sec_timer = 0
        timer.cancel()
        tv_time.text="TIME: 0:00"
        timer.start()
    }

    override fun startTimer() {
        timer.start()
    }

    override fun getStates(): ArrayList<Array<IntArray>> {
        return states
    }

    override fun stopTimer() {
        timer.cancel()
    }

    override fun onPositionClicked(x: Int, y: Int) {
        //Log.i("X", x.toString())
        //Log.i("Y", y.toString())
       // Log.d("presenter",(presenter==null).toString())
        presenter.onBoardClick(x, y)
    }

    fun undoMoveClick(view: View){
        if(!is_over){
            //Log.d("undo", is_over.toString())
            presenter?.undoMove()
        }

    }

    fun restartGameClick(view: View){
        presenter?.restartGame()
    }

    override fun drawWinningPositions(arr:Array<Pair<Int,Int>>){
        gameBoardView.winingPoints=arr
        gameBoardView.gameFinished =  true
        gameBoardView.invalidate()
    }



    override fun setModeInfo(modeInfo: String) {
        tv_lvl.text = "MODE: $modeInfo"
    }

    override fun updateMovesCounter(moves: Int) {
        tv_moves.text = "MOVES: ${moves.toString()}"
    }

    override fun gameBoardNewGame(){
        resetTimer()
        gameBoardView.resetGame()
    }

    override fun showYourMove(){
        image_game_notification.setImageResource(R.drawable.your_move)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 700)
    }

    override fun showDraw() {
        image_game_notification.setImageResource(R.drawable.text_draw)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 2000)
    }

    override fun showPlayer1Move(){
        image_game_notification.setImageResource(R.drawable.player1_move)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 700)
    }

    override fun showPlayer1Wins(){
        image_game_notification.setImageResource(R.drawable.player1_wins)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 2000)
    }

    override fun showPlayer2Move(){
        image_game_notification.setImageResource(R.drawable.player2_move)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 700)
    }

    override fun showPlayer2Wins(){
        image_game_notification.setImageResource(R.drawable.player2_wins)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 2000)
    }

    override fun showAiWins(){
        image_game_notification.setImageResource(R.drawable.ai_wins)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 2000)
    }

    override fun setBoard(board : Array<IntArray>){

        gameBoardView.gameBoardArray = board
        gameBoardView.invalidate()

    }

    override fun showYouWin(){
        image_game_notification.setImageResource(R.drawable.you_win)
        image_game_notification.visibility=View.VISIBLE
//        Handler().postDelayed({
//            image_game_notification.visibility = View.INVISIBLE
//        }, 2000)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("moves", presenter.getMoves().toString())
        outState.putInt("timer",sec_timer)
        //Log.d("SSSSaving", token.toString())
        outState.putInt("token",token)
        val board = gameBoardView.gameBoardArray
        val states = presenter.getStates()
        //Log.d("size",board.size.toString()+" "+ board[0].size.toString())
        if(states.size>0){
            outState.putInt("states_q",states.size)
        }
        if(winning!=null){
            outState.putInt("winner", winning?.value!!)
        }

        Log.d("saving over", is_over.toString())
        outState.putBoolean("over", is_over)

        //Log.d("game saving states", states.size.toString())
        for(i in 0..(board.size-1)){
            outState.putIntArray("board_row$i", board[i] )
        }
        for(i in 0 until states.size){
            for(j in 0 until states[i].size){
                outState.putIntArray("state_row$i $j", states[i][j] )
            }
        }

    }

    override fun saveToken(token: Int) {
        this.token = token
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)
        tv_moves.text = "MOVES: "+savedInstanceState.getString("moves")
        presenter.setMoves(savedInstanceState.getString("moves")?.toInt())
        sec_timer = savedInstanceState.getInt("timer")
        Log.d("game restoring "," ")

        token = savedInstanceState.getInt("token")
        //Log.d("restore", token.toString())
        val sharedPref = this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE) ?: return
        val gb_size_mode = sharedPref.getInt("SIZE", 0)
        var size = 0
        size = when (gb_size_mode) {
            1 -> 10
            2 -> 12
            else -> 8
        }
        val board : Array<IntArray> = Array<IntArray>(size) { IntArray(size) }
        for(i in 0..(board.size-1)){
            board[i] = savedInstanceState.getIntArray("board_row$i") ?: intArrayOf()
            var line = "$i: "
            for(j in 0..(board[i].size-1)){
                line +=" " + board[i][j].toString()
            }
           // Log.d("board_restore",line)


        }



    }
}
