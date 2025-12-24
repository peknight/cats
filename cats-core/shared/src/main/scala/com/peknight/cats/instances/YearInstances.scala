package com.peknight.cats.instances

import cats.{Eq, Order}

import java.time.Year

trait YearInstances:
  given Eq[Year] with
    override def eqv(x: Year, y: Year): Boolean = x.compareTo(y) == 0
  end given
  given Order[Year] = Order.fromComparable[Year]
end YearInstances
object YearInstances extends YearInstances
