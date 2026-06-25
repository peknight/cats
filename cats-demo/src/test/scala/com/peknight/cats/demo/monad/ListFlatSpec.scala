package com.peknight.cats.demo.monad

import cats.Monad
import cats.syntax.applicative.*
import org.scalatest.flatspec.AnyFlatSpec

class ListFlatSpec extends AnyFlatSpec:
  "List" should "pass" in {
    val listFlatMap1 =
      for
        x <- (1 to 3).toList
        y <- (4 to 5).toList
      yield (x, y)

    val list1 = Monad[List].pure(3)
    val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
    val list3 = Monad[List].map(list2)(a => a + 123)
    1.pure[List]

    assert(listFlatMap1 === List((1,4), (1,5), (2,4), (2,5), (3,4), (3,5)))
    assert(list1 === List(3))
    assert(list2 === List(1, 10, 2, 20, 3, 30))
    assert(list3 === List(124, 133, 125, 143, 126, 153))
    assert(Monad[Vector].flatMap(Vector(1, 2, 3))(a => Vector(a, a * 10)) === Vector(1, 10, 2, 20, 3, 30))
  }
end ListFlatSpec
