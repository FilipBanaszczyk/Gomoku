package com.example.myapplication

import android.os.AsyncTask

class CompMoveAsyncTask(private val aiPlayer: AIPlayer, private val taskCaller: TaskCaller): AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg params: Unit?) {
        aiPlayer.makeMove()
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

        taskCaller.taskCompleted()
    }
}