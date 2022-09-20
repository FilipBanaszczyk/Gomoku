package com.example.myapplication

import android.content.Context
import android.content.Context.MODE_APPEND
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*
import kotlin.math.sqrt

class GamesHistory {
    private val FILE_NAME = "matches_history.txt"

    fun save(diffLevel: String, winner: String, states: ArrayList<Array<IntArray>>, ctx: Context) {
        val fw = ctx.openFileOutput(FILE_NAME, MODE_APPEND)
        val calendar = java.util.Calendar.getInstance()

        fw.write(
            ("date:" + calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(
                Calendar.DAY_OF_MONTH
            ) + "/" + calendar.get(Calendar.HOUR_OF_DAY)
                    + "/" + calendar.get(Calendar.MINUTE) + "/" + calendar.get(Calendar.SECOND) + "\n").toByteArray()
        )
        fw.write("difficulty:$diffLevel\n".toByteArray())
        fw.write("winner:$winner\n".toByteArray())
        for(i in 0 until states.size){
            val board = states[i]
            fw.write(("board:" + board.joinToString("") { it.joinToString("") } + "\n").toByteArray())
        }


        fw.close()
    }

    fun load(ctx: Context): ArrayList<GameLog> {

        val fis = ctx.openFileInput(FILE_NAME)
        val isr = InputStreamReader(fis)
        val br = BufferedReader(isr)

        val dates = mutableListOf<String>()
        val diffLevels = mutableListOf<String>()
        val winners = mutableListOf<String>()
        val gameBoards = mutableListOf<ArrayList<Array<IntArray>>>()
        val game_states = ArrayList<Array<IntArray>>()
        br.forEachLine { line ->
            when {
                line.startsWith("date") ->{
                    dates.add(line.substringAfter(":"))
                    //Log.d("loading", line.substringAfter(":"))
                    if(game_states.size>0){
                        val temp = ArrayList<Array<IntArray>>()
                        for(i in 0 until game_states.size){
                            temp.add(game_states[i])
                        }
                        gameBoards.add(temp)
                        //Log.d("New game read","OOOOOOOOOOOOOOOOOOOOOOOOO")
                        //print(game_states)
                    }
                    game_states.clear()
                }
                line.startsWith("difficulty") -> diffLevels.add(line.substringAfter(":"))
                line.startsWith("winner") -> winners.add(line.substringAfter(":"))
                line.startsWith("board") -> game_states.add(
                    parseBoardStringToArray(
                        line.substringAfter(
                            ":"
                        )
                    )
                )
            }
        }
        gameBoards.add(game_states)
        for(i in 0 until gameBoards.size){
            //Log.d("New game read", "OOOOOOOOOOOOOOOOOOOOOOOOO")
            //print(gameBoards[i])
        }
        fis.close()

        val logs = ArrayList<GameLog>()
        for (i in dates.indices)
            logs.add(GameLog(dates[i], diffLevels[i], winners[i], gameBoards[i]))

        return logs
    }
    fun print(states: ArrayList<Array<IntArray>>){
        for(i in 0 until states.size){
            //Log.d("States ",i.toString())
            for(j in 0 until states[i].size){

                var line = "$j: "
                for(k in 0 until states[i][j].size){
                    line +=" " + states[i][j][k].toString()
                }
                Log.d("", line)


            }
            Log.d("", "-----------------------")
        }
    }

    private fun parseBoardStringToArray(boardString: String): Array<IntArray> {
        val boardSize = sqrt(boardString.length.toDouble()).toInt()
        val stringRows = boardString.chunked(boardSize)
        val board = stringRows.map {
            it.map { Character.getNumericValue(it) }.toIntArray()
        }.toTypedArray()

        return board
    }
}