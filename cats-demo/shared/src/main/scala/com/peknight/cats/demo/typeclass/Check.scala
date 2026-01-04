package com.peknight.cats.demo.typeclass

import cats.Semigroup
import cats.data.Validated
import com.peknight.cats.demo.typeclass.Check.*

sealed trait Check[E, A, B]:
  def apply(a: A)(using Semigroup[E]): Validated[E, B]
  def map[C](func: B => C): Check[E, A, C] = Map[E, A, B, C](this, func)
  def flatMap[C](func: B => Check[E, A, C]): Check[E, A, C] = FlatMap[E, A, B, C](this, func)
  def andThen[C](next: Check[E, B, C]): Check[E, A, C] = AndThen[E, A, B, C](this, next)
end Check
object Check:
  final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C) extends Check[E, A, C]:
    def apply(in: A)(using Semigroup[E]): Validated[E, C] = check(in).map(func)
  end Map
  final case class FlatMap[E, A, B, C](check: Check[E, A, B], func: B => Check[E, A, C]) extends Check[E, A, C]:
    def apply(in: A)(using Semigroup[E]): Validated[E, C] = check(in).withEither(_.flatMap(b => func(b)(in).toEither))
  end FlatMap
  final case class AndThen[E, A, B, C](check1: Check[E, A, B], check2: Check[E, B, C]) extends Check[E, A, C]:
    def apply(in: A)(using Semigroup[E]): Validated[E, C] = check1(in).withEither(_.flatMap(check2(_).toEither))
  end AndThen
  final case class Pure[E, A, B](func: A => Validated[E, B]) extends Check[E, A, B]:
    def apply(in: A)(using Semigroup[E]): Validated[E, B] = func(in)
  end Pure
  final case class PurePredicate[E, A](pred: Predicate[E, A]) extends Check[E, A, A]:
    def apply(in: A)(using Semigroup[E]): Validated[E, A] = pred(in)
  end PurePredicate

  def apply[E, A](pred: Predicate[E, A]): Check[E, A, A] = PurePredicate(pred)

  def apply[E, A, B](func: A => Validated[E, B]): Check[E, A, B] = Pure(func)
end Check