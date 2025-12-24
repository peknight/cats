package com.peknight.cats.instances

import cats.Monad
import cats.syntax.applicative.*
import cats.syntax.either.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*

trait EitherTInstances:
  given eitherTMonad[F[_]: Monad, E]: Monad[[T] =>> F[Either[E, T]]] with
    def flatMap[A, B](fa: F[Either[E, A]])(f: A => F[Either[E, B]]): F[Either[E, B]] =
      fa.flatMap {
        case Left(e) => e.asLeft[B].pure[F]
        case Right(a) => f(a)
      }
    def tailRecM[A, B](a: A)(f: A => F[Either[E, Either[A, B]]]): F[Either[E, B]] =
      Monad[F].tailRecM[A, Either[E, B]](a) { a0 =>
        // F[Either[A, Either[E, B]]]
        f(a0).map {
          case Left(e) => e.asLeft[B].asRight[A]
          case Right(Left(a1)) => a1.asLeft[Either[E, B]]
          case Right(Right(b)) => b.asRight[E].asRight[A]
        }
      }
    def pure[A](x: A): F[Either[E, A]] = x.asRight[E].pure[F]
  end eitherTMonad
end EitherTInstances
object EitherTInstances extends EitherTInstances
