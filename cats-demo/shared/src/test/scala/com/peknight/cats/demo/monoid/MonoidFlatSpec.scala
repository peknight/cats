package com.peknight.cats.demo.monoid

import cats.Monoid
import cats.syntax.semigroup.*
import com.peknight.cats.demo.monoid.MonoidInstances.given
import org.scalatest.flatspec.AnyFlatSpec

class MonoidFlatSpec extends AnyFlatSpec:
  "Monoid" should "pass" in {
    assert(Monoid[Symbol].empty === Symbol(""))
    assert((Symbol("a") |+| Symbol("few") |+| Symbol("words")) === Symbol("afewwords"))
  }
end MonoidFlatSpec
