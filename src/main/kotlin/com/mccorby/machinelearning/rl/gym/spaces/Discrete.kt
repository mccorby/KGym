package com.mccorby.machinelearning.rl.gym.spaces

/**
    A discrete space in :math:`\{ 0, 1, \dots, n-1 \}`.

    Example::

    >>> Discrete(2)
 */
class Discrete(private val value: Int) : Space<Int>() {

    override fun sample(): Int {
        return randomState.nextInt(value)
    }

    override fun contains(x: List<Int>): Boolean {
        return x.all { it in 0 until value }
    }
}