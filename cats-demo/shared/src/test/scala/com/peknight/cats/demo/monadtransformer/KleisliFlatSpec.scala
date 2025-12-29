package com.peknight.cats.demo.monadtransformer

import cats.data.Kleisli
import org.scalatest.flatspec.AnyFlatSpec

class KleisliFlatSpec extends AnyFlatSpec:
  "Kleisli" should "pass" in {
    val twice: Int => Int = x => x * 2
    val countCats: Int => String = x => if x == 1 then "1 cat" else s"$x cats"
    val twiceAsManyCats: Int => String = twice andThen countCats
    val parse: Kleisli[Option, String, Int] =
      Kleisli((s: String) => if s.matches("-?[0-9]+") then Some(s.toInt) else None)
    val reciprocal: Kleisli[Option, Int, Double] = Kleisli(i => if i != 0 then Some(1.0 / i) else None)
    val parseAndReciprocal: Kleisli[Option, String, Double] = reciprocal.compose(parse)
    assert(twiceAsManyCats(1) === "2 cats")
  }
end KleisliFlatSpec
