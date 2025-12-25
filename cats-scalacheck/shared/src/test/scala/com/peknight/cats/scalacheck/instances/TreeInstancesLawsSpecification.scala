package com.peknight.cats.scalacheck.instances

import cats.laws.discipline.MonadTests
import com.peknight.cats.data.Tree
import com.peknight.cats.scalacheck.instances.tree.given
import org.scalacheck.Properties

class TreeInstancesLawsSpecification extends Properties("TreeInstancesLaws"):
  include(MonadTests[Tree].monad[Int, String, Boolean].all)
end TreeInstancesLawsSpecification
