package com.peknight.cats.demo.monad

import cats.Id
import cats.data.{Writer, WriterT}
import cats.syntax.applicative.*
import cats.syntax.writer.*
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.*

class FutureWriterFlatSpec extends AsyncFlatSpec:
  "Future Writer" should "pass" in {
    type Logged[A] = Writer[Vector[String], A]

    def slowly[A](body: => A) = try body finally Thread.sleep(100)

    def factorial(n: Int): Int =
      val ans = slowly(if n == 0 then 1 else n * factorial(n - 1))
      println(s"fact $n $ans")
      ans

    def factorialWithLog(n: Int): Logged[Int] =
      for
        ans <- if n == 0 then 1.pure[Logged] else slowly(factorialWithLog(n - 1).map(_ * n))
        _ <- Vector(s"fact $n $ans").tell
      yield ans

    val _: WriterT[Id, Vector[String], Int] = Writer(Vector("It was the best of times", "it was the worst of times"), 1859)
    val _: Logged[Int] = 123.pure[Logged]
    val _: Writer[Vector[String], Unit] = Vector("msg1", "msg2", "msg3").tell
    val a = Writer(Vector("msg1", "msg2", "msg3"), 123)
    val b = 123.writer(Vector("msg1", "msg2", "msg3"))
    val aResult: Int = a.value
    val aLog: Vector[String] = a.written
    val (bLog, bResult) = b.run
    val writer1 =
      for
        a <- 10.pure[Logged]
        _ <- Vector("a", "b", "c").tell
        b <- 32.writer(Vector("x", "y", "z"))
      yield a + b
    val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
    val writer3 = writer1.bimap(
      log => log.map(_.toUpperCase),
      res => res * 100
    )
    val writer4 = writer1.mapBoth { (log, res) =>
      val log2 = log.map(_ + "!")
      val res2 = res * 1000
      (log2, res2)
    }
    val writer5 = writer1.reset
    val writer6 = writer1.swap
    val (logFactorial, res) = factorialWithLog(5).run
    val _: Future[Vector[Logged[Int]]] = Future.sequence(Vector(
      Future(factorialWithLog(5)),
      Future(factorialWithLog(5))
    ))

    assert(aResult === 123)
    assert(aLog === Vector("msg1", "msg2", "msg3"))
    assert(bResult === 123)
    assert(bLog === Vector("msg1", "msg2", "msg3"))
    assert(writer1.run === (Vector("a", "b", "c", "x", "y", "z"), 42))
    assert(10.pure[Logged].flatMap(a =>
      Vector("a", "b", "c").tell.flatMap(_ =>
        32.writer(Vector("x", "y", "z")).map(b =>
          a + b
        )
      )
    ).run === (Vector("a", "b", "c", "x", "y", "z"), 42))
    assert(writer2.run === (Vector("A", "B", "C", "X", "Y", "Z"), 42))
    assert(writer3.run === (Vector("A", "B", "C", "X", "Y", "Z"), 4200))
    assert(writer4.run === (Vector("a!", "b!", "c!", "x!", "y!", "z!"), 42000))
    assert(writer5.run === (Vector.empty, 42))
    assert(writer6.run === (42, Vector("a", "b", "c", "x", "y", "z")))
    assert(factorial(5) === 120)
    assert(logFactorial === Vector("fact 0 1", "fact 1 1", "fact 2 2", "fact 3 6", "fact 4 24", "fact 5 120"))
    assert(res === 120)
    Future.sequence(Vector(
      Future(factorial(5)),
      Future(factorial(5))
    )).map(i => assert(i === Vector(120, 120)))
  }
end FutureWriterFlatSpec
