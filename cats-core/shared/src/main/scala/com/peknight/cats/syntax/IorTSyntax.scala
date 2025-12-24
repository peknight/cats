package com.peknight.cats.syntax

import cats.data.{Ior, IorT}
import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.either.*
import cats.syntax.functor.*
import cats.syntax.ior.*
import cats.{Applicative, ApplicativeError, Functor, Monad, Semigroup}

trait IorTSyntax:
  extension [A, B] (either: Either[A, B])
    def eLiftIT[F[_]: Applicative]: IorT[F, A, B] = IorT(either.toIor.pure[F])
  end extension
  extension [A, B] (ior: Ior[A, B])
    def iLiftIT[F[_]: Applicative]: IorT[F, A, B] = IorT(ior.pure[F])
  end extension
  extension [A] (a: A)
    def lLiftIT[F[_] : Applicative, R]: IorT[F, A, R] = a.leftIor[R].iLiftIT[F]
    def rLiftIT[F[_]: Applicative, L]: IorT[F, L, A] = a.rightIor[L].iLiftIT[F]
  end extension
  extension [F[_], A] (fa: F[A])
    def flLiftIT[R](using Functor[F]): IorT[F, A, R] = IorT(fa.map(_.leftIor[R]))
    def frLiftIT[L](using Functor[F]): IorT[F, L, A] = IorT(fa.map(_.rightIor[L]))
    def fiLiftIT[E](using ApplicativeError[F, E]): IorT[F, E, A] = IorT(fa.attempt.map(_.toIor))
  end extension
  extension [F[_], A] (iorT: IorT[F, A, Boolean])
    def &&(that: => IorT[F, A, Boolean])(using Monad[F], Semigroup[A]): IorT[F, A, Boolean] = iorT.flatMap {
      case true => that
      case false => false.rLiftIT[F, A]
    }
  end extension
end IorTSyntax
object IorTSyntax extends IorTSyntax
