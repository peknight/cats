package com.peknight.cats.scalacheck.instances

import cats.data.NonEmptyList
import cats.kernel.laws.discipline.{MonoidTests, SemigroupTests}
import cats.laws.discipline.{AlternativeTests, FunctorFilterTests, MonadTests}
import com.peknight.cats.scalacheck.instances.arbitrary.given
import com.peknight.cats.scalacheck.instances.nonEmptyList.given
import com.peknight.cats.scalacheck.instances.test.arbitrary.given
import com.peknight.cats.scalacheck.instances.test.gen.given
import org.scalacheck.{Arbitrary, Properties, Test}

class ArbitraryInstancesLawsSpecification extends Properties("ArbitraryInstancesLaws"):
  override def overrideParameters(p: Test.Parameters): Test.Parameters =
    p.withMaxSize(16).withMinSuccessfulTests(16)

  include(MonadTests[Arbitrary].monad[Int, String, Boolean].all)
  include(AlternativeTests[Arbitrary].alternative[Int, String, Boolean].all)
  include(FunctorFilterTests[Arbitrary].functorFilter[Int, String, Boolean].all)
  include(MonoidTests[Arbitrary[String]].monoid.all)
  include(SemigroupTests[Arbitrary[NonEmptyList[Int]]].semigroup.all)
end ArbitraryInstancesLawsSpecification
