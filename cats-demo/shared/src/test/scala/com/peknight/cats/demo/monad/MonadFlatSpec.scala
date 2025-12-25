package com.peknight.cats.demo.monad

import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.monad.*
import cats.{Id, Monad}
import com.peknight.cats.data.Tree.*
import org.scalatest.flatspec.AnyFlatSpec

class MonadFlatSpec extends AnyFlatSpec:

  "Monad" should "pass" in {
    def sumSquare[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
      for
        x <- a
        y <- b
      yield x * x + y * y

    def retry[F[_] : Monad, A](start: A)(f: A => F[A]): F[A] =
      f(start).flatMap(a => retry(a)(f))

    def retryTailRecM[F[_] : Monad, A](start: A)(f: A => F[A]): F[A] =
      Monad[F].tailRecM(start)(a => f(a).map(a2 => Left(a2)))

    def retryM[F[_] : Monad, A](start: A)(f: A => F[A]): F[A] = start.iterateWhileM(f)(_ => true)

    val treeFor =
      for
        a <- branch(leaf(100), leaf(200))
        b <- branch(leaf(a - 10), leaf(a + 10))
        c <- branch(leaf(b - 1), leaf(b + 1))
      yield c

    assert(sumSquare(Option(3), Option(4)) === Some(25))
    assert(sumSquare(List(1, 2, 3), List(4, 5)) === List(17, 26, 20, 29, 25, 34))
    assert(sumSquare(3: Id[Int], 4: Id[Int]) === 25)
    assert(retry(100)(a => if a == 0 then Option.empty[Int] else Option(a - 1)) === None)
    assert(retryTailRecM(100000)(a => if a == 0 then Option.empty[Int] else Option(a - 1)) === None)
    assert(retryM(100000)(a => if a == 0 then Option.empty[Int] else Option(a - 1)) === None)
    assert(branch(leaf(100), leaf(200)).flatMap(x => branch(leaf(x - 1), leaf(x + 1))) ===
      branch(branch(leaf(99), leaf(101)), branch(leaf(199), leaf(201))))
    assert(treeFor === branch(
      branch(branch(leaf(89), leaf(91)), branch(leaf(109), leaf(111))),
      branch(branch(leaf(189), leaf(191)), branch(leaf(209), leaf(211)))
    ))
  }
end MonadFlatSpec
