package com.peknight.cats.scalacheck.instances.test

import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Cogen}

trait SeedTestInstances:
  given Arbitrary[Seed] = Arbitrary(Arbitrary.arbitrary[Long].map(Seed.apply))
  given Cogen[Seed] = Cogen[Long].contramap(_.long._1)
end SeedTestInstances
object SeedTestInstances extends SeedTestInstances
