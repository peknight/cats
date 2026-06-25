package com.peknight.cats.demo.monad

import cats.MonadError
import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.monadError.*
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.{Success, Try}

class MonadErrorFlatSpec extends AnyFlatSpec:
  "MonadError" should "pass" in {
    type ErrorOr[A] = Either[String, A]
    type ExceptionOr[A] = Either[Throwable, A]

    def validateAdult[F[_]](age: Int)(using me: MonadError[F, Throwable]): F[Int] =
      if age >= 18 then age.pure[F]
      else new IllegalArgumentException("Age must be greater than or equal to 18").raiseError[F, Int]

    val monadError = MonadError[ErrorOr, String]
    val success1 = monadError.pure(42)
    val success2 = 42.pure[ErrorOr]
    val failure1 = monadError.raiseError[String]("Badness")
    val failure2 = monadError.raiseError[Int]("Badness")
    val failure3 = "Badness".raiseError[ErrorOr, Int]
    val exn: Throwable = new RuntimeException("It's all gone wrong")

    assert(monadError.handleErrorWith(failure1) {
      case "Badness" => monadError.pure("It's ok")
      case _ => monadError.raiseError("It's not ok")
    } === Right("It's ok"))

    assert(monadError.handleError(failure2) {
      case "Badness" => 42
      case _ => -1
    } === Right(42))

    assert(monadError.ensure(success1)("Number too low!")(_ > 1000).isLeft)

    assert(failure3.handleErrorWith {
      case "Badness" => 256.pure[ErrorOr]
      case _ => ("It's not ok").raiseError[ErrorOr, Int]
    } === Right(256))
    assert(success2.ensure("Number too low!")(_ > 1000).isLeft)

    assert(exn.raiseError[Try, Int].isFailure)

    assert(validateAdult[Try](18) === Success(18))
    assert(validateAdult[Try](8).isFailure)
    assert(validateAdult[ExceptionOr](-1).isLeft)
  }
end MonadErrorFlatSpec
