package com.mccorby.machinelearning.rl.examples.modelfree_mc

import com.mccorby.machinelearning.rl.gym.core.Action
import com.mccorby.machinelearning.rl.gym.core.Observation
import com.mccorby.machinelearning.rl.gym.core.Policy
import com.mccorby.machinelearning.rl.gym.envs.Blackjack
import com.mccorby.machinelearning.rl.gym.envs.BlackjackObservation
import koma.*
import kotlin.collections.set
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

            var i = 0
            do {
                val action = policy.getAction(state)
                val actionStep = env.step(action)
                episode.add(Triple(state, action, actionStep.reward))
                if (!actionStep.done) state = actionStep.observation
            } while (i++ < 100 && !actionStep.done)

            // Find all states the we've visited in this episode
            // Calculate average return for this state over all sampled episodes
//            for state in states_in_episode:
//            # Find the first occurance of the state in the episode
//                first_occurence_idx = next(i for i,x in enumerate(episode) if x[0] == state)
//            # Sum up all rewards since the first occurance
//                G = sum([x[2]*(discount_factor**i) for i,x in enumerate(episode[first_occurence_idx:])])
//            # Calculate average return for this state over all sampled episodes
//                returns_sum[state] += G
//            returns_count[state] += 1.0
//            V[state] = returns_sum[state] / returns_count[state]
            sumRewards(episode).forEach {
                // TODO Work with maps and results
                val currentSum = returnsSum.getOrDefault(it.first, 0f)
                returnsSum[it.first] = (currentSum + it.second)
                val currentCount = returnsCount.getOrDefault(it.first, 0)
                returnsCount[it.first] = currentCount + 1
                val returnCountState = returnsCount.getOrDefault(state, 1)
                v[it.first] = returnsSum[it.first]!!.div(returnCountState)
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
        if (values.size != 1) {
            println(values.first())
            println(values.size)
        }
        return values.foldIndexed(0.0F, { idx, acc: Float, triple -> acc + triple.third * discountFactor.pow(idx + 1)})
    }
}

// TODO Replace for HOF
class SamplePolicy : Policy {
    // A policy that sticks if the player score is >= 20 and hits otherwise.
    override fun getAction(state: Observation): Action {
        return if ((state as BlackjackObservation).playerHand >= 20) 0
        else 1
    }
}

fun main() {
    val result = BlackjackResolution(Blackjack(), SamplePolicy(), discountFactor = 1.0f).monteCarloPrediction(10000)
    result.forEach { println(it) }
    showPlots(result)
}

fun showPlots(result: Map<Observation, Float>) {
    val playerHands =
        result.keys.asSequence().map { it as BlackjackObservation }.map { it.playerHand.toDouble() }.toList()
            .toDoubleArray()
    val dealerHands =
        result.keys.asSequence().map { it as BlackjackObservation }.map { it.dealerHand.toDouble() }.toList()
            .toDoubleArray()
    val rewards = result.values.asSequence().map { it.toDouble() }.toList().toDoubleArray()
    figure(1)
    plot(playerHands, 'b', "Player")
    plot(dealerHands, 'y', "Dealer")
    plot(rewards, 'r', "Rewards")
    xlabel("Time (Episodes)")
    ylabel("Magnitude")
    title("Blackjack Episodes")
}
