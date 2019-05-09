package com.mccorby.machinelearning.rl.gym.spaces

import kotlin.random.Random

/**
    Defines the observation and action spaces, so you can write generic
    code that applies to any Env. For example, you can choose a random
    action.
 */
abstract class Space<T> {

    var randomState: Random = Random.Default

    /**
     * Uniformly randomly sample a random element of this space.
     */
    abstract fun sample(): T

    /**
     * Return boolean specifying if x is a valid member of this space
     */
    abstract fun contains(x: List<T>): Boolean

    /**
     * Seed the PRNG of this space.
     */
    fun seed(seed: Int) {
        randomState = Random(seed)
    }
}
