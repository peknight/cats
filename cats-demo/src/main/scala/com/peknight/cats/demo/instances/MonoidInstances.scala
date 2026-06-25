package com.peknight.cats.demo.instances

import cats.Monoid
import cats.syntax.invariant.*

trait MonoidInstances:
  given booleanAndMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean): Boolean = a && b
    def empty: Boolean = true
  end booleanAndMonoid

  given booleanOrMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean): Boolean = a || b
    def empty: Boolean = false
  end booleanOrMonoid

  given booleanEitherMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean): Boolean = (a && !b) || (!a && b)
    def empty: Boolean = false
  end booleanEitherMonoid

  given booleanXnorMonoid: Monoid[Boolean] with
    def combine(a: Boolean, b: Boolean): Boolean = (!a || b)  && (a || !b)
    def empty: Boolean = true
  end booleanXnorMonoid

  given setUnionMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = a union b
    def empty: Set[A] = Set.empty[A]
  end setUnionMonoid

  val intSetMonoid: Monoid[Set[Int]] = Monoid[Set[Int]](using setUnionMonoid)
  val strSetMonoid: Monoid[Set[String]] = Monoid[Set[String]](using setUnionMonoid)

  given symDiffMonoid[A]: Monoid[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = (a diff b) union (b diff a)
    def empty: Set[A] = Set.empty
  end symDiffMonoid

  given symbolMonoid: Monoid[Symbol] = Monoid[String].imap[Symbol](Symbol.apply)(_.name)
end MonoidInstances
object MonoidInstances extends MonoidInstances