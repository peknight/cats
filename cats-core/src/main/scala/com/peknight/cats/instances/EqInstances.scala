package com.peknight.cats.instances

import cats.Eq

trait EqInstances:
  given [A <: Comparable[A]]: Eq[A] with
    override def eqv(x: A, y: A): Boolean = x.compareTo(y) == 0
  end given
end EqInstances
