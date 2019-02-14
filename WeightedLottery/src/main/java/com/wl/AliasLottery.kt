package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom

private const val ONE = 1.000000
private const val ZERO = 0.0

class AliasLottery(
    private val weights: DoubleArray,
    private val random: () -> Random = { ThreadLocalRandom.current() }
) : IntLottery {
    private val probabilities = DoubleArray(weights.size) { ONE }
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
        while (smaller.isNotEmpty() && bigger.isNotEmpty()) {
            val pickSmaller = smaller.removeAt(0)
            val pickBigger = bigger.removeAt(0)
            val delta = pickBigger.value - (ONE - pickSmaller.value)
            probabilities[pickSmaller.index] = pickSmaller.value
            aliases[pickSmaller.index] = pickBigger.index
            putItemInCorrectBag(IndexedValue(pickBigger.index, delta), bigger, smaller)
        }
        smaller.forEach { probabilities[it.index] = ONE }
        bigger.forEach { probabilities[it.index] = ONE }
    }

    private fun putItemInCorrectBag(
        item: IndexedValue<Double>,
        bigger: MutableList<IndexedValue<Double>>,
        smaller: MutableList<IndexedValue<Double>>
    ) {

        when {
            item.value.compareTo(ONE) < 0 -> smaller.add(item)
            item.value.compareTo(ONE) > 0 -> bigger.add(item)
            else -> probabilities[item.index] = ONE
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
        if (isNaN() || this < ZERO) {
            throw IllegalArgumentException("$weights contains invalid weight: $this")
        }
        return this
    }
}