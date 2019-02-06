package com.wl

interface IntLottery {


    // get random index, according to the given distribution. throw NoSuchElementException if no items left
    fun draw(): Int

    // return true in case there are no items to draw, false otherwise
    fun empty(): Boolean

    // return the number of remaining valid calls to 'draw'
    fun remaining(): Int
}