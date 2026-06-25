package com.peknight.cats.demo.functor

import cats.syntax.contravariant.*
import cats.syntax.functor.*
import cats.{Contravariant, Show}
import org.scalatest.flatspec.AnyFlatSpec

class ContravariantFlatSpec extends AnyFlatSpec:
  "Contravariant" should "pass for Show" in {
    val showString = Show[String]
    val showSymbol = Contravariant[Show].contramap(showString)((sym: Symbol) => s"'${sym.name}")
    assert(showSymbol.show(Symbol("dave")) === "'dave")
    assert(showString.contramap[Symbol](sym => s"'${sym.name}").show(Symbol("dave")) === "'dave")
  }

  "Contravariant" should "pass for Function" in {
    val func1 = (x: Int) => x.toDouble
    val func2 = (y: Double) => y * 2
    val func3 = func1.map(func2)

    val func3a: Int => Double = a => func2(func1(a))
    val func3b: Int => Double = func2.compose(func1)

    // won't compile
    // import cats.syntax.contravariant.*
    // val func3c: Int => Double = func2.contramap(func1)
    // type F[A] = A => Double

    type <=[B, A] = A => B
    type F[A] = Double <= A

    val func2b: Double <= Double = func2

    // (Double => Double).contramap(Int => Double)
    val func3c = func2b.contramap(func1)

    val func4: Boolean <= Double = _ > 0
    val func5 = func4.contramap(func1)

    assert(func3c(10) === 20.0)
    assert(func5(10))
  }
end ContravariantFlatSpec
