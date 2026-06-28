package com.peknight.cats.instances

import cats.Monoid

import scala.compiletime.constValue

trait TupleInstances extends TupleInstances2:

  inline given[K <: String, H, T <: Tuple](using hMonoid: => Monoid[H], tMonoid: Monoid[T]): Monoid[(K, H) *: T] =
    val label = constValue[K]
    Monoid.instance(
      (label, hMonoid.empty) *: tMonoid.empty,
      (x, y) => (label, hMonoid.combine(x.head._2, y.head._2)) *: tMonoid.combine(x.tail, y.tail)
    )
  end given
end TupleInstances
object TupleInstances extends TupleInstances
