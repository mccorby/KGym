package com.mccorby.machinelearning.rl.examples.modelfree_mc

import com.mccorby.machinelearning.rl.gym.core.Action
import com.mccorby.machinelearning.rl.gym.core.Observation
import com.mccorby.machinelearning.rl.gym.core.Policy
import com.mccorby.machinelearning.rl.gym.envs.Blackjack
import com.mccorby.machinelearning.rl.gym.envs.BlackjackObservation
import kotlin.math.pow

/**
 *
 */
class BlackjackResolution(private val env: Blackjack, private val policy: Policy, private val discountFactor: Float = 1.0F) {
    // TODO this should be injected as a strategy
    // policy, env, num_episodes, discount_factor=1.0
    fun monteCarloPrediction(numEpisodes: Int): Map<Observation, Float> {
        val returnsSum = HashMap<Observation, Float>()
        val returnsCount = HashMap<Observation, Int>()
        val v = HashMap<Observation, Float>()

        (1..numEpisodes + 1).forEach { episodeIdx ->
            logEpisode(episodeIdx, numEpisodes)

            // Generate an episode. An episode is an array of (state, action, reward) tuples
            val episode = mutableListOf<Triple<Observation, Action, Float>>()
            var state = env.reset()

            (0..100).forEach time@{ t ->
                val action = policy.getAction(state)
                val actionStep = env.step(action)
                episode.add(Triple(state, action, actionStep.reward))
                if (actionStep.done) return@time

                state = actionStep.observation
            }

            // Find all states the we've visited in this episode
            // We convert each state to a tuple so that we can use it as a dict key
            // Calculate average return for this state over all sampled episodes

            sumRewards(episode).forEach {
                // TODO Work with maps and results
                val currentSum = returnsSum.getOrDefault(it.first, 0f)
                returnsSum[it.first] = (currentSum + it.second)
                val currentCount = returnsCount.getOrDefault(it.first, 0)
                returnsCount[it.first] = currentCount + 1
                v[it.first] = returnsSum[it.first]!!.div(returnsCount[state]!!)
            }

        }
        return v
    }

    private fun logEpisode(episode: Int, numEpisodes: Int) {
        if (episode.div(1000) == 0) {
            println("Episode $episode/$numEpisodes")
        }
    }

    fun sumRewards(episode: List<Triple<Observation, Action, Float>>): Set<Pair<Observation, Float>> {
        return episode.groupBy { triple -> triple.first }
            .map {Pair(it.key, sumRewardsForState(it.value))}.toSet()
    }

    fun sumRewardsForState(values: List<Triple<Observation, Action, Float>>): Float {
        return values.foldIndexed(0.0F, { idx, acc: Float, triple -> acc + triple.third * discountFactor.pow(idx + 1)})
    }
}

// TODO Replace for HOF
class SamplePolicy : Policy {
    // A policy that sticks if the player score is >= 20 and hits otherwise.
    override fun getAction(state: Observation): Action {
        return if ((state as BlackjackObservation).playerHand >= 0) 0
        else 1
    }
}

fun main() {
    val result = BlackjackResolution(Blackjack(), SamplePolicy()).monteCarloPrediction(10000)
    print(result.size)
}