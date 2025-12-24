package com.peknight.cats.order

import cats.Order

sealed trait ComparisonOperator:
  def compare[T: Order](t1: T, t2: T): Boolean
end ComparisonOperator
object ComparisonOperator:
  case object `==` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.eqv(t1, t2)
  end `==`
  case object `!=` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.neqv(t1, t2)
  end `!=`
  case object `>` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.gt(t1, t2)
  end `>`
  case object `<` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.lt(t1, t2)
  end `<`
  case object `>=` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.gteqv(t1, t2)
  end `>=`
  case object `<=` extends ComparisonOperator:
    def compare[T](t1: T, t2: T)(using order: Order[T]): Boolean = order.lteqv(t1, t2)
  end `<=`
end ComparisonOperator
