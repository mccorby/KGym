package com.mccorby.machinelearning.rl.gym.spaces

/**
    A tuple (i.e., product) of simpler spaces

    Example usage:
    self.observation_space = spaces.Tuple((spaces.Discrete(2), spaces.Discrete(3)))
 */
class Tuple(private val spaces: List<Space<*>>): Space<List<*>>() {

    override fun sample(): List<*> {
        return spaces.map {
            space -> space.sample()
        }
    }

    override fun contains(x: List<List<*>>): Boolean {
        return true
    }
}