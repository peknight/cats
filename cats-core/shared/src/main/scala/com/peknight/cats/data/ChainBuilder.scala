package com.peknight.cats.data

import cats.data.Chain

import scala.collection.mutable

class ChainBuilder[A] extends mutable.Builder[A, Chain[A]]:
  private var xs: Chain[A] = Chain.nil
  final def clear(): Unit = xs = Chain.nil
  final def result(): Chain[A] = xs
  final def addOne(elem: A): this.type =
    xs = xs.append(elem)
    this
end ChainBuilder
