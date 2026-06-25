package com.peknight.cats.demo.semigroupal

import org.scalatest.flatspec.AnyFlatSpec
import cats.Semigroupal
import cats.arrow.FunctionK
import cats.syntax.apply.*
import cats.syntax.parallel.*

class ParallelFlatSpec extends AnyFlatSpec:
  "Parallel" should "pass" in {
    type ErrorOr[A] = Either[Vector[String], A]
    type ErrorOrList[A] = Either[List[String], A]

    object optionToList extends FunctionK[Option, List]:
      def apply[A](fa: Option[A]): List[A] = fa match
        case None => List.empty[A]
        case Some(a) => List(a)
    end optionToList

    val error1: ErrorOr[Int] = Left(Vector("Error 1"))
    val error2: ErrorOr[Int] = Left(Vector("Error 2"))
    val errStr1: ErrorOrList[Int] = Left(List("error 1"))
    val errStr2: ErrorOrList[Int] = Left(List("error 2"))
    val success1: ErrorOr[Int] = Right(1)
    val success2: ErrorOr[Int] = Right(2)
    val addTwo = (x: Int, y: Int) => x + y

    assert(Semigroupal[ErrorOr].product(error1, error2) === Left(Vector("Error 1")))
    assert((error1, error2).tupled === Left(Vector("Error 1")))
    assert((error1, error2).parTupled === Left(Vector("Error 1", "Error 2")))
    assert((errStr1, errStr2).parTupled === Left(List("error 1", "error 2")))
    assert((error1, error2).parMapN(addTwo) === Left(Vector("Error 1", "Error 2")))
    assert((success1, success2).parMapN(addTwo) === Right(3))
    assert(optionToList(Some(1)) === List(1))
    assert(optionToList(None) === Nil)
    assert((List(1, 2), List(3, 4)).tupled === List((1, 3), (1, 4), (2, 3), (2, 4)))
    assert((List(1, 2), List(3, 4)).parTupled === List((1, 3), (2, 4)))
  }
end ParallelFlatSpec
