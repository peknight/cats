package com.peknight.cats.demo.data

import cats.Monoid
import cats.syntax.semigroup.*

case class Order(totalCost: Double, quantity: Double)
object Order:
  given orderMonoid: Monoid[Order] with
    def combine(a: Order, b: Order): Order = Order(a.totalCost |+| b.totalCost, a.quantity |+| b.quantity)
    def empty: Order = Order(Monoid[Double].empty, Monoid[Double].empty)
  end orderMonoid
end Order
