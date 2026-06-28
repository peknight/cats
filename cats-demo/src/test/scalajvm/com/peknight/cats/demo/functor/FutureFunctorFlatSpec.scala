package com.peknight.cats.demo.functor

import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future
import scala.util.Random

class FutureFunctorFlatSpec extends AsyncFlatSpec:

  "Future Functor" should "pass for map" in {
    val future: Future[String] = Future(123).map(_ + 1).map(_ * 2).map(n => s"$n!")
    future.map(i => assert(i === "248!"))
  }

  "Future Functor" should "pass for flatMap" in {
    val future1 =
      // Initialize Random with a fixed seed:
      val r = new Random(0L)
      // nextInt has the side effect of moving to
      // the next random number in the sequence:
      val x = Future(r.nextInt())
      for
        a <- x
        b <- x
      yield (a, b)

    val future2 =
      val r = new Random(0L)
      for
        a <- Future(r.nextInt())
        b <- Future(r.nextInt())
      yield (a, b)

    for
      (a1, b1) <- future1
      (a2, b2) <- future2
    yield
      assert(a1 == b1)
      assert(a2 != b2)
  }

  "Future Functor" should "pass" in {
    Future(123).map(_.toString()).map(i => assert(i === "123"))
  }
end FutureFunctorFlatSpec
