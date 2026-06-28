package com.peknight.cats.demo.monadtransformer

import cats.Id
import cats.data.{EitherT, OptionT, Writer, WriterT}
import cats.syntax.applicative.*
import org.scalatest.flatspec.AnyFlatSpec

import scala.util.Try

class MonadTransformerFlatSpec extends AnyFlatSpec:
  "MonadTransformer" should "pass" in {
    type ListOption[A] = OptionT[List, A]
    type ErrorOr[A] = Either[String, A]
    type ErrorOrOption[A] = OptionT[ErrorOr, A]
    type Logged[A] = Writer[List[String], A]

    def parseNumber(str: String): Logged[Option[Int]] = Try(str.toInt).toOption match
      case Some(num) => Writer(List(s"Read $str"), Some(num))
      case None => Writer(List(s"Failed on $str"), None)

    def addAll(a: String, b: String, c: String): Logged[Option[Int]] =
      val result =
        for
          a <- OptionT(parseNumber(a))
          b <- OptionT(parseNumber(b))
          c <- OptionT(parseNumber(c))
        yield a + b + c
      result.value

    val result1: ListOption[Int] = OptionT(List(Option(10)))
    val result2: ListOption[Int] = 32.pure[ListOption]
    val res1 = result1.flatMap(x => result2.map(x + _))
    val a = 10.pure[ErrorOrOption]
    val b = 32.pure[ErrorOrOption]
    val c = a.flatMap(x => b.map(x + _))
    // kind projector EitherT[Option, String, *]
    123.pure[({type f[x] = EitherT[Option, String, x]})#f]
    123.pure[[A] =>> EitherT[Option, String, A]]
    val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
    val errorStack2 = 32.pure[ErrorOrOption]
    val resultAddAll1 = addAll("1", "2", "3")
    val resultAddAll2 = addAll("1", "a", "3")

    assert(res1 === OptionT(List(Some(42))))
    assert(c === OptionT(Right(Some(42))))
    assert(errorStack1.value === Right(Some(10)))
    assert(errorStack2.value.map(_.getOrElse(-1)) === Right(32))
    assert(resultAddAll1 === WriterT[Id, List[String], Option[Int]]((List("Read 1", "Read 2", "Read 3"), Some(6))))
    assert(resultAddAll2 === WriterT[Id, List[String], Option[Int]]((List("Read 1", "Failed on a"), None)))
  }
end MonadTransformerFlatSpec
