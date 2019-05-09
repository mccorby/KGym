package com.mccorby.machinelearning.rl.gym.core

interface Env<ACTION> {
    /**
     * ORIGINAL DESCRIPTION IN PYTHON GYM
     * Run one timestep of the environment's dynamics. When end of
        episode is reached, you are responsible for calling `reset()`
        to reset this environment's state.

        Accepts an action and returns a tuple (observation, reward, done, info).

        Args:
        action (object): an action provided by the environment

        Returns:
        observation (object): agent's observation of the current environment
        reward (float) : amount of reward returned after previous action
        done (boolean): whether the episode has ended, in which case further step() calls will return undefined results
        info (dict): contains auxiliary diagnostic information (helpful for debugging, and sometimes learning)
     */
    fun step(action: ACTION): ActionStep
    fun reset(): Observation
    fun render(mode: RenderMode = RenderMode.HUMAN)
    fun close()
    fun seed(seed: Int = 0): List<Int>
}

enum class RenderMode {
    HUMAN,
    RGB_ARRAY,
    ASCII
}