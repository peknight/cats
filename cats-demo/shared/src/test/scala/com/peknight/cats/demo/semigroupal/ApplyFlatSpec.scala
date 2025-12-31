package com.peknight.cats.demo.semigroupal

import cats.syntax.apply.*
import cats.syntax.semigroup.*
import cats.{Monoid, Semigroupal}
import com.peknight.cats.demo.data.Cat
import org.scalatest.flatspec.AnyFlatSpec

class ApplyFlatSpec extends AnyFlatSpec:
  "Apply" should "pass for Option" in {
    val add: (Int, Int) => Int = (a, b) => a + b
    val tupleToCat: (String, Int, String, List[String]) => Cat = Cat.apply
    val catToTuple: Cat => (String, Int, String, List[String]) = cat => (cat.name, cat.age, cat.color, cat.favoriteFoods)
    given catMonoid: Monoid[Cat] = (Monoid[String], Monoid[Int], Monoid[String], Monoid[List[String]]).imapN(tupleToCat)(catToTuple)
    assert((Option(123), Option("abc")).tupled === Some((123, "abc")))
    assert((Option(123), Option("abc"), Option(true)).tupled === Some((123, "abc", true)))
    assert((
      Option(Cat.garfield.name),
      Option(Cat.garfield.age),
      Option(Cat.garfield.color),
      Option(Cat.garfield.favoriteFoods)
    ).mapN(Cat.apply) === Some(Cat.garfield))
    assert((Cat.garfield |+| Cat.heathcliff) === Cat(
      s"${Cat.garfield.name}${Cat.heathcliff.name}",
      Cat.garfield.age + Cat.heathcliff.age,
      s"${Cat.garfield.color}${Cat.heathcliff.color}",
      Cat.garfield.favoriteFoods ::: Cat.heathcliff.favoriteFoods
    ))
  }

  "Apply" should "pass for List" in {
    assert(Semigroupal[List].product(List(1, 2), List(3, 4)) === List((1, 3), (1, 4), (2, 3), (2, 4)))
    assert((List(1, 2), List(3, 4)).tupled === List((1, 3), (1, 4), (2, 3), (2, 4)))
  }
end ApplyFlatSpec
