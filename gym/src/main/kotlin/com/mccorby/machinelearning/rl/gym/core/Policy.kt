package com.mccorby.machinelearning.rl.gym.core

interface Policy {
    fun getAction(state: Observation): Action
}

class PolicyMap : Policy {
    private val internalMap = mutableMapOf<Observation, Action>()

    override fun getAction(state: Observation): Action = internalMap[state]!!
}