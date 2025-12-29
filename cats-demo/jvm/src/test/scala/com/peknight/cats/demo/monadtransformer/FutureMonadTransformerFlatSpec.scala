package com.peknight.cats.demo.monadtransformer

import cats.data.{EitherT, OptionT}
import cats.syntax.applicative.*
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class FutureMonadTransformerFlatSpec extends AsyncFlatSpec:

  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]
  type Response[A] = EitherT[Future, String, A]

  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  def getPowerLevel(ally: String): Response[Int] = powerLevels.get(ally) match
    case Some(avg) => EitherT.right(Future(avg))
    case None => EitherT.left(Future(s"$ally unreachable"))

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for
      ally1Level <- getPowerLevel(ally1)
      ally2Level <- getPowerLevel(ally2)
    yield ally1Level + ally2Level > 15

  def tacticalReport(ally1: String, ally2: String): Future[String] =
    val stack = canSpecialMove(ally1, ally2).value
    stack.map {
      case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) => s"$ally1 and $ally2 need a recharge."
      case Left(msg) => s"Comms error: $msg"
    }

  "Future MonadTransformer" should "pass for FutureEitherOr" in {
    val futureEitherOr: FutureEitherOption[Int] =
      for
        a <- 10.pure[FutureEitherOption]
        b <- 32.pure[FutureEitherOption]
      yield a + b
    futureEitherOr.value.value.map(either => assert(either === Right(Some(42))))
  }

  "Future MonadTransformer" should "pass for tacticalReport Jazz and Bumblebee" in {
    tacticalReport("Jazz", "Bumblebee").map(i => assert(i === "Jazz and Bumblebee need a recharge."))
  }

  "Future MonadTransformer" should "pass for tacticalReport Bumblebee and Hot Rod" in {
    tacticalReport("Bumblebee", "Hot Rod").map(i => assert(i === "Bumblebee and Hot Rod are ready to roll out!"))
  }

  "Future MonadTransformer" should "pass for tacticalReport Jazz and Ironhide" in {
    tacticalReport("Jazz", "Ironhide").map(i => assert(i === "Comms error: Ironhide unreachable"))
  }
end FutureMonadTransformerFlatSpec
