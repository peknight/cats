package com.peknight.cats.demo.monoid

import cats.Monoid
import cats.syntax.invariant.*
import cats.syntax.semigroup.*

object MonoidInstances:
  given booleanAndMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = a && b
    def empty = true
  end booleanAndMonoid

  given booleanOrMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = a || b
    def empty = false
  end booleanOrMonoid

  given booleanEitherMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = (a && !b) || (!a && b)
    def empty = false
  end booleanEitherMonoid

  given booleanXnorMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean) = (!a || b)  && (a || !b)
    def empty = true
  end booleanXnorMonoid

  given setUnionMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]) = a union b
    def empty = Set.empty[A]
  end setUnionMonoid

  val intSetMonoid = Monoid[Set[Int]](using setUnionMonoid)
  val strSetMonoid = Monoid[Set[String]](using setUnionMonoid)

  given symDiffMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = (a diff b) union (b diff a)
    def empty: Set[A] = Set.empty
  end symDiffMonoid

  given orderMonoid: Monoid[Order] with
    def combine(a: Order, b: Order): Order = Order(a.totalCost |+| b.totalCost, a.quantity |+| b.quantity)
    def empty: Order = Order(Monoid[Double].empty, Monoid[Double].empty)
  end orderMonoid

  given symbolMonoid: Monoid[Symbol] = Monoid[String].imap[Symbol](Symbol.apply)(_.name)
end MonoidInstances