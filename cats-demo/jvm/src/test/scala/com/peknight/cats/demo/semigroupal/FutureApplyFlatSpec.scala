package com.peknight.cats.demo.semigroupal

import cats.Semigroupal
import cats.syntax.apply.*
import com.peknight.cats.demo.data.Cat
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FutureApplyFlatSpec extends AnyFlatSpec:
  "Future Apply" should "pass for pair" in {
    Semigroupal[Future].product(Future("Hello"), Future(123)).map(i => assert(i === ("Hello", 123)))
  }

  "Future Apply" should "pass for cat" in {
    (
      Future(Cat.garfield.name),
      Future(Cat.garfield.age),
      Future(Cat.garfield.color),
      Future(Cat.garfield.favoriteFoods)
    ).mapN(Cat.apply).map(i => assert(i === Cat.garfield))
  }

end FutureApplyFlatSpec

