package com.mccorby.machinelearning.rl.gym.core

data class ActionStep(val observation: Observation, val reward: Float, val done: Boolean, val info: List<Any> = listOf())
