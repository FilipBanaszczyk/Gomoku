package com.example.myapplication

import android.util.Log

class GameStateRegister(private val game: MetaGame) {
    val gameStates = mutableListOf<GameState>()

    fun saveState() {
        val state = getCurrentState() ?: return
        if (!isAlreadySaved(state)) {
            gameStates.add(state)
            //print(state)
        }

        //Log.i("states", gameStates.size.toString())
    }
    fun print(state:GameState){
        Log.d("States","Register saving state")
        val board = state.gameBoard.getValuesMatrix()
        for(j in 0 until board.size){

            var line = "$j: "
            for(k in 0 until board[j].size){
                line +=" " + board[j][k].toString()
            }
            Log.d("",line)


        }
        Log.d("","")
    }
    fun setStates(states: ArrayList<Array<IntArray>>, token: Token){
        val temp = ArrayList<GameState>()
        var next_token = token
        //Log.d("Register copy states", states.size.toString())
        for(i in 0 until states.size ){
            //Log.d("Copy states iter $i",i.toString())
            temp.add(GameState(GameBoard(states[states.size-i-1].size,states[states.size-i-1].size, states[states.size-i-1]),next_token))
            next_token = getNextToken(next_token)
        }
        //Log.d("Register copy states", temp.size.toString())
        for(i in 0 until temp.size){
            gameStates.add(temp[temp.size-i-1])
        }
    }
    fun getNextToken(token: Token):Token{
        when (token.value){
            1 ->return Token(2)
            2->return Token(1)
            else->return Token(1)
        }
    }

    private fun getCurrentState(): GameState? {
        val gameBoard = game.getGameBoardCopy()

        val turn = game.currentTurn() ?: Token(1)
        return GameState(gameBoard, turn)
    }

    private fun isAlreadySaved(state: GameState):Boolean =
        gameStates.isNotEmpty() && state.gameBoard.equals(gameStates.last().gameBoard)

    fun recoverLastState() {
        //Log.d("Gamestateregister","recover")
        val lastState = pullLastState() ?: return
        if (!game.isGameOver()) {
            game.switchGameBoard(lastState.gameBoard)
            game.setTurn(lastState.expectedToken)
        }
    }

    private fun pullLastState(): GameState? {
        return if (gameStates.isNotEmpty()) gameStates.removeAt(gameStates.lastIndex)
        else null
    }

    fun clearRegistry() {
        gameStates.clear()
    }
    fun divideMoves():ArrayList<Array<IntArray>>{
        val result = ArrayList<Array<IntArray>>()
        for(i in 0 until gameStates.size){
            val state = gameStates[i].gameBoard.getValuesMatrix()

            if(!isEmpty(state)){
                result.add(state)

            }
            if(i<gameStates.size-1){
                val next  = gameStates[i+1].gameBoard.getValuesMatrix()
                result.add(getMiddleMove(state, next))
            }

        }
       // Log.d("presenter","divided ${gameStates.size} to ${result.size} moves")
        return result
    }
    fun isEmpty(old: Array<IntArray>):Boolean {
        for(i in 0 until old.size){
            for(j in 0 until old[i].size){
                if(old[i][j]!=0){
                    return false
                }
            }
        }
        return true
    }
    fun getMiddleMove(old: Array<IntArray>, new: Array<IntArray>):Array<IntArray>{
        val result = Array<IntArray>(old.size) {IntArray(old[0].size)}
        //Log.d("presenter","old")
        //print(old)
        //Log.d("presenter","new")
        //print(new)
        for(i in 0 until old.size){
            for(j in 0 until old[i].size){
                if(old[i][j] == new[i][j]){
                    //Log.d("same", "i:$i j:$j")
                    result[i][j] = old[i][j]
                }
                else{
                    //Log.d("move on","i:$i j:$j first:${first.value} old:${old[i][j]}  new:${new[i][j]}")
                    if(new[i][j] == 1){


                        result[i][j] = new[i][j]
                    }
                    else{
                        result[i][j] = old[i][j]
                    }
                }
            }
        }
        //Log.d("presenter","result")
        //print(result)
        return result

    }
}