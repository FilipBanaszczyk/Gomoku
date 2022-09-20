package com.example.myapplication

abstract class Player(val playerName: String) {
    protected var game: MetaGame? = null
    var token: Token? = null
        protected set

    fun play(game: MetaGame) {
        this.game = game
    }

    fun assign(token: Token) {
        this.token = token
    }
}