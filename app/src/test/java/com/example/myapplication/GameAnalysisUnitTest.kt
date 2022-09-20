package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

class GameAnalysisUnitTest {
    val size = 8

    @Test
    fun `check evaluation return 50 50 with mirror moves`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,2,1,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val new = GameBoard(size,size,arr)
        val analysis: GameAnalysis = GameAnalysis(size,size)
        val result = analysis.getEvaluation(new)
        assertEquals(result, Pair(50,50))


    }
    @Test
    fun `check evaluation return 50 50 with empty board`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val new = GameBoard(size,size,arr)
        val analysis: GameAnalysis = GameAnalysis(size,size)
        val result = analysis.getEvaluation(new)
        assertEquals(result, Pair(50,50))


    }
    @Test
    fun `check getNewMove return proper move`(){

        val analysis: GameAnalysis = GameAnalysis(size,size)
        val old = Board(size,size)
        old.makeMove(1,1,1)
        val new = Board(size,size)
        new.makeMove(1,1,1)
        new.makeMove(1,2,2)
        analysis.old_board = old
        assertEquals(analysis.getNewMove(new),Triple(1,2, 2))
    }
    @Test
    fun `check getNewMove return null after no move`(){

        val analysis: GameAnalysis = GameAnalysis(size,size)
        val old = Board(size,size)
        old.makeMove(1,1,1)
        val new = Board(size,size)
        new.makeMove(1,1,1)

        analysis.old_board = old
        assertEquals(analysis.getNewMove(new),null)
    }

}