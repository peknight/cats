package com.peknight.cats.instances

import cats.Applicative
import cats.syntax.applicative.*
import cats.syntax.apply.*

trait ApplicativeInstances:
  given applicativeFG[F[_]: Applicative, G[_]: Applicative]: Applicative[[T] =>> F[G[T]]] with
    def pure[A](x: A): F[G[A]] = x.pure[G].pure[F]
    def ap[A, B](ff: F[G[A => B]])(fa: F[G[A]]): F[G[B]] =
      (ff, fa).mapN((ef, ea) => (ef, ea).mapN((f, a) => f(a)))
  end applicativeFG
end ApplicativeInstances
object ApplicativeInstances extends ApplicativeInstances
