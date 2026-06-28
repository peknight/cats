package com.peknight.cats.demo.foldable

import cats.syntax.foldable.*
import cats.syntax.semigroup.*
import cats.{Eval, Foldable, Monoid}
import org.scalatest.flatspec.AnyFlatSpec

class FoldableFlatSpec extends AnyFlatSpec:

  def sumWithFoldable[F[_] : Foldable](values: F[Int]): Int = values.foldLeft(0)(_ + _)

  "Foldable" should "pass for List" in {
    def show[A](list: List[A]): String = list.foldLeft("nil")((accum, item) => s"$item then $accum")
    def map[A, B](list: List[A])(func: A => B): List[B] = list.foldRight(List.empty[B])(func(_) :: _)
    def flatMap[A, B](list: List[A])(func: A => List[B]): List[B] = list.foldRight(List.empty[B])(func(_) ::: _)
    def filter[A](list: List[A])(func: A => Boolean): List[A] = list.foldRight(List.empty[A])(
      (item, accum) => if func(item) then item :: accum else accum
    )
    def sum[A](list: List[A])(using m: Monoid[A]) = list.foldRight(m.empty)(_ |+| _)
    def sumWithNumeric[A](list: List[A])(using numeric: Numeric[A]): A = list.foldRight(numeric.zero)(numeric.plus)

    val bigDataRange = 1 to 100000
    def bigData = bigDataRange.to(LazyList)
    val bigDataSum = 5000050000L

    val ints = List(1, 2, 3)
    val eval: Eval[Long] = Foldable[LazyList].foldRight(bigData, Eval.now(0L))((num, eval) => eval.map(_ + num))

    assert(show(Nil) === "nil")
    assert(show(List(1, 2, 3)) === "3 then 2 then 1 then nil")
    //noinspection SimplifiableFoldOrReduce
    assert(List(1, 2, 3).foldLeft(0)(_ + _) === 6)
    //noinspection SimplifiableFoldOrReduce
    assert(List(1, 2, 3).foldRight(0)(_ + _) === 6)
    assert(List(1, 2, 3).foldLeft(0)(_ - _) === -6)
    assert(List(1, 2, 3).foldRight(0)(_ - _) === 2)
    assert(List(1, 2, 3).foldLeft(List.empty[Int])((accum, ele) => ele :: accum) === List(3, 2, 1))
    assert(List(1, 2, 3).foldRight(List.empty[Int])(_ :: _) === List(1, 2, 3))
    assert(map(List(1, 2, 3))(i => s"$i!") === List("1!", "2!", "3!"))
    assert(flatMap(List(1, 2, 3))(i => List(i - 1, i + 1)) === List(0, 2, 1, 3, 2, 4))
    assert(filter(List(1, 2, 3))(_ % 2 == 0) === List(2))
    assert(sum(List(1, 2, 3)) === 6)
    assert(sumWithNumeric(List(1, 2, 3)) === 6)
    assert(Foldable[List].foldLeft(ints, 0)(_ + _) === 6)
    // 不会有StackOverflow问题
    assert(bigData.foldRight(0L)(_ + _) === bigDataSum)
    assert(eval.value === bigDataSum)
    assert(bigDataRange.toList.foldRight(0L)(_ + _) === bigDataSum)
    assert(bigDataRange.toVector.foldRight(0L)(_ + _) === bigDataSum)
    assert(Foldable[List].find(List(1, 2, 3))(_ % 2 == 0) === Some(2))
    assert(Foldable[List].combineAll(List(1, 2, 3)) === 6)
    assert(Foldable[List].foldMap(List(1, 2, 3))(_.toString) === "123")
    assert(Foldable[List].compose(using Foldable[Vector]).combineAll(List(Vector(1, 2, 3), Vector(4, 5, 6))) === 21)
    assert(List(1, 2, 3).combineAll === 6)
    assert(List(1, 2, 3).foldMap(_.toString) === "123")
    assert(List(1, 2, 3).sum === 6)
  }

  "Foldable" should "pass for Option" in {
    val maybeInt = Option(123)
    assert(Foldable[Option].foldLeft(maybeInt, 10)(_ * _) === 1230)
    assert(Foldable[Option].nonEmpty(Option(42)))
    assert(Foldable[Option].combineAll(Option.empty[Int]) === 0)
  }
end FoldableFlatSpec
