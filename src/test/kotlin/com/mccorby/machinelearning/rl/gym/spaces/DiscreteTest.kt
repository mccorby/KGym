package com.mccorby.machinelearning.rl.gym.spaces

import org.junit.Test

class DiscreteTest {

    @Test
    fun `Given a Discrete object when sampled it returns an integer smaller than value`() {
        val value = 5
        val discrete = Discrete(value)
        val result = discrete.sample()

        assert(result < value)
    }

    @Test
    fun `Given a Discrete object when a list of integers smaller than value then contains returns true`() {
        val value = 5
        val discrete = Discrete(value)
        val listOfInts = listOf(1, 2, 3, 4, 3, 2)

        val result = discrete.contains(listOfInts)

        assert(result)
    }

    @Test
    fun `Given a Discrete object when a list of integers not all with a smaller than value then contains returns false`() {
        val value = 5
        val discrete = Discrete(value)
        val listOfInts = listOf(1, 2, 3, 4, 3, 2, 8)

        val result = discrete.contains(listOfInts)

        assert(!result)
    }
}