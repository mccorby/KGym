package com.mccorby.machinelearning.rl.gym.spaces

import com.mccorby.machinelearning.rl.gym.spaces.Discrete
import com.mccorby.machinelearning.rl.gym.spaces.Tuple
import org.junit.Test

class TupleTest {

    @Test
    fun `Given a list of list of items then contains returns true`() {
        val tupleValue = listOf(
            Discrete(1),
            Discrete(2),
            Discrete(3),
            Discrete(4)
        )
        val list1 = listOf(
            Discrete(1),
            Discrete(2),
            Discrete(3)
        )//        val list2 = listOf(
//            Discrete(2),
//            Discrete(3),
//            Discrete(4)
//        )


        val cut = Tuple(tupleValue)

        val result = cut.contains(listOf(list1))

        assert(result)
    }
}