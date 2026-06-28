package com.peknight.cats.demo.monad

import cats.Monad
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class FutureMonadFlatSpec extends AsyncFlatSpec:
  def doSomethingLongRunning(): Future[Int] = Future(1)

  def doSomethingElseLongRunning(): Future[Int] = Future(2)

  def doSomethingVeryLongRunning(): Future[Int] =
    for
      result1 <- doSomethingLongRunning()
      result2 <- doSomethingElseLongRunning()
    yield result1 + result2

  "Future Monad" should "pass for very long running" in {
    doSomethingVeryLongRunning().map(i => assert(i === 3))
  }

  "Future Monad" should "pass for flatMap" in {
    val fm = Monad[Future]
    val x = fm.pure(1)
    val future = fm.flatMap(fm.pure(1)) {
      x => fm.pure(x + 2)
    }
    future.map(i => assert(i === 3))
  }
end FutureMonadFlatSpec
