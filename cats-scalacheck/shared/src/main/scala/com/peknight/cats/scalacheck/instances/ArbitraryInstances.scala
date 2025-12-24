package com.peknight.cats.scalacheck.instances

import cats.{Alternative, Eval, Functor, FunctorFilter, Monad, Monoid}
import org.scalacheck.{Arbitrary, Gen, GenOps}

private[scalacheck] trait ArbitraryInstances extends ArbitraryInstances2:
  given Monad[Arbitrary] with Alternative[Arbitrary] with FunctorFilter[Arbitrary] with
    def pure[A](x: A): Arbitrary[A] = Arbitrary(Gen.const(x))

    def flatMap[A, B](fa: Arbitrary[A])(f: A => Arbitrary[B]): Arbitrary[B] =
      Arbitrary(fa.arbitrary.flatMap(a => f(a).arbitrary))

    def tailRecM[A, B](a: A)(f: A => Arbitrary[Either[A, B]]): Arbitrary[B] =
      Arbitrary(Gen.tailRecM(a)(a => f(a).arbitrary))

    def empty[A]: Arbitrary[A] = Arbitrary(Gen.fail)

    def combineK[A](x: Arbitrary[A], y: Arbitrary[A]): Arbitrary[A] = Arbitrary { GenOps.gen { (params, seed) =>
      val xGen = GenOps.doApply(x.arbitrary)(params, seed)
      if xGen.retrieve.isDefined then xGen else GenOps.doApply(y.arbitrary)(params, xGen.seed)
    }}

    def functor: Functor[Arbitrary] = this

    def mapFilter[A, B](fa: Arbitrary[A])(f: A => Option[B]): Arbitrary[B] = Arbitrary { fa.arbitrary.flatMap { a =>
      f(a) match
        case Some(b) => Gen.const(b)
        case _ => Gen.fail
    }}

    override def product[A, B](fa: Arbitrary[A], fb: Arbitrary[B]): Arbitrary[(A, B)] =
      Arbitrary(Gen.zip(fa.arbitrary, fb.arbitrary))

    override def map2Eval[A, B, Z](fa: Arbitrary[A], fb: Eval[Arbitrary[B]])(f: (A, B) => Z): Eval[Arbitrary[Z]] =
      Eval.later(map2(fa, Arbitrary(Gen.lzy(fb.value.arbitrary)))(f))
  end given

  given [A: Monoid]: Monoid[Arbitrary[A]] with
    def empty: Arbitrary[A] = Arbitrary(Gen.const(Monoid[A].empty))

    def combine(x: Arbitrary[A], y: Arbitrary[A]): Arbitrary[A] = Arbitrary {
      for
        xa <- x.arbitrary
        ya <- y.arbitrary
      yield Monoid[A].combine(xa, ya)
    }
  end given

end ArbitraryInstances

object ArbitraryInstances extends ArbitraryInstances