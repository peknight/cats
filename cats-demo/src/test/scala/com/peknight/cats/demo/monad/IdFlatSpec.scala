package com.peknight.cats.demo.monad

import org.scalatest.flatspec.AnyFlatSpec
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Id, Monad}

class IdFlatSpec extends AnyFlatSpec:
  "Id" should "pass" in {
    val strId: Id[String] = "Dave"
    val intId: Id[Int] = 123
    // 还能这样写。。。
    val listId = List(1, 2, 3): Id[List[Int]]

    val a = Monad[Id].pure(3)
    val b = Monad[Id].flatMap(a)(_ * 1)

    val addAns =
      for
        x <- a
        y <- b
      yield x + y
    assert(addAns === 6)
  }
end IdFlatSpec
