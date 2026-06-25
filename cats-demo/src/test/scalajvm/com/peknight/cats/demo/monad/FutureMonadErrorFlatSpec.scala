package com.peknight.cats.demo.monad

import cats.syntax.applicativeError.*
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class FutureMonadErrorFlatSpec extends AsyncFlatSpec:
  "Future MonadError" should "pass" in {
    recoverToSucceededIf[RuntimeException] {
      val exn: Throwable = new RuntimeException("It's all gone wrong")
      exn.raiseError[Future, Int]
    }
  }
end FutureMonadErrorFlatSpec
