package com.peknight.cats.demo.monoid

import cats.Monoid
import cats.kernel.Semigroup
import cats.syntax.semigroup.*
import com.peknight.cats.demo.instances.MonoidInstances.given
import com.peknight.cats.demo.instances.monoid.{intSetMonoid, strSetMonoid}
import org.scalatest.flatspec.AnyFlatSpec

class MonoidFlatSpec extends AnyFlatSpec:
  "Monoid" should "pass" in {
    // def add(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)(_ |+| _)
    // def add[A](items: Seq[A])(using m: Monoid[A]): A = items.foldLeft(m.empty)(_ |+| _)
    def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _)

    val a = Option(22)
    val b = Option(20)
    val intResult = 1 |+| 2 |+| Monoid[Int].empty

    assert(Monoid[Symbol].empty === Symbol(""))
    assert((Symbol("a") |+| Symbol("few") |+| Symbol("words")) === Symbol("afewwords"))
    assert(intSetMonoid.combine(Set(1, 2), Set(2, 3)) === Set(1, 2, 3))
    assert(strSetMonoid.combine(Set("A", "B"), Set("B", "C")) === Set("A", "B", "C"))
    assert(Monoid[String].combine("Hi ", "there") === "Hi there")
    assert(Monoid[String].empty === "")
    assert(Semigroup[String].combine("Hi ", "there") === "Hi there")
    assert(Monoid[Int].combine(32, 10) === 42)
    assert(Monoid[Option[Int]].combine(a, b) === Some(42))
    assert(Monoid[Option[Int]].combine(a, Option.empty[Int]) === Some(22))
    assert(intResult === 3)
    assert(add(List(1, 2, 3, 4, 5)) === 15)
    assert(add(List(Option(1), Option(2), Option(3), Option.empty)) === Some(6))
  }
end MonoidFlatSpec
