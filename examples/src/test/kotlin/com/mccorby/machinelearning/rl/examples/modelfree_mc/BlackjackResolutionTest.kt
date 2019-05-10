package com.mccorby.machinelearning.rl.examples.modelfree_mc

import com.mccorby.machinelearning.rl.gym.core.Action
import com.mccorby.machinelearning.rl.gym.core.Observation
import com.mccorby.machinelearning.rl.gym.core.PolicyMap
import com.mccorby.machinelearning.rl.gym.envs.Blackjack
import com.mccorby.machinelearning.rl.gym.envs.BlackjackObservation
import org.junit.Assert.assertTrue
import org.junit.Test


class BlackjackResolutionTest {
    @Test
    fun `Given two ocurrences of a state visit when resolving it adds up the rewards`() {
        val o1 = BlackjackObservation(1, 1, false)
        val o2 = BlackjackObservation(2, 1, false)

        val t1 = Triple<Observation, Action, Float>(o1, 1, 1.0F)
        val t2 = Triple<Observation, Action, Float>(o2, 2, 1.0F)
        val t3 = Triple<Observation, Action, Float>(o1, 2, 1.0F)
        val t4 = Triple<Observation, Action, Float>(o1, 2, 1.0F)

        val episode = listOf(t1, t2, t3, t4)

        val expected = setOf(Pair(o1, 3F), Pair(o2, 1.0F))

        val cut = BlackjackResolution(Blackjack(), PolicyMap())

        val result = cut.sumRewards(episode)

        assertTrue(expected == result)
    }
}