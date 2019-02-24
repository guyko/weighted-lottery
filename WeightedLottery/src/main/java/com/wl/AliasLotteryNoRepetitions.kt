package com.wl

import java.util.*
import java.util.concurrent.ThreadLocalRandom

private const val ONE = 1.000000
private const val ZERO = 0.0


class AliasLotteryNoRepetitions(private val weights: DoubleArray,
                                private val random: () -> ThreadLocalRandom = { ThreadLocalRandom.current() }) : IntLottery {

    private val sumOfWeights = weights.sum()
    private val almostSumOfWeights = 0.999 * sumOfWeights
    private var draws = 0
    private var selected = 0
    private var selectedSum = 0.0
    private val selectedArr = BooleanArray(weights.size)
    private val cells = weights.asSequence()
            .mapIndexed { index, w ->
                val weight = weights[index].validate()
                Cell(index, weight * weights.size / sumOfWeights)
            }
            .toList()
            .toTypedArray()

    private var weightsIter: Iterator<Int>? = null


    private var cellsLeft = cells.size

    init {
        val over = mutableListOf<Cell>()
        val under = mutableListOf<Cell>()

        cells.forEach {
            val main = it.main()!!
            if (main.prob > ONE) {
                over.add(it)
            } else if (main.prob < ONE) {
                under.add(it)
            }
        }

        while (over.isNotEmpty() && under.isNotEmpty()) {
            val overCell = over.removeAt(over.size - 1)
            val underCell = under.removeAt(under.size - 1)
            val overCellMain = overCell.main()!!
            val underCellMain = underCell.main()!!
            val shifted = ONE - underCellMain.prob

            overCellMain.prob -= shifted
            underCell.with(overCellMain.idx, ONE - underCellMain.prob)


            if (overCellMain.prob < ONE) {
                under.add(overCell)
            } else if (overCellMain.prob > ONE) {
                over.add(overCell)
            }
        }
    }

    override fun draw() = draw(0)

    fun draw(d: Int): Int {
        ++draws
        if (selected == weights.size) {
            throw NoSuchElementException()
        }
        if (selectedSum >= almostSumOfWeights) {
            initWeightsIterator()
            ++selected
            return weightsIter!!.next()
        }
        val index = random().nextInt(cellsLeft)
        val prob = random().nextDouble()
        val cell = cells[index]
        val cellPart = cell.popFollowingDraw(prob) ?: return draw(d + 1)
        return if (selectedArr[cellPart.idx]) {
            reorder(index)
            draw(d + 1)
        } else {
            selectedArr[cellPart.idx] = true
            ++selected
            selectedSum += weights[cellPart.idx]
            reorder(index)
            cellPart.idx
        }
    }

    private fun initWeightsIterator() {
        if (weightsIter == null) {
            weightsIter = weights.asSequence()
                    .mapIndexed { i, w -> i to w }
                    .filter { !selectedArr[it.first] }
                    .sortedByDescending { it.second }
                    .map { it.first }
                    .iterator()
        }
    }


    private fun reorder(idx: Int) {
        if (cellsLeft == 1) {
            return
        }
        val cell = cells[idx]
        val lastCell = cells[cellsLeft - 1]

        if (idx == cellsLeft - 1) {
            // noop
        } else if (cell.empty()) {
            swapWithLast(idx)
            removeLastCell()
            reorder(idx)
        } else if (lastCell.full()) {
            swapWithLast(idx)
        } else {
            val shiftedPart = lastCell.pop()!!
            val cellMainPart = cell.main()!!
            if (cellMainPart.prob + shiftedPart.prob > ONE) {
                cell.with(shiftedPart.idx, ONE - cellMainPart.prob)
                lastCell.with(shiftedPart.idx, cellMainPart.prob + shiftedPart.prob - ONE)
            } else {
                cell.push(shiftedPart)
            }
        }

        removeLastCell()
    }

    private fun removeLastCell() {
        if (cells[cellsLeft - 1].empty()) {
            --cellsLeft
        }
    }

    private fun swapWithLast(idx: Int) {
        val tmp = cells[idx]
        cells[idx] = cells[cellsLeft - 1]
        cells[cellsLeft - 1] = tmp
    }

    override fun empty() = remaining() == 0

    override fun remaining() = weights.size - selected

    private fun Double.validate(): Double {
        if (isNaN() || this < ZERO) {
            throw IllegalArgumentException("$weights contains invalid weight: $this")
        }
        return this
    }


    private class Cell(idx: Int, prob: Double) {
        private val parts = Array(2) { if (it == 0) CellPart(idx, prob) else null }
        private var size = 1

        fun full() = size == 2
        fun empty() = size == 0
        fun main() = parts[0]

        fun with(idx: Int, prob: Double) = push(CellPart(idx, prob))

        fun push(part: CellPart) {
            parts[size] = part
            ++size
        }

        fun pop(): CellPart? {
            val ret = parts[0]
            parts[0] = parts[1]
            parts[1] = null
            if (ret != null) {
                --size
            }
            return ret
        }

        fun popFollowingDraw(p: Double): CellPart? {
            val main = parts[0]
            val alias = parts[1]

            if (main != null && p < main.prob) {
                parts[0] = alias
                parts[1] = null
                --size
                return main
            }
            if (alias != null && ONE - p < alias.prob) {
                parts[1] = null
                --size
                return alias
            }
            return null
        }

        override fun toString(): String {
            return "main=${parts[0]},alias=${parts[1]}"
        }
    }

    private data class CellPart(val idx: Int, var prob: Double)
}