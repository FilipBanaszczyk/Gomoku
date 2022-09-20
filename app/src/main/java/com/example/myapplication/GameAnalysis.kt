package com.example.myapplication

import android.util.Log
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt


class GameAnalysis(rows :Int, columns : Int) {
    val p1_best: ArrayList<Triple<Int,Int,Int>> = ArrayList()
    val p2_best: ArrayList<Triple<Int,Int,Int>> = ArrayList()
    var old_board : Board = Board(rows, columns)
    var last : Int = 0

    var patterns: HashMap<String?, Int?> = object : HashMap<String?, Int?>() {
        init {
            put("22222", 100000)
            put("022220", 10000)
            put("02220", 1000)
            put("02200", 100)
            put("00220", 100)
            put("122220", 1000)
            put("102220", 900)
            put("122202", 900)
            put("122200", 100)
            put("122020", 90)
            put("122000", 10)
            put("102200", 10)
            put("100220", 10)
            put("022221", 1000)
            put("022201", 900)
            put("202221", 900)
            put("002221", 100)
            put("020221", 90)
            put("000221", 10)
            put("002201", 10)
            put("022001", 10)
            put("20202", 100)
            put("211112", 10000)


            put("11111", -100000)
            put("011110", -10000)
            put("01110", -1000)
            put("01100", -100)
            put("00110", -100)
            put("211110", -1000)
            put("201110", -900)
            put("211101", -900)
            put("211100", -100)
            put("211010", -90)
            put("211000", -10)
            put("201100", -10)
            put("200110", -10)
            put("011112", -1000)
            put("011102", -900)
            put("101112", -900)
            put("001112", -100)
            put("010112", -90)
            put("000112", -10)
            put("001102", -10)
            put("011002", -10)
            put("10101", -100)

            put("122221", -10000)
        }
    }
    fun clearMovesList(){
        Log.d("analysis","clear")
        p1_best.clear()
        p2_best.clear()
    }
    fun undoMoves(mode: Int){
        if(mode==0){
            //Log.d("analysis","UNDO $last")
            if(last==1 && p1_best.size>0){
                //Log.d("analysis","undo 1")
                last = 2
                p1_best.removeAt(p1_best.size-1)
            }
            else if(last==2 && p2_best.size>0){
                //Log.d("analysis","undo 2")
                last = 1
                p2_best.removeAt(p2_best.size-1)
            }
        }
        else{
           // Log.d("analysis","UNDO both")
            if(p2_best.size>0 && p1_best.size>0){
                p1_best.removeAt(p1_best.size-1)
                p2_best.removeAt(p2_best.size-1)
            }
        }
    }
    fun restoreMoves(states: MutableList<GameState>):Pair<String,String>{
        for(i in 0 until states.size){
            getEvaluation(states[i].gameBoard)
        }
        return getBestMoves()

    }
    fun getBestMoves():Pair<String, String>{
        val p1_size = p1_best.size
        val p2_size = p2_best.size
        var p1_result = ""
        var p2_result = ""

        if(p2_size>0 ) {
            val sorted_p2 = p2_best.sortedWith(compareBy { it.third }).reversed()
            //Log.d("best of 2",sorted_p2[0].toString())
            for(i in 0 until p2_size){
                if(i<5){
                    p2_result+=" ${getLetter(sorted_p2[i].first)}.${sorted_p2[i].second}"
                }
            }
        }
        if(p1_size>0) {
            val sorted_p1 = p1_best.sortedWith(compareBy { it.third })
            for(i in 0 until p1_size){
                if(i<5){
                    p1_result+=" ${getLetter(sorted_p1[i].first)}.${sorted_p1[i].second}"
                }
            }
            //Log.d("best of 1",sorted_p1[0].toString())
        }

        //Log.d("Best",p1_result+"   "+p2_result)
        return Pair(p1_result,p2_result)
    }
    private fun getLetter(column: Int):String{
        Log.d("analisys get letter ","$column")
        return when(column){
            1 -> "A"
            2 -> "B"
            3 -> "C"
            4 -> "D"
            5 -> "E"
            6 -> "F"
            7 -> "G"
            8 -> "H"
            9 -> "I"
            10 -> "J"
            11 -> "K"
            12 -> "L"
            else -> ""

        }
    }


    fun getEvaluation(game_board: GameBoard):Pair<Int, Int>{
        val board  =  gameBoardToBoard(game_board)

        var pos = getScore(board).first.toDouble()*10
        var neg = -1*getScore(board).second.toDouble()*10
        val score = pos-neg
        pos = sqrt(pos)
        neg = sqrt(neg)
        val sum = pos + neg

        addMoves(board, score.toInt())
        //printBoard(board)
        copyBoard(board)
        //printBoard(board)
       // Log.d("Sum = ","$pos   $neg    $sum")
        printLists()
        if(sum.toInt()==0){
            return Pair(50,50)
        }
        else{
            return  Pair(((pos*100)/sum).toInt(),100- (pos*100/sum).toInt())
        }

    }
    private fun printLists(){
        for(i in 0 until p1_best.size){
            //Log.d("p1 bests:", p1_best[i].toString())
        }
        for(i in 0 until p2_best.size){
            //Log.d("p2 bests:", p2_best[i].toString())
        }
    }
    private fun printBoard(gboard: Board){
        for(i in 0..(gboard.board_matrix.size-1)){

            var line = "$i: "
            for(j in 0..(gboard.board_matrix[i].size-1)){
                line +=" " + gboard.board_matrix[i][j].toString()
            }
            //Log.d("Analysis new Board",line)


        }
        for(i in 0..(old_board.board_matrix.size-1)){

            var line = "$i: "
            for(j in 0..(old_board.board_matrix[i].size-1)){
                line +=" " + old_board.board_matrix[i][j].toString()
            }
            //Log.d("Analysis old Board",line)


        }
    }
    fun addMoves(board: Board, score: Int){

        val result = getNewMove(board)

        //Log.d("ANALYSIS triple: ",result.toString())

        if(result?.third==1){
            last = 1
            //Log.d("DIFF1",score.toString()+"    "+(score-getSum()).toString())
            p1_best.add(Triple(result.first+1, result.second+1, score-getSum()))



        }
        else if(result?.third==2){
            last = 2
            //Log.d("DIFF2",score.toString()+"    "+(score-getSum()).toString())
            p2_best.add(Triple(result.first+1, result.second+1, score-getSum()))

        }
        else{
            //Log.d("ANALYSIS","nie powinno tego byc ${result?.third}")
        }
    }
    private fun getSum():Int{
        var sum = 0
        for(i in 0 until p1_best.size){
            sum+=p1_best[i].third
        }
        for(i in 0 until p2_best.size){
            sum+=p2_best[i].third
        }
        return sum
    }
    fun copyBoard(board: Board){
        for(i in 0 until board.BOARD_HEIGHT){
            for(j in 0 until board.BOARD_WIDTH){
                this.old_board.board_matrix[i][j]=board.board_matrix[i][j]
            }
        }
    }
    fun getNewMove(board : Board):Triple<Int,Int,Int>?{
        for(i in 0 until board.board_matrix.size){
            for(j in 0 until board.board_matrix.size){
                //Log.d("ANALYSIS-Change: ", "${old_board.board_matrix[i][j]}   ${board.board_matrix[i][j]}")
                if(old_board.board_matrix[i][j]!=board.board_matrix[i][j]){
                    //Log.d("ANALYSIS-Change found: ", "$i   $j")
                    return Triple(i,j,board.board_matrix[i][j])
                }



            }
        }
        return null
    }
    private fun gameBoardToBoard(game_board: GameBoard):Board{
        val board: Board = Board(game_board.rows, game_board.columns)
        for(i in 0 until game_board.rows){
            for(j in 0 until game_board.columns){
                val field_value = game_board.getBoard()[i][j].token?.value
                if(field_value!=null){
                    board.board_matrix[i][j] = field_value
                }
                else{
                    board.board_matrix[i][j] = 0
                }

            }
        }
        return board
    }

    //returns board score for computer
    fun getScore(board: Board): Pair<Int,Int> {
        var score_pos = 0
        var score_neg = 0

        //list of all lines on board(horizontal, vartical, diagonal)
        val all_lines = ArrayList<String>()

        //rows
        for (r in board.board_matrix.indices) {
            var line = ""
            for (c in 0 until board.board_matrix[0].size) {
                line = line + board.board_matrix[r][c].toString()
            }
            all_lines.add(line)
        }

        //columns
        for (c in 0 until board.board_matrix[0].size) {
            var line = ""
            for (r in board.board_matrix.indices) {
                line = line + board.board_matrix[r][c].toString()
            }
            all_lines.add(line)
        }

        //diagonal right
        for (k in 0..board.board_matrix.size + board.board_matrix[0].size - 2) {
            var line = ""
            for (j in 0..k) {
                val i = k - j
                if (i < board.board_matrix.size && j < board.board_matrix[0].size) {
                    line = line + board.board_matrix[i][j].toString()
                }
            }
            if (line.length >= 5) all_lines.add(line)
        }

        //diagonal left
        for (k in 0..board.board_matrix.size + board.board_matrix[0].size - 2) {
            var line = ""
            for (j in 0..k) {
                val i = k - j
                if (i < board.board_matrix.size && j < board.board_matrix[0].size) {
                    line = line + board.board_matrix[board.board_matrix.size - i - 1][j].toString()
                }
            }
            if (line.length >= 5) all_lines.add(line)
        }

        //check all lines for patterns add and points if found
        for (line in all_lines) {
            for ((key, value) in patterns) if (line.contains(
                    key!!
                )
            ) if (value != null) {
                if(value>0){ score_pos += value}
                else{score_neg += value}
            }
        }
        return Pair(score_pos,score_neg)
    }
}