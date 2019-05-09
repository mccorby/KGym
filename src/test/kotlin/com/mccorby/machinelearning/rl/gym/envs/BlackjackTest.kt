package com.mccorby.machinelearning.rl.gym.envs

import com.mccorby.machinelearning.rl.gym.core.ActionStep
import org.junit.Assert.assertFalse
import org.junit.Test

class BlackjackTest {

    @Test
    fun `Given the env is reset then it returns a valid Observation`() {
        val cut = Blackjack()

        val obs = cut.reset() as BlackjackObservation

        assert(obs.playerHand > 0)
        assert(obs.dealerHand > 0)
    }

    @Test
    fun `Given an action that busts the match for the player when step is executed it returns the corresponding ActionStep`() {
        val playerInit = listOf(10, 10, 1) // Taking one more card will bust
        val dealerInit = listOf(0)
        val expected = BlackjackObservation(22, 0, false)

        val cut = Blackjack(playerInit = playerInit, dealerInit = dealerInit)
        val actionStep = cut.step(1)

        assert(actionStep.done)
        assert(actionStep.reward == -1F)
        assert(expected == actionStep.observation)
    }

    @Test
    fun `Given the player hits when step is executed it returns the corresponding ActionStep`() {
        val playerInit = listOf(10)
        val dealerInit = listOf(0)
        val expected = BlackjackObservation(22, 0, false)

        val cut = Blackjack(playerInit = playerInit, dealerInit = dealerInit)
        val actionStep = cut.step(1)

        assertFalse(actionStep.done)
        assert(actionStep.reward == 0F)
        assert(expected.playerHand > (actionStep.observation as BlackjackObservation).playerHand)
    }

    @Test
    fun `Given an action for the dealer (stick) when step is executed it returns the corresponding ActionStep`() {
        val playerInit = listOf(0)
        val dealerInit = listOf(1, 1)

        val cut = Blackjack(playerInit, dealerInit)
        val actionStep = cut.step(0)

        assert(actionStep.done)
        // Note that rewards might vary depending on how the game went
    }

    @Test
    fun `Given playing natural and player has one then when step is executed it returns a reward of 1dot5`() {
        val playerInit = listOf(1, 10)
        val dealerInit = listOf(0)
        val expectedObs = BlackjackObservation(11, 0, false)
        val expected = ActionStep(expectedObs, 1.5F, true)

        val cut = Blackjack(playerInit, dealerInit, true)
        val actionStep = cut.step(0)

        assert(actionStep.done)
        assert(expected.reward == actionStep.reward)
    }
}