package com.example.myapplication

class HumanPlayer(playerName: String): Player(playerName) {
    fun makeMove(row: Int, column: Int) {
        val checkGame = this.game ?: return
        val checkToken = this.token ?: return
        checkGame.placeToken(checkToken, row, column)
    }
}