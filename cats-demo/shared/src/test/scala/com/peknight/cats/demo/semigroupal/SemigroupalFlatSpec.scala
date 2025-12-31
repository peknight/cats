package com.peknight.cats.demo.semigroupal

import cats.Semigroupal
import cats.syntax.either.*
import org.scalatest.flatspec.AnyFlatSpec

class SemigroupalFlatSpec extends AnyFlatSpec:
  "Semigroupal" should "pass" in {
    def parseInt(str: String): Either[String, Int] =
      Either.catchOnly[NumberFormatException](str.toInt).leftMap(_ => s"Couldn't read $str")

    val eitherRes =
      for
        a <- parseInt("a")
        b <- parseInt("b")
        c <- parseInt("c")
      yield (a + b + c)
    assert(eitherRes === Left("Couldn't read a"))
    assert(Semigroupal[Option].product(Some(123), Some("abc")) === Some((123, "abc")))
    assert(Semigroupal[Option].product(None, Some("abc")).isEmpty)
    assert(Semigroupal[Option].product(Some("abc"), None).isEmpty)
    // Joining Three or More Contexts
    assert(Semigroupal.tuple3(Option(1), Option(2), Option(3)) === Some((1, 2, 3)))
    assert(Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int]).isEmpty)
    assert(Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _) === Some(6))
    assert(Semigroupal.map2(Option(1), Option.empty[Int])(_ + _).isEmpty)
  }
end SemigroupalFlatSpec
