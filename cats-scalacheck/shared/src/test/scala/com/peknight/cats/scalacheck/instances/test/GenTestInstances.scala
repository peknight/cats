package com.peknight.cats.scalacheck.instances.test

import cats.Eq
import seed.given
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Gen, GenOps}

import scala.annotation.tailrec

trait GenTestInstances:
  def genEq[A: Eq](trials: Int): Eq[Gen[A]] = Eq.instance[Gen[A]] { (genX: Gen[A], genY: Gen[A]) =>
    val params = Gen.Parameters.default
    @tailrec def loop(count: Int, seed: Seed): Boolean =
      if count <= 0 then true
      else
        val x = GenOps.doApply(genX)(params, seed)
        val y = GenOps.doApply(genY)(params, seed)
        if Eq[Option[A]].neqv(x.retrieve, y.retrieve) then false
        else loop(count - 1, seed.next)
    loop(trials, Seed.random())
  }
  given [A : Eq]: Eq[Gen[A]] = genEq(1024)
  given [A : Arbitrary]: Arbitrary[Gen[A]] =
    val simple = Gen.const(Arbitrary.arbitrary[A])
    val complex = Arbitrary.arbitrary[Seed => Seed].map(f => GenOps.gen(
      (params, seed) => GenOps.doApply(Arbitrary.arbitrary[A])(params, f(seed))
    ))
    Arbitrary(Gen.oneOf(simple, complex))

end GenTestInstances

object GenTestInstances extends GenTestInstances