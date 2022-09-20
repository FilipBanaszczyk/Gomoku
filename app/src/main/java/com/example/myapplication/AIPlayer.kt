package com.example.myapplication

class AIPlayer(playerName: String = "AI", private val searchDepth: Int): Player(playerName) {
    fun makeMove() {
        val checkGame = this.game ?: return
        val checkToken = this.token ?: return
        val (row, column) = findBestMove(checkGame.getGameBoardCopy(), checkToken) ?: return
        checkGame.placeToken(checkToken, row, column)
    }

    private fun findBestMove(gameBoard: GameBoard, token: Token): Pair<Int, Int>? {
        val (row, column) = Alphabeta().Search(
            this.searchDepth,
            Board(gameBoard.getValuesMatrix()),
            true,
            token.value,
            Int.MIN_VALUE,
            Int.MAX_VALUE
        )[1] as IntArray

        return Pair(row, column)
    }
}