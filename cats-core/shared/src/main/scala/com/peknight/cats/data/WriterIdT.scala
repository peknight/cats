package com.peknight.cats.data

import cats.data.Writer
import cats.{FlatMap, Functor, Semigroup}

case class WriterIdT[F[_], L, A](value: F[Writer[L, A]]):
  def map[B](f: A => B)(using Functor[F]): WriterIdT[F, L, B] = WriterIdT(Functor[F].map(value) { writer =>
    val (l, a) = writer.run
    Writer(l, f(a))
  })

  def flatMap[B](f: A => WriterIdT[F, L, B])(using FlatMap[F], Semigroup[L]): WriterIdT[F, L, B] =
    flatMapF(a => f(a).value)

  def flatMapF[B](f: A => F[Writer[L, B]])(using FlatMap[F], Semigroup[L]): WriterIdT[F, L, B] =
    WriterIdT(FlatMap[F].flatMap(value) { writer =>
      val (la, a) = writer.run
      FlatMap[F].map(f(a))(_.mapWritten(lb => Semigroup[L].combine(la, lb)))
    })
end WriterIdT