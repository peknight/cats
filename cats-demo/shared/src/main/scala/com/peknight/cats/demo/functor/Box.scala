package com.peknight.cats.demo.functor

import cats.Eq
import cats.syntax.eq.*

final case class Box[A](value: A)
object Box:
  given eqBox[A: Eq]: Eq[Box[A]] with
    def eqv(x: Box[A], y: Box[A]): Boolean = x.value === y.value
  end eqBox
end Box
