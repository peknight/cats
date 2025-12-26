package com.peknight.cats.demo.monad

import cats.data.State
import org.scalatest.flatspec.AnyFlatSpec

class StateFlatSpec extends AnyFlatSpec:
  "State" should "pass" in {
    val a = State[Int, String] { state =>
      (state, s"The state is $state")
    }
    val (state, result) = a.run(10).value
    val justTheState = a.runS(10).value
    val justTheResult = a.runA(10).value
    val step1 = State[Int, String] { num =>
      val ans = num + 1
      (ans, s"Result of step1: $ans")
    }
    val step2 = State[Int, String] { num =>
      val ans = num * 2
      (ans, s"Result of step2: $ans")
    }
    val both =
      for
        a <- step1
        b <- step2
      yield (a, b)
    val (stateBoth, resultBoth) = both.run(20).value
    val getDemo = State.get[Int]
    val setDemo = State.set[Int](30)
    val pureDemo = State.pure[Int, String]("Result")
    val inspectDemo = State.inspect[Int, String](x => s"${x}!")
    val modifyDemo = State.modify[Int](_ + 1)
    val program: State[Int, (Int, Int, Int)] =
      for
        a <- State.get[Int]
        _ <- State.set[Int](a + 1)
        b <- State.get[Int]
        _ <- State.modify[Int](_ + 1)
        c <- State.inspect[Int, Int](_ * 1000)
      yield (a, b, c)
    val (stateProgram, resultProgram) = program.run(1).value

    assert(state === 10)
    assert(result === "The state is 10")
    assert(justTheState === 10)
    assert(justTheResult === "The state is 10")
    assert(stateBoth === 42)
    assert(resultBoth === ("Result of step1: 21", "Result of step2: 42"))
    assert(getDemo.run(10).value === (10, 10))
    assert(setDemo.run(10).value === (30, ()))
    assert(pureDemo.run(10).value === (10, "Result"))
    assert(inspectDemo.run(10).value === (10, "10!"))
    assert(modifyDemo.run(10).value === (11, ()))
    assert(stateProgram === 3)
    assert(resultProgram === (1, 2, 3000))
  }
end StateFlatSpec
