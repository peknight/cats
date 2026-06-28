package com.peknight.cats.syntax

import cats.data.EitherT
import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.either.*
import cats.syntax.functor.*
import cats.{Applicative, ApplicativeError, Functor, Monad}

trait EitherTSyntax:
  extension [A, B] (either: Either[A, B])
    def eLiftET[F[_]: Applicative]: EitherT[F, A, B] = EitherT(either.pure[F])
  end extension
  extension [A] (a: A)
    def lLiftET[F[_] : Applicative, R]: EitherT[F, A, R] = a.asLeft[R].eLiftET[F]
    def rLiftET[F[_]: Applicative, L]: EitherT[F, L, A] = a.asRight[L].eLiftET[F]
  end extension
  extension [F[_], A] (fa: F[A])
    def flLiftET[R](using Functor[F]): EitherT[F, A, R] = EitherT(fa.map(_.asLeft[R]))
    def frLiftET[L](using Functor[F]): EitherT[F, L, A] = EitherT(fa.map(_.asRight[L]))
    def feLiftET[E](using ApplicativeError[F, E]): EitherT[F, E, A] = EitherT(fa.attempt)
  end extension
  extension [F[_], A] (eitherT: EitherT[F, A, Boolean])
    def &&(that: => EitherT[F, A, Boolean])(using Monad[F]): EitherT[F, A, Boolean] = eitherT.flatMap {
      case true => that
      case false => false.rLiftET[F, A]
    }
  end extension
end EitherTSyntax
object EitherTSyntax extends EitherTSyntax
