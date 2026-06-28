package com.peknight.cats.data

import cats.syntax.functor.*
import cats.{Functor, Monad}
import com.peknight.cats.instances.optionEitherT.optionEitherTMonad

final case class OptionEitherT[F[_], E, A](value: F[Either[E, Option[A]]]):
  def bimap[S, T](fe: E => S, fa: A => T)(using Functor[F]): OptionEitherT[F, S, T] =
    OptionEitherT(value.map {
      case Right(Some(a)) => Right(Some(fa(a)))
      case Right(None) => Right(None)
      case Left(e) => Left(fe(e))
    })
  def map[B](f: A => B)(using Functor[F]): OptionEitherT[F, E, B] = bimap[E, B](identity, f)
  def flatMap[B](f: A => OptionEitherT[F, E, B])(using Monad[F]): OptionEitherT[F, E, B] =
    OptionEitherT(optionEitherTMonad[F, E].flatMap[A, B](value)(a => f(a).value))
  def leftMap[S](f: E => S)(using Functor[F]): OptionEitherT[F, S, A] = bimap[S, A](f, identity)
end OptionEitherT
object OptionEitherT:
  given [F[_]: Monad, E]: Monad[[A] =>> OptionEitherT[F, E, A]] with
    def pure[A](x: A): OptionEitherT[F, E, A] = OptionEitherT(optionEitherTMonad[F, E].pure(x))
    def flatMap[A, B](fa: OptionEitherT[F, E, A])(f: A => OptionEitherT[F, E, B]): OptionEitherT[F, E, B] =
      fa.flatMap[B](f)
    def tailRecM[A, B](a: A)(f: A => OptionEitherT[F, E, Either[A, B]]): OptionEitherT[F, E, B] =
      OptionEitherT(optionEitherTMonad[F, E].tailRecM[A, B](a)(a0 => f(a0).value))
  end given
end OptionEitherT
