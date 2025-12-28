package com.peknight.cats.demo.monad

import cats.data.State
import cats.syntax.applicative.*
import org.scalatest.flatspec.AnyFlatSpec

class PostOrderCalculatorFlatSpec extends AnyFlatSpec:
  "Post order calculator" should "pass" in {
    type CalcState[A] = State[List[Int], A]

    def evalOne(sym: String): CalcState[Int] = sym match
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    def operator(func: (Int, Int) => Int): CalcState[Int] = State[List[Int], Int] {
      case value1 :: value2 :: tail =>
        val ans = func(value1, value2)
        (ans :: tail, ans)
      case _ => sys.error("Fail!")
    }
    def operand(num: Int): CalcState[Int] = State[List[Int], Int] { stack => (num :: stack, num) }
    def evalAll(input: List[String]): CalcState[Int] = input.foldLeft(0.pure[CalcState]) { (a, b) =>
      a.flatMap(_ => evalOne(b))
    }
    def evalInput(input: String): Int = evalAll(input.split(" ").toList).runA(Nil).value

    val program =
      for
        _ <- evalOne("1")
        _ <- evalOne("2")
        ans <- evalOne("+")
      yield ans

    val multistageProgram = evalAll(List("1", "2", "+", "3", "*"))

    val biggerProgram =
      for
        _ <- evalAll(List("1", "2", "+"))
        _ <- evalAll(List("3", "4", "+"))
        ans <- evalOne("*")
      yield ans

    assert(evalOne("42").runA(Nil).value === 42)
    assert(program.runA(Nil).value === 3)
    assert(multistageProgram.runA(Nil).value === 9)
    assert(biggerProgram.runA(Nil).value === 21)
    assert(evalInput("1 2 + 3 4 + *") === 21)
  }
end PostOrderCalculatorFlatSpec
