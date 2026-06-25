package com.peknight.cats.demo.monad

import cats.Monad
import cats.syntax.applicative.*
import org.scalatest.flatspec.AnyFlatSpec

class OptionFlatSpec extends AnyFlatSpec:
  "Option" should "pass" in {
    def parseInt(str: String): Option[Int] = scala.util.Try(str.toInt).toOption

    def divide(a: Int, b: Int): Option[Int] = if b == 0 then None else Some(a / b)

    def stringDivideBy(aStr: String, bStr: String): Option[Int] =
      for
        aNum <- parseInt(aStr)
        bNum <- parseInt(bStr)
        ans <- divide(aNum, bNum)
      yield ans

    val opt1 = Monad[Option].pure(3)
    val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
    val opt3 = Monad[Option].map(opt2)(a => 100 * a)
    1.pure[Option]

    assert(stringDivideBy("6", "2") === Some(3))
    assert(stringDivideBy("6", "0") === None)
    assert(stringDivideBy("6", "foo") === None)
    assert(stringDivideBy("bar", "2") === None)
    assert(opt1 === Some(3))
    assert(opt2 === Some(5))
    assert(opt3 === Some(500))
  }
end OptionFlatSpec
