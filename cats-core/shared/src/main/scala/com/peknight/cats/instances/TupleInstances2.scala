package com.peknight.cats.instances

import cats.Monoid

trait TupleInstances2:
  given Monoid[EmptyTuple] with
    def empty: EmptyTuple = EmptyTuple

    def combine(x: EmptyTuple, y: EmptyTuple): EmptyTuple = EmptyTuple
  end given

  given [H, T <: Tuple] (using hMonoid: Monoid[H], tMonoid: Monoid[T]): Monoid[H *: T] =
    Monoid.instance(
      hMonoid.empty *: tMonoid.empty,
      (x, y) => hMonoid.combine(x.head, y.head) *: tMonoid.combine(x.tail, y.tail)
    )
end TupleInstances2
