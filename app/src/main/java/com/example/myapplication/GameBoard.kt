package com.example.myapplication

import android.util.Log

class GameBoard(val rows: Int, val columns: Int) {

    private val board: Array<Array<Field>> = Array(rows) { Array(columns) { Field() } }

    fun size(): Pair<Int, Int> = Pair(rows, columns)

    fun clear() {
        board.flatten().forEach { it.token = null }
    }
    fun getBoard(): Array<Array<Field>>{
        return board
    }

    fun isFull(): Boolean {
        for (field in board.flatten()) {
            if (field.isEmpty())
                return false
        }
        return true
    }
    constructor(rows: Int, columns: Int, old: Array<IntArray>): this(rows, columns) {

        for(i in 0..(old.size-1)){
            var line = "$i "
            for(j in 0..(old[i].size-1)){
                line+=old[i][j].toString()+" "

                if(old[i][j]==0){
                    board[i][j]=Field(null)
                }
                else{
                    val token = Token(old[i][j])
                    val field = Field(token)
                    board[i][j] = field
                }

            }
            //Log.d("board", line)
        }
    }
    


    fun isFieldEmpty(row: Int, column: Int): Boolean = board[row][column].isEmpty()

    fun placeToken(token: Token, row: Int, column: Int) {
        board[row][column].token = token
    }

    fun getToken(row: Int, column: Int): Token? = board[row][column].token

    fun copy(): GameBoard {
        val boardCopy = GameBoard(rows, columns)
        for ((origin, copy) in board.flatten().zip(boardCopy.board.flatten()))
            copy.token = origin.token
        return boardCopy
    }

    fun equals(gameBoard: GameBoard): Boolean {
        if (this.size() != gameBoard.size()) return false
        for (r in 0 until rows) {
            for (c in 0 until columns)
                if (getToken(r,c) != gameBoard.getToken(r,c))
                    return false
        }
        return true
    }

    fun getValuesMatrix(): Array<IntArray> =
        board.map { row ->
            row.map { field ->
                field.token?.value ?: 0
            }.toIntArray()
        }.toTypedArray()
}