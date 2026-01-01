package com.peknight.cats.demo.instances

import cats.Semigroup

trait SemigroupInstances:
  given setIntersectionSemigroup[A]: Semigroup[Set[A]] with
    def combine(a: Set[A], b: Set[A]): Set[A] = a intersect b
  end setIntersectionSemigroup
end SemigroupInstances
object SemigroupInstances extends SemigroupInstances
