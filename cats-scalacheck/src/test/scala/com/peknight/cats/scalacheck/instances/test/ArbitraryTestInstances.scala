package com.peknight.cats.scalacheck.instances.test

import cats.Eq
import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryTestInstances:
  given [A](using Eq[Gen[A]]): Eq[Arbitrary[A]] = Eq.by(_.arbitrary)
  given [A](using Arbitrary[Gen[A]]): Arbitrary[Arbitrary[A]] =
    Arbitrary(Arbitrary.arbitrary[Gen[A]].map(Arbitrary.apply))
end ArbitraryTestInstances

object ArbitraryTestInstances extends ArbitraryTestInstances
