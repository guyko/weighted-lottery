package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AliasLottery(
    private val weights: DoubleArray,
    private val random: () -> Random = { ThreadLocalRandom.current() }
) : IntLottery {
    private val probabilities = DoubleArray(weights.size) { 1.0 }
    private val aliases = IntArray(weights.size) { random().nextInt(weights.size) }

    init {
        val sumOfWeights = weights.sum()
        val itemsWithIndex = weights
            .withIndex()
            .map {
                it.value.validate()
                IndexedValue(it.index, it.value * weights.size / sumOfWeights)
            }
        val bigger = mutableListOf<IndexedValue<Double>>()
        val smaller = mutableListOf<IndexedValue<Double>>()
        for (item in itemsWithIndex) {
            putItemInCorrectBag(item, bigger, smaller)
        }
        while (smaller.isNotEmpty()) {
            val pickSmaller = smaller.removeAt(0)
            val pickBigger = bigger.removeAt(0)
            val delta = pickBigger.value - (1.0 - pickSmaller.value)
            probabilities[pickSmaller.index] = pickSmaller.value
            aliases[pickSmaller.index] = pickBigger.index
            putItemInCorrectBag(IndexedValue(pickBigger.index, delta), bigger, smaller)
        }
    }

    private fun putItemInCorrectBag(
        item: IndexedValue<Double>,
        bigger: MutableList<IndexedValue<Double>>,
        smaller: MutableList<IndexedValue<Double>>
    ) {
        when {
            item.value.compareTo(1.000000) < 0 -> smaller.add(item)
            item.value.compareTo(1.000001) > 0 -> bigger.add(item)
            else -> probabilities[item.index] = 1.0
        }
    }

    override fun draw(): Int {
        if (weights.isEmpty()) {
            throw NoSuchElementException()
        }
        val index = random().nextInt(weights.size)
        val prob = random().nextDouble()
        return if (prob < probabilities[index]) {
            index
        } else {
            aliases[index]
        }
    }

    override fun empty(): Boolean {
        return weights.isEmpty()
    }

    override fun remaining(): Int {
        return weights.size
    }

    private fun Double.validate(): Double {
        if (isNaN() || this < 0.0) {
            throw IllegalArgumentException("$weights contains invalid weight: $this")
        }
        return this
    }
}