package com.peknight.cats.demo.casestudy.crdt

import com.peknight.cats.demo.data.GCounter
import org.scalatest.flatspec.AnyFlatSpec

class CRDTFlatSpec extends AnyFlatSpec:
  "CRDT" should "pass" in {
    val g1 = Map("a" -> 7, "b" -> 3)
    val g2 = Map("a" -> 2, "b" -> 5)
    val counter = GCounter[Map, String, Int]
    val merged = counter.merge(g1, g2)
    val total = counter.total(merged)
    assert(merged === Map("a" -> 7, "b" -> 5))
    assert(total === 12)
  }
end CRDTFlatSpec
