package com.wl

interface IntLottery {
    fun draw(): Int

    fun empty(): Boolean

    fun remaining(): Int
}