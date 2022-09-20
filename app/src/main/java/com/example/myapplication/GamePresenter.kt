package com.example.myapplication

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import kotlin.math.max
import kotlin.math.roundToInt

class GamePresenter(private val game_ui: GameListener,fmMode: Int, boardSizeMode: Int, private val gameMode: Int): TaskCaller {

   val token1 = Token(1)
   val token2 = Token(2)
   lateinit var first:Token
   val analysis = GameAnalysis(boardSizeMode*2+8, boardSizeMode*2+8)
   val game = Gomoku(createBoard(boardSizeMode), token1, token2, getFirstMove(fmMode))
   val register = GameStateRegister(game)
   val history = GamesHistory()
   lateinit var player1: Player
   lateinit var player2: Player
   var moveLock = true
   var movesCounter = 0
        set(value) { field = max(value, 0) }



    init {
        when (gameMode) {
            0 -> {
                player1 = HumanPlayer("Player1").also { it.assign(token1); it.play(game) }
                player2 = HumanPlayer("Player2").also { it.assign(token2); it.play(game) }
            }
            1 -> {
                player1 = HumanPlayer("Player1").also { it.assign(token1); it.play(game) }
                player2 = AIPlayer("Player2", 1).also { it.assign(token2); it.play(game) }
            }
            2 -> {
                player1 = HumanPlayer("Player1").also { it.assign(token1); it.play(game) }
                player2 = AIPlayer("Player2", 2).also { it.assign(token2); it.play(game) }
            }
            3 -> {
                player1 = HumanPlayer("Player1").also { it.assign(token1); it.play(game) }
                player2 = AIPlayer("Player2", 3).also { it.assign(token2); it.play(game) }
            }
        }
    }

    interface GameListener{
        fun setBoard(board : Array<IntArray>)
        fun showYouWin()
        fun showAiWins()
        fun showPlayer2Wins()
        fun showPlayer2Move()
        fun showPlayer1Wins()
        fun showPlayer1Move()
        fun showYourMove()
        fun showDraw()
        fun getOver():Boolean
        fun saveOver(over: Boolean)
        fun getWinner():Token?
        fun saveWinner(win: Token)
        fun updateBestMoves(p1:String, p2:String)
        fun saveToken(token: Int)
        fun gameBoardNewGame()
        fun updateWining(p1: Int, p2: Int)
        fun getToken(): Int
        fun drawWinningPositions(arr:Array<Pair<Int,Int>>)
        fun getContext(): Context
        fun setModeInfo(modeInfo: String)
        fun updateMovesCounter(moves: Int)
        fun startTimer()
        fun getStates(): ArrayList<Array<IntArray>>
        fun resetTimer()
        fun stopTimer()
    }

    private fun getFirstMove(fmMode: Int): Token {
        when (fmMode) {
            1 -> {
                first = this.token1
                return first
            }
            2 -> {
                first = this.token2
                return first
            }
            else -> if (Math.random().roundToInt() == 0){
                first = this.token1
                return first
            }
            else {
                first = this.token2
                return first
            }

        }
    }
    fun getStates():ArrayList<Array<IntArray>>{
        val result = ArrayList<Array<IntArray>>()
        for(i in 0 until register.gameStates.size){
            result.add(register.gameStates[i].gameBoard.getValuesMatrix())
        }
       // Log.d("presenter", "return ${result.size} states")
        return result

    }

    fun createBoard(boardSizeMode: Int): GameBoard {
        return when (boardSizeMode) {
            1 -> GameBoard(10,10)
            2 -> GameBoard(12,12)
            else -> GameBoard(8,8)
        }
    }

    fun restore(board : Array<IntArray>){

        for(i in 0..(board.size-1)){

            var line = "$i: "
            for(j in 0..(board[i].size-1)){
                line +=" " + board[i][j].toString()
            }
           // Log.d("game_restore",line)
        }
        setModeInfo()
        checkForAIMove()
        game.restoreGameBoard(board)
        game_ui.setBoard(board)
        updateMoveInfo()
        updateMovesCounter()
        val new = game.getBoard()
        for(i in 0..(new.size-1)){

            var line = "$i: "
            for(j in 0..(new[i].size-1)){
                line +=" " + new[i][j].toString()
            }
            //Log.d("game_restored",line)
        }
        game_ui.startTimer()
        //Log.d("presenter","restoring token ${game_ui.getToken()}")

        setNextToken(game_ui.getToken())
        val token = prepareToken(game_ui.getToken())
        if(gameMode==0){
            setNextToken(token)
        }
        else{
            setNextToken(1)
        }
        game.isGameOver()
        checkGameOver()
    }
    fun setStates(states: ArrayList<Array<IntArray>>){
        register.setStates(states, Token(game_ui.getToken()))
        analysis.restoreMoves(register.gameStates)
    }
    fun prepareToken(token:Int):Int {
        return if (game.isGameOver()) {
            return token
        } else {
            when {
                token == 1 -> return 2
                token == 2 -> return 1
                else -> return  token
            }

        }
    }
    fun setMoves(moves: Int?){
        movesCounter = moves ?: 0
        //Log.d("moves_counter",movesCounter.toString())
    }
    fun getMoves():Int{
        return movesCounter
    }
    fun startGame() {
        setModeInfo()
        checkForAIMove()
        game_ui.gameBoardNewGame()
        game_ui.setBoard(game.getGameBoardCopy().getValuesMatrix())
        updateMoveInfo()
        updateMovesCounter()
        game_ui.resetTimer()
    }

    private fun setModeInfo() {
        when (gameMode) {
            0 -> game_ui.setModeInfo("PvP")
            1 -> game_ui.setModeInfo("EASY")
            2 -> game_ui.setModeInfo("MEDIUM")
            3 -> game_ui.setModeInfo("HARD")
        }
    }

    fun restartGame() {
        game_ui.saveOver(false)
        movesCounter = 0
        game.resetGame()
        analysis.clearMovesList()
        register.clearRegistry()
        checkForAIMove()
        game_ui.gameBoardNewGame()
        game_ui.setBoard(game.getGameBoardCopy().getValuesMatrix())
        updateMoveInfo()
        updateMovesCounter()
        game_ui.resetTimer()
    }

    private fun checkForAIMove() {
        //Log.d("check for AI",moveLock.toString())
        if (player2 is AIPlayer && game.currentTurn() == player2.token) {
            moveLock = true
            CompMoveAsyncTask((player2 as AIPlayer), this).execute()
        }
        else
            moveLock = false
    }

    fun undoMove() {
        if(!game.isGameOver() || game_ui.getOver()) {
            register.recoverLastState()
            analysis.undoMoves(gameMode)
            game_ui.setBoard(game.getGameBoardCopy().getValuesMatrix())
            movesCounter--
            updateMovesCounter()
        }
    }
    fun refreshWiningLine(){
        if(game.isGameOver())
            drawWinningLine()
    }

    fun onBoardClick(row: Int, column: Int) {
        saveTokenOnRotate()
        if (!isMoveValid(row, column)) return
        //Log.d("click", game.currentTurn()?.value.toString())
        if (game.currentTurn() == player1.token && player1 is HumanPlayer) {
            //Log.d("on click","player 1")
            register.saveState()
            (player1 as HumanPlayer).makeMove(row, column)
            movesCounter++
            if (game.isGameOver()) onGameOver()
            else checkForAIMove()
        }
        else if (game.currentTurn() == player2.token && player2 is HumanPlayer) {
            //Log.d("on click","player 2")
            register.saveState()
            (player2 as HumanPlayer).makeMove(row, column)
            movesCounter++
            if (game.isGameOver()) onGameOver()
        }
        game_ui.setBoard(game.getGameBoardCopy().getValuesMatrix())
        updateMoveInfo()
        updateMovesCounter()
    }
    private fun checkGameOver(){
        //Log.d("check", game.isGameOver().toString() )
        if (game.isGameOver() || game_ui.getOver()) {

            onGameOver()
        }
    }

    private fun isMoveValid(row: Int, column: Int): Boolean {
        Log.d("moveblock", (!moveLock).toString() +" "+ (row >= 0 && column >= 0).toString() +" "+ game.isMoveEligible(row, column).toString())
        return !moveLock && row >= 0 && column >= 0 && game.isMoveEligible(row, column)
    }
    private fun saveTokenOnRotate(){
        val token = game.currentTurn()
        //Log.d("saving", token.toString())
        if(token!=null){
            game_ui.saveToken(token.value)
        }

    }
    private fun setNextToken(token :Int){
        //Log.d("settoken", token.toString())
        game.setTurn(Token(token))

    }

    private fun onGameOver() {
        moveLock = true
        Log.d("onGameOver", game_ui.getOver().toString() )

        register.saveState()
        saveGameToHistory()
        game_ui.stopTimer()

        if(game_ui.getOver()){
            Log.d("onGameOver", "restore winner" )
            game.restoreWinner(game_ui.getWinner())
        }
        game_ui.saveOver(true)
        if (game.getWinner() == player1.token) {
            game_ui.saveWinner(player1.token!!)
            game_ui.updateWining(0,-1)
            if (player2 is HumanPlayer)
                game_ui.showPlayer1Wins()
            else
                game_ui.showYouWin()
        }
        else if (game.getWinner() == player2.token) {
            game_ui.saveWinner(player2.token!!)
            game_ui.updateWining(-1,0)
            if (player2 is HumanPlayer)
                game_ui.showPlayer2Wins()
            else
                game_ui.showAiWins()
        } else
            game_ui.showDraw()

        drawWinningLine()
    }

    private fun drawWinningLine() {
        val fields = findFiveConnected()
        if (fields.isNotEmpty())
            game_ui.drawWinningPositions(arrayOf(fields.first(), fields.last()))
    }

    private fun saveGameToHistory() {
        val winner = when {
            game.getWinner() == player1.token -> "PLAYER"
            game.getWinner() == player2.token -> "AI"
            else -> "DRAW"
        }

        when (gameMode) {
            1 -> history.save("EASY", winner, divideMoves(), game_ui.getContext())
            2 -> history.save("MEDIUM", winner, divideMoves(), game_ui.getContext())
            3 -> history.save("HARD", winner, divideMoves(), game_ui.getContext())
        }
    }
    fun divideMoves():ArrayList<Array<IntArray>>{

        return register.divideMoves()
    }



    fun print(board:Array<IntArray>){
        //Log.d("States","Register saving state")

        for(j in 0 until board.size){

            var line = "$j: "
            for(k in 0 until board[j].size){
                line +=" " + board[j][k].toString()
            }
            Log.d("States",line)


        }
        Log.d("States","")
    }

    private fun updateMoveInfo() {
        updateWiningInfo()
        if (game.currentTurn() == player1.token) {
            if (player2 is HumanPlayer)
                game_ui.showPlayer1Move()
            else
                game_ui.showYourMove()
        }
        else if (game.currentTurn() == player2.token && player2 is HumanPlayer) {
            game_ui.showPlayer2Move()
        }
    }
    private fun updateWiningInfo(){
        val ev = analysis.getEvaluation(game.getGameBoardCopy())
        val bests = analysis.getBestMoves()
        game_ui.updateBestMoves(bests.first, bests.second)
        //Log.d("EVALUTAION: ", "p2 = ${ev.first}.%   p1 = ${ev.second}%")
        if(!game.isGameOver()){
            game_ui.updateWining(ev.second, ev.first)
        }




    }

    private fun updateMovesCounter() {
        updateWiningInfo()
        if (game.currentTurn() == game.firstTurn())
            game_ui.updateMovesCounter(movesCounter)
        else
            game_ui.updateMovesCounter(movesCounter)
        //Log.d("update_counter",movesCounter.toString())
    }

    override fun taskCompleted() {
        movesCounter++
        updateMovesCounter()
        game_ui.setBoard(game.getGameBoardCopy().getValuesMatrix())

        if (game.isGameOver())
            onGameOver()
        else {
            moveLock = false
            updateMoveInfo()
        }
    }

    private fun findFiveConnected(): Array<Pair<Int, Int>> {
        val winningToken = game.getWinner() ?: return arrayOf()
        val gameBoard = game.getGameBoardCopy()

        for (c in 0 until gameBoard.columns-4) {
            for (r in 0 until gameBoard.rows) {
                if (gameBoard.getToken(r, c) == winningToken &&
                    gameBoard.getToken(r, c+1) == winningToken &&
                    gameBoard.getToken(r, c+2) == winningToken &&
                    gameBoard.getToken(r, c+3) == winningToken &&
                    gameBoard.getToken(r, c+4) == winningToken)
                    return arrayOf(
                        Pair(r,c),
                        Pair(r,c+1),
                        Pair(r,c+2),
                        Pair(r,c+3),
                        Pair(r,c+4))
            }
        }
        for (c in 0 until gameBoard.columns) {
            for (r in 0 until gameBoard.rows-4) {
                if (gameBoard.getToken(r, c) == winningToken &&
                    gameBoard.getToken(r+1, c) == winningToken &&
                    gameBoard.getToken(r+2, c) == winningToken &&
                    gameBoard.getToken(r+3, c) == winningToken &&
                    gameBoard.getToken(r+4, c) == winningToken)
                    return arrayOf(
                        Pair(r,c),
                        Pair(r+1,c),
                        Pair(r+2,c),
                        Pair(r+3,c),
                        Pair(r+4,c))
            }
        }
        for (c in 0 until gameBoard.columns-4) {
            for (r in 0 until gameBoard.rows-4) {
                if (gameBoard.getToken(r, c) == winningToken &&
                    gameBoard.getToken(r+1, c+1) == winningToken &&
                    gameBoard.getToken(r+2, c+2) == winningToken &&
                    gameBoard.getToken(r+3, c+3) == winningToken &&
                    gameBoard.getToken(r+4, c+4) == winningToken)
                    return arrayOf(
                        Pair(r,c),
                        Pair(r+1,c+1),
                        Pair(r+2,c+2),
                        Pair(r+3,c+3),
                        Pair(r+4,c+4))
            }
        }
        for (c in 0 until gameBoard.columns-4) {
            for (r in 3 until gameBoard.rows) {
                if (gameBoard.getToken(r, c) == winningToken &&
                    gameBoard.getToken(r-1, c+1) == winningToken &&
                    gameBoard.getToken(r-2, c+2) == winningToken &&
                    gameBoard.getToken(r-3, c+3) == winningToken &&
                    gameBoard.getToken(r-4, c+4) == winningToken)
                    return arrayOf(
                        Pair(r,c),
                        Pair(r-1,c+1),
                        Pair(r-2,c+2),
                        Pair(r-3,c+3),
                        Pair(r-4,c+4))
            }
        }
        return return arrayOf()
    }


}