package com.peknight.cats.scalacheck.instances

import cats.{ContravariantSemigroupal, MonoidK}
import org.scalacheck.Cogen

private[scalacheck] trait CogenInstances:
  given ContravariantSemigroupal[Cogen] with
    def product[A, B](fa: Cogen[A], fb: Cogen[B]): Cogen[(A, B)] = Cogen.tuple2(using fa, fb)
    def contramap[A, B](fa: Cogen[A])(f: B => A): Cogen[B] = fa.contramap(f)
  end given

  given MonoidK[Cogen] with
    def empty[A]: Cogen[A] = Cogen { (seed, _) => seed }
    def combineK[A](x: Cogen[A], y: Cogen[A]): Cogen[A] = Cogen { (seed, a) => y.perturb(x.perturb(seed, a), a) }
  end given

end CogenInstances

object CogenInstances extends CogenInstances
