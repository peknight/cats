package com.peknight.cats.demo

import cats.data.EitherT

import scala.concurrent.Future

package object data:
  type FutureEither[A] = EitherT[Future, HttpError, A]
end data
