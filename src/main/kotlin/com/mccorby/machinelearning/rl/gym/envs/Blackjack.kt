package com.mccorby.machinelearning.rl.gym.envs

import com.mccorby.machinelearning.rl.gym.core.*
import com.mccorby.machinelearning.rl.gym.spaces.Discrete
import kotlin.random.Random

private const val STICK = 0
private const val HIT = 1
/**
 * @param playerInit Starts this game with a known state for the player
 * @param dealerInit Starts this game with a known state for the dealer
 */
class Blackjack(
    private val playerInit: List<Int> = listOf(),
    private val dealerInit: List<Int> = listOf(),
    private val isNaturalEnabled: Boolean = false
) : Env<Int> {

    private var player: MutableList<Int>
    private var dealer: MutableList<Int>
    private val deck = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10)
    private lateinit var random: Random
    private val actionSpace = Discrete(2)
    // Not used in this example
    private val observationSpace = listOf(Discrete(32), Discrete(11), Discrete(2))
    private var reward: Float = 0F
    private var done = false

    init {
        player = playerInit.toMutableList()
        dealer = dealerInit.toMutableList()
        seed()
    }

    override fun step(action: Int): ActionStep {
        assert(actionSpace.contains(listOf(action)))
        when (action) {
            STICK -> {
                done = true
                while (dealer.sum() < 17) {
                    dealer.add(drawCard())
                }
                reward = score(player).compareTo(score(dealer)).toFloat()
                if (isNaturalEnabled && isNatural(player) && reward == 1F) {
                    reward = 1.5F
                }
            }
            HIT -> {
                player.add(drawCard())
                if (isBust(player)) {
                    reward = -1F
                    done = true
                } else {
                    reward = 0F
                    done = false
                }
            }
        }
        return ActionStep(createObservation(), reward, done)
    }

    override fun reset(): Observation {
        dealer = drawHand()
        player = drawHand()
        return createObservation()
    }

    override fun render(mode: RenderMode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seed(seed: Int): List<Int> {
        random = Random(seed)
        return listOf(seed)
    }

    private fun isNatural(hand: List<Int>): Boolean {
        return hand.size == 2 && hand.containsAll(listOf(1, 10))
    }

    // What is the score of this hand (0 if bust)
    private fun score(hand: List<Int>): Int {
        if (isBust(hand)) return 0
        return hand.sum()
    }

    private fun isBust(hand: List<Int>) = hand.sum() > 21

    private fun createObservation() = BlackjackObservation(sumHand(player), dealer[0], hasUsableAce(player))

    private fun drawHand(): MutableList<Int> {
        return mutableListOf(drawCard(), drawCard())
    }

    private fun drawCard() = deck[random.nextInt(deck.size)]

    // Return current total
    private fun sumHand(hand: List<Int>): Int {
        return if (hasUsableAce(hand)) {
            hand.sum() + 10
        } else {
            hand.sum()
        }
    }

    private fun hasUsableAce(hand: List<Int>): Boolean {
        return 1 in hand && hand.sum() + 10 <= 21
    }
}

data class BlackjackObservation(val playerHand: Int, val dealerHand: Int, val usableAce: Boolean) : Observation
