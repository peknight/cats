package com.peknight.cats.scalacheck.instances

import cats.Semigroup
import com.peknight.cats.scalacheck.instances
import org.scalacheck.Gen

private[scalacheck] trait GenInstances2:
  given [A: Semigroup]: Semigroup[Gen[A]] with
    def combine(x: Gen[A], y: Gen[A]): Gen[A] =
      for
        xa <- x
        ya <- y
      yield Semigroup[A].combine(xa, ya)
  end given
end GenInstances2

