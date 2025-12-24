package com.peknight.cats.scalacheck.instances.test

import cats.Eq
import seed.given
import org.scalacheck.rng.Seed
import org.scalacheck.{Arbitrary, Cogen, Gen, GenOps}

import scala.annotation.tailrec

trait CogenTestInstances:
  def cogenEq[A : Arbitrary](trials: Int): Eq[Cogen[A]] = Eq.instance[Cogen[A]] { (cogenX: Cogen[A], cogenY: Cogen[A]) =>
    val gen = Arbitrary.arbitrary[A]
    val params = Gen.Parameters.default
    @tailrec def loop(count: Int, retries: Int, seed: Seed): Boolean =
      if retries <= 0 then sys.error("Cogen equivalent check failed")
      else if count <= 0 then true
      else
        val r = GenOps.doApply(gen)(params, seed)
        r.retrieve match
          case Some(a) =>
            val seed = Seed.random()
            val sx = cogenX.perturb(seed, a)
            val sy = cogenY.perturb(seed, a)
            given CanEqual[Seed, Seed] = CanEqual.derived
            if sx != sy then false
            else loop(count - 1, retries, r.seed)
          case None => loop(count, retries - 1, r.seed)
    loop(trials, trials, Seed.random())
  }

  given [A : Arbitrary]: Eq[Cogen[A]] = cogenEq(1024)

  given [A : Cogen]: Arbitrary[Cogen[A]] = Arbitrary(Arbitrary.arbitrary[Seed => Seed].map(
    f => Cogen((seed, a) => f(Cogen[A].perturb(seed, a)))
  ))

end CogenTestInstances

object CogenTestInstances extends CogenTestInstances
