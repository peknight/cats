package com.peknight.cats.demo.functor

import cats.Monoid
import cats.syntax.semigroup.*
import CodecInstances.given
import com.peknight.cats.demo.monoid.MonoidInstances.given

object InvariantApp extends App:
  println(Codec.encode(123.4))
  println(Codec.decode[Double]("123.4"))
  println(Codec.encode[Box[Double]](Box(123.4)))
  println(Codec.decode[Box[Double]]("123.4"))

  println(Monoid[Symbol].empty)
  println(Symbol("a") |+| Symbol("few") |+| Symbol("words"))
