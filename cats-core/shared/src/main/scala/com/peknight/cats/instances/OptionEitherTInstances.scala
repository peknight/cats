package com.peknight.cats.instances

import cats.Monad
import cats.syntax.applicative.*
import cats.syntax.either.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import cats.syntax.option.*

trait OptionEitherTInstances:
  given optionEitherTMonad[F[_]: Monad, E]: Monad[[T] =>> F[Either[E, Option[T]]]] with
    def flatMap[A, B](fa: F[Either[E, Option[A]]])(f: A => F[Either[E, Option[B]]]): F[Either[E, Option[B]]] =
      fa.flatMap {
        case Left(e) => e.asLeft[Option[B]].pure[F]
        case Right(None) => none[B].asRight[E].pure[F]
        case Right(Some(a)) => f(a)
      }
    def tailRecM[A, B](a: A)(f: A => F[Either[E, Option[Either[A, B]]]]): F[Either[E, Option[B]]] =
      Monad[F].tailRecM[A, Either[E, Option[B]]](a){ a0 =>
        // F[Either[A, Either[E, Option[B]]]
        f(a0).map {
          case Left(e) => e.asLeft[Option[B]].asRight[A]
          case Right(None) => none[B].asRight[E].asRight[A]
          case Right(Some(Left(a1))) => a1.asLeft[Either[E, Option[B]]]
          case Right(Some(Right(b))) => b.some.asRight[E].asRight[A]
        }
      }
    def pure[A](x: A): F[Either[E, Option[A]]] = x.some.asRight[E].pure[F]
  end optionEitherTMonad
end OptionEitherTInstances
object OptionEitherTInstances extends OptionEitherTInstances
