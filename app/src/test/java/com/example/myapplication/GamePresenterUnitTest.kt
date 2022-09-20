package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

class GamePresenterUnitTest {
    private val ui = ActivityGame()
    @Test
    fun `check wrong board size passed should create 8x8`(){
        val fm = 0
        val board_size = 123
        val game_mode = 0
        val presenter:GamePresenter = GamePresenter(ui,fm,board_size,game_mode)
        assertEquals(presenter.createBoard(board_size).columns,8)
    }

}