package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

class GameRegisterUnitTest {
    private val ui = ActivityGame()
    val fm = 0
    val board_size = 0
    val game_mode = 0
    val presenter:GamePresenter = GamePresenter(ui,fm,board_size,game_mode)
    @Test

    fun `check get middle move from two same boards should return same board`(){

        val register = presenter.register
        val old_board = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val new_board = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val expected = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        assertArrayEquals(register.getMiddleMove(old_board, new_board), expected)


    }
    @Test
    fun `check get middle move return proper board`(){

        val register = presenter.register
        val old_board = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val new_board = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,2,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val expected = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        assertArrayEquals(register.getMiddleMove(old_board, new_board), expected)

    }
    @Test
    fun `check divide moves return proper count of states`(){
        val arr = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )

        val register = presenter.register
        val states_count = 5
        for (i in 0 until 5){
            val new_state = Array<IntArray>(board_size*2+8) {IntArray(board_size*2+8)}
            register.gameStates.add(GameState(GameBoard(8,8,arr),Token(1)))
        }
        assertEquals(register.divideMoves().size,states_count*2-1)
    }
}