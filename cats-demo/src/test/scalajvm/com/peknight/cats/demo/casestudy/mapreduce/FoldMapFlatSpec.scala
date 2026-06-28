package com.peknight.cats.demo.casestudy.mapreduce

import cats.Monoid
import cats.syntax.foldable.*
import cats.syntax.semigroup.*
import cats.syntax.traverse.*
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class FoldMapFlatSpec extends AsyncFlatSpec:
  def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B =
    // values.foldLeft(Monoid[B].empty)((b, a) => func(a) |+| b)
    values.map(func).foldLeft(Monoid[B].empty)(_ |+| _)

  def parallelFoldMap[A, B: Monoid](values: Vector[A])(func: A => B): Future[B] =
    val numCores = Runtime.getRuntime.availableProcessors()
    val groupSize = (1.0 * values.size / numCores).ceil.toInt
    values
      .grouped(groupSize)
      .toVector
      .traverse(group => Future(group.foldMap(func)))
      .map(_.combineAll)
    // val groups: Iterator[Vector[A]] = values.grouped(groupSize)
    // val futures: Iterator[Future[B]] = groups map { group =>
    //   Future {
    ////     group.foldLeft(Monoid[B].empty)(_ |+| func(_))
    //     foldMap(group)(func)
    //   }
    // }
    // Future.sequence(futures) map { iterable =>
    //   iterable.foldLeft(Monoid[B].empty)(_ |+| _)
    // }

  "FoldMap" should "pass" in {
    assert(foldMap(Vector(1, 2, 3))(identity) === 6)
    assert(foldMap(Vector(1, 2, 3))(_.toString + "! ") === "1! 2! 3! ")
    assert(foldMap("Hello world!".toVector)(_.toString.toUpperCase) === "HELLO WORLD!")
  }

  "Parallel FoldMap" should "pass" in {
    parallelFoldMap((1 to 1000000).toVector)(identity).map(i => assert(i === 1784293664))
  }
end FoldMapFlatSpec
