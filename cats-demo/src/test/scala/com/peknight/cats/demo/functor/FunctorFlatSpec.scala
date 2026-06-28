package com.peknight.cats.demo.functor

import cats.Functor
import cats.syntax.functor.*
import com.peknight.cats.data.Tree
import com.peknight.cats.data.Tree.Leaf
import com.peknight.cats.demo.data.Box
import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class FunctorFlatSpec extends AnyFlatSpec:
  "Functor" should "pass for List" in {
    val list1 = List(1, 2, 3)
    val list2 = Functor[List].map(list1)(_ * 2)
    assert(list1.map(_ + 1) === List(2, 3, 4))
    assert(list1.map(_ + 1).map(_ * 2).map(n => s"$n!") === List("4!", "6!", "8!"))
    assert(list2 === List(2, 4, 6))
    assert(Functor[List].as(list1, "As") === List("As", "As", "As"))
    assert(list1.as("As") == List("As", "As", "As"))
  }

  "Functor" should "pass for Function" in {
    val func1: Int => Double = (x: Int) => x.toDouble
    val func2: Double => Double = (y: Double) => y * 2
    val func3: Int => String = ((x: Int) => x.toDouble).map(_ + 1).map(_ * 2).map(x => s"$x!")
    val func4: Int => Double = func1.map(func2)
    val func5 = (a: Int) => a + 1
    val func6 = (a: Int) => a * 2
    val func7 = (a: Int) => s"$a!"
    val func8 = func5.map(func6).map(func7)

    assert((func1 map func2)(1) === 2.0)
    assert((func1 andThen func2)(1) === 2.0)
    assert(func2(func1(1)) === 2.0)
    // jvm与js表现不一致，jvm: "248.0!" js: "248!"
    assert(func3(123).matches("248(\\.0)?!"))
    assert(func4(123) === 246.0)
    assert(func8(123) === "248!")
  }

  "Functor" should "pass for Option" in {
    val option1 = Option(123)
    val option2 = Functor[Option].map(option1)(_.toString)
    assert(option2 === Some("123"))
  }

  "Functor" should "pass for lift" in {
    val func = (x: Int) => x + 1
    val liftedFunc = Functor[Option].lift(func)
    assert(liftedFunc(Option(1)) === Some(2))
  }

  def doMath[F[_]](start: F[Int])(using functor: Functor[F]): F[Int] = start.map(n => n + 2)

  "Functor" should "pass for doMath" in {
    assert(doMath(Option(20)) === Option(22))
    assert(doMath(List(1, 2, 3)) === List(3, 4, 5))
  }

  "Functor" should "pass for Box" in {
    val box = Box[Int](123)
    assert(box.map(value => value + 1) === Box(124))
  }

  "Functor" should "pass for Tree" in {
    assert(Tree.branch(Leaf(10), Leaf(20)).map(_ * 2) === Tree.branch(Leaf(20), Leaf(40)))
  }

  "Functor" should "pass for Either" in {
    val either: Either[String, Int] = Right(123)
    assert(either.map(_ + 1) === Right(124))
  }

  "Functor" should "pass" in {
    Functor[Future]
    type F[A] = Int => A
    val functor = Functor[F]
  }
end FunctorFlatSpec
