package com.peknight.cats.scalacheck.instances

import cats.Semigroup
import com.peknight.cats.scalacheck.instances
import org.scalacheck.Arbitrary

private[scalacheck] trait ArbitraryInstances2:
  given [A: Semigroup]: Semigroup[Arbitrary[A]] with
    def combine(x: Arbitrary[A], y: Arbitrary[A]): Arbitrary[A] = Arbitrary {
      for
        xa <- x.arbitrary
        ya <- y.arbitrary
      yield Semigroup[A].combine(xa, ya)
    }
  end given
end ArbitraryInstances2

