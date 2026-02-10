package com.peknight.cats.syntax

import cats.syntax.applicative.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.{Functor, Monad}

trait BooleanSyntax:
  extension [F[_]] (fa: F[Boolean])
    def &&(fb: => F[Boolean])(using Monad[F]): F[Boolean] =
      fa.flatMap(a => if a then fb else false.pure[F])

    def &&(fb: => Boolean)(using Functor[F]): F[Boolean] =
      fa.map(a => if a then fb else false)

    def ||(fb: => F[Boolean])(using Monad[F]): F[Boolean] =
      fa.flatMap(a => if a then true.pure[F] else fb)

    def ||(fb: => Boolean)(using Functor[F]): F[Boolean] =
      fa.map(a => if a then true else fb)
  end extension
end BooleanSyntax
object BooleanSyntax extends BooleanSyntax
