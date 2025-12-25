package com.peknight.cats.scalacheck

package object instances:
  object arbitrary extends ArbitraryInstances
  object gen extends GenInstances
  object cogen extends CogenInstances

  object nonEmptyList extends NonEmptyListInstances
  object tree extends TreeInstances
end instances
