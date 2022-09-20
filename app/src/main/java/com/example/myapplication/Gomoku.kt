package com.example.myapplication

import android.R
import android.R.attr.button
import android.util.Log
import android.widget.EditText
import android.widget.TextView


class Gomoku(gameBoard: GameBoard, token1: Token, token2: Token, firstTurn: Token): MetaGame(
    gameBoard,
    token1,
    token2,
    firstTurn
) {

    override fun checkIfGameOver() {
        //Log.d("check","check")
        if (fiveConnected()) {
            //Log.d("GOMOKU ifOver","connected")
            setWinner()
            gameOver()
        }
        else if (gameBoard.isFull())
            gameOver()
    }



    fun fiveConnected(): Boolean {
        val checkToken = expectedToken ?: return false
        //Log.d("over","connected2  "+checkToken.toString())
        for (c in 0 until gameBoard.columns-4) {
            for (r in 0 until gameBoard.rows) {
                if (gameBoard.getToken(r, c) == checkToken &&
                    gameBoard.getToken(r, c + 1) == checkToken &&
                    gameBoard.getToken(r, c + 2) == checkToken &&
                    gameBoard.getToken(r, c + 3) == checkToken &&
                    gameBoard.getToken(r, c + 4) == checkToken){
                    //Log.d("connect","true")
                    return true
                }

            }
        }
        for (c in 0 until gameBoard.columns) {
            for (r in 0 until gameBoard.rows-4) {
                if (gameBoard.getToken(r, c) == checkToken &&
                    gameBoard.getToken(r + 1, c) == checkToken &&
                    gameBoard.getToken(r + 2, c) == checkToken &&
                    gameBoard.getToken(r + 3, c) == checkToken &&
                    gameBoard.getToken(r + 4, c) == checkToken){
                    //Log.d("connect","true")
                    return true
                }

            }
        }
        for (c in 0 until gameBoard.columns-4) {
            for (r in 0 until gameBoard.rows-4) {
                if (gameBoard.getToken(r, c) == checkToken &&
                    gameBoard.getToken(r + 1, c + 1) == checkToken &&
                    gameBoard.getToken(r + 2, c + 2) == checkToken &&
                    gameBoard.getToken(r + 3, c + 3) == checkToken &&
                    gameBoard.getToken(r + 4, c + 4) == checkToken){
                    //Log.d("connect","true")
                    return true
                }

            }
        }
        for (c in 0 until gameBoard.columns-4) {
            for (r in 4 until gameBoard.rows) {
                if (gameBoard.getToken(r, c) == checkToken &&
                    gameBoard.getToken(r - 1, c + 1) == checkToken &&
                    gameBoard.getToken(r - 2, c + 2) == checkToken &&
                    gameBoard.getToken(r - 3, c + 3) == checkToken &&
                    gameBoard.getToken(r - 4, c + 4) == checkToken){
                    //Log.d("connect","true")
                    return true
                }

            }
        }
        //Log.d("connect","false")

        return false
    }
}