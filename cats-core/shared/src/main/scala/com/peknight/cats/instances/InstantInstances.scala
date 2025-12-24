package com.peknight.cats.instances

import cats.{Eq, Order, Show}

import java.time.Instant

trait InstantInstances:
  given Eq[Instant] with
    override def eqv(x: Instant, y: Instant): Boolean = x.compareTo(y) == 0
  end given
  given Order[Instant] = Order.fromComparable[Instant]
  given Show[Instant] = Show.fromToString[Instant]
end InstantInstances
object InstantInstances extends InstantInstances
