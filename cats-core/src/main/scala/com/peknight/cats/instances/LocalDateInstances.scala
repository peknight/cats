package com.peknight.cats.instances

import cats.{Eq, Order}

import java.time.LocalDate

trait LocalDateInstances:
  given Eq[LocalDate] with
    override def eqv(x: LocalDate, y: LocalDate): Boolean = x.compareTo(y) == 0
  end given
  given Order[LocalDate] with
    def compare(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  end given
end LocalDateInstances
object LocalDateInstances extends LocalDateInstances
