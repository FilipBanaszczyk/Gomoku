package com.example.myapplication

import org.junit.Assert
import org.junit.Test

class GomokuUnitTest {
    val size = 8
    @Test
    fun `check if five connected with 4x4 square`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,2,2,2,0),
            intArrayOf(0,0,0,2,2,2,2,0),
            intArrayOf(0,0,0,2,2,2,2,0),
            intArrayOf(0,0,0,2,2,2,2,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(2))
        Assert.assertEquals(gomoku.fiveConnected(), false)
    }
    @Test
    fun `check if five connected with gap`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(2))
        Assert.assertEquals(gomoku.fiveConnected(), false)
    }
    @Test
    fun `check if five connected with longer chain`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(2))
        Assert.assertEquals(gomoku.fiveConnected(), true)
    }
    @Test
    fun `check if five connected with other expected token`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,2,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(1))
        Assert.assertEquals(gomoku.fiveConnected(), false)
    }
    @Test
    fun `check if five connected with wrong token values`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1),
            intArrayOf(-1,-1,-1,-1,-1,-1,-1,-1)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(3))
        Assert.assertEquals(gomoku.fiveConnected(), false)
    }
    @Test
    fun `placing token on non empty field should not work`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(1,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,1,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(1))
        gomoku.placeToken(Token(2),0,0)

        Assert.assertEquals(gomoku.getBoard()[0][0].token?.value, 1)
    }
    @Test
    fun `passing wrong column or row should not work`(){
        val arr:Array<IntArray> = arrayOf(
            intArrayOf(1,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,1,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )
        val gomoku = Gomoku(GameBoard(size,size,arr),Token(1),Token(2),Token(1))
        gomoku.placeToken(Token(2),123,123)

        Assert.assertEquals(gomoku.getBoard()[0][0].token?.value, 1)
    }
}