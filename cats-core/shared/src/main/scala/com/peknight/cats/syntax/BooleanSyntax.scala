package com.peknight.cats.syntax

import cats.Monad
import cats.syntax.applicative.*
import cats.syntax.flatMap.*

trait BooleanSyntax:
  extension [F[_]] (fa: F[Boolean])
    def &&(fb: => F[Boolean])(using Monad[F]): F[Boolean] =
      fa.flatMap(a => if a then fb else false.pure[F])

    def ||(fb: => F[Boolean])(using Monad[F]): F[Boolean] =
      fa.flatMap(a => if a then true.pure[F] else fb)
  end extension
end BooleanSyntax
object BooleanSyntax extends BooleanSyntax
