package com.peknight.cats.demo.monad

import cats.syntax.either.*
import org.scalatest.flatspec.AnyFlatSpec

class EitherFlatSpec extends AnyFlatSpec:

  "Either" should "pass" in {
    type Result[A] = Either[Throwable, A]

    def countPositive(nums: List[Int]) = nums.foldLeft(0.asRight[String]) { (accumulator, num) =>
      if num > 0 then accumulator.map(_ + 1)
      else Left("Negative. Stopping!")
    }

    val either1: Either[String, Int] = Right(10)
    val either2: Either[String, Int] = Right(32)
    for
      a <- either1
      b <- either2
    yield a + b

    val either3 = 3.asRight[String]
    val either4 = 4.asRight[String]
    val eitherAns =
      for
        x <- either3
        y <- either4
      yield x * x + y * y

    val divAns =
      for
        a <- 1.asRight[String]
        b <- 0.asRight[String]
        c <- if b == 0 then "DIV0".asLeft[Int] else (a / b).asRight[String]
      yield c * 100

    assert(eitherAns === Right(25))
    assert(countPositive(List(1, 2, 3)) === Right(3))
    assert(countPositive(List(1, -2, 3)).isLeft)
    assert(Either.catchOnly[NumberFormatException]("foo".toInt).isLeft)
    assert(Either.catchNonFatal(sys.error("Badness")).isLeft)
    assert(Either.fromTry(scala.util.Try("foo".toInt)).isLeft)
    assert(Either.fromOption[String, Int](None, "Badness").isLeft)
    assert("Error".asLeft[Int].getOrElse(0) === 0)
    assert("Error".asLeft[Int].orElse(2.asRight[String]) === Right(2))
    assert(-1.asRight[String].ensure("Must be non-negative!")(_ > 0).isLeft)
    assert("error".asLeft[Int].recover { case _: String => -1 } === Right(-1))
    assert("error".asLeft[Int].recoverWith { case _: String => Right(-1) } === Right(-1))
    assert("foo".asLeft[Int].leftMap(_.reverse) === Left("oof"))
    assert(6.asRight[String].bimap(_.reverse, _ * 7) === Right(42))
    assert("bar".asLeft[Int].bimap(_.reverse, _ * 7) === Left("rab"))
    assert(123.asRight[String] === Right(123))
    assert(123.asRight[String].swap === Left(123))
    assert(divAns === Left("DIV0"))
  }
end EitherFlatSpec
