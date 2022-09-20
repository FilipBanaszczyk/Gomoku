package com.example.myapplication

data class Token(val value: Int){
    override fun toString(): String {
        return value.toString()
    }
}