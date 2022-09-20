package com.example.myapplication

data class Field(var token: Token? = null) {
    fun isEmpty(): Boolean {
        return token == null
    }

    override fun toString(): String {
        return token?.value.toString()
    }
}