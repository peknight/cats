package com.peknight.cats.scalacheck.instances

import cats.{Alternative, Eval, Functor, FunctorFilter, Monad, Monoid}
import com.peknight.cats.scalacheck.instances
import org.scalacheck.{Gen, GenOps}

private[scalacheck] trait GenInstances extends GenInstances2:
  given Monad[Gen] with Alternative[Gen] with FunctorFilter[Gen] with
    def pure[A](x: A): Gen[A] = Gen.const(x)

    def flatMap[A, B](fa: Gen[A])(f: A => Gen[B]): Gen[B] = fa.flatMap(f)

    def tailRecM[A, B](a: A)(f: A => Gen[Either[A, B]]): Gen[B] = Gen.tailRecM(a)(f)

    def empty[A]: Gen[A] = Gen.fail

    def combineK[A](x: Gen[A], y: Gen[A]): Gen[A] = GenOps.gen { (params, seed) =>
      val xGen = GenOps.doApply(x)(params, seed)
      if xGen.retrieve.isDefined then xGen else GenOps.doApply(y)(params, xGen.seed)
    }

    def functor: Functor[Gen] = this

    def mapFilter[A, B](fa: Gen[A])(f: A => Option[B]): Gen[B] = fa.flatMap { a => f(a) match
      case Some(b) => Gen.const(b)
      case _ => Gen.fail
    }

    override def product[A, B](fa: Gen[A], fb: Gen[B]): Gen[(A, B)] = Gen.zip(fa, fb)

    override def map2Eval[A, B, Z](fa: Gen[A], fb: Eval[Gen[B]])(f: (A, B) => Z): Eval[Gen[Z]] =
      Eval.later(map2(fa, Gen.lzy(fb.value))(f))
  end given

  given [A: Monoid]: Monoid[Gen[A]] with
    def empty: Gen[A] = Gen.const(Monoid[A].empty)

    def combine(x: Gen[A], y: Gen[A]): Gen[A] =
      for
        xa <- x
        ya <- y
      yield Monoid[A].combine(xa, ya)
  end given

end GenInstances

object GenInstances extends GenInstances
