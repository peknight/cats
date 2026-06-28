package com.peknight.cats.syntax

import cats.syntax.applicative.*
import cats.syntax.applicativeError.*
import cats.syntax.either.*
import cats.syntax.functor.*
import cats.syntax.option.*
import cats.{Applicative, ApplicativeError, Functor}
import com.peknight.cats.data.OptionEitherT

trait OptionEitherTSyntax:
  extension [L, R](either: Either[L, Option[R]])
    def oeLiftOET[F[_]: Applicative]: OptionEitherT[F, L, R] = OptionEitherT(either.pure[F])
  end extension

  extension [L, R](either: Either[L, R])
    def eLiftOET[F[_]: Applicative]: OptionEitherT[F, L, R] = either.map(Some.apply).oeLiftOET[F]
  end extension

  extension [R](option: Option[R])
    def oLiftOET[F[_]: Applicative, L]: OptionEitherT[F, L, R] = option.asRight[L].oeLiftOET[F]
  end extension

  extension [A] (a: A)
    def lLiftOET[F[_]: Applicative, R]: OptionEitherT[F, A, R] = a.asLeft[Option[R]].oeLiftOET[F]
    def rLiftOET[F[_]: Applicative, L]: OptionEitherT[F, L, A] = Some(a).oLiftOET[F, L]
  end extension

  extension [F[_], L, R] (fe: F[Either[L, R]])
    def efLiftOET(using Functor[F]): OptionEitherT[F, L, R] = OptionEitherT(fe.map(_.map(Some.apply)))
  end extension

  extension [F[_], R] (fo: F[Option[R]])
    def ofLiftOET[L](using Functor[F]): OptionEitherT[F, L, R] = OptionEitherT(fo.map(_.asRight[L]))
  end extension

  extension [F[_], A] (fa: F[A])
    def flLiftOET[R](using Functor[F]): OptionEitherT[F, A, R] = OptionEitherT(fa.map(_.asLeft[Option[R]]))
    def frLiftOET[L](using Functor[F]): OptionEitherT[F, L, A] = OptionEitherT(fa.map(_.some.asRight[L]))
    def feLiftOET[E](using ApplicativeError[F, E]): OptionEitherT[F, E, A] = OptionEitherT(fa.map(_.some).attempt)
  end extension
end OptionEitherTSyntax
object OptionEitherTSyntax extends OptionEitherTSyntax
