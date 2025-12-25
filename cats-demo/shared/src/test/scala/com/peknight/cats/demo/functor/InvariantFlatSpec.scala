package com.peknight.cats.demo.functor

import com.peknight.cats.demo.data.Box
import com.peknight.cats.demo.typeclass.Codec
import org.scalatest.flatspec.AnyFlatSpec

class InvariantFlatSpec extends AnyFlatSpec:
  "Codec" should "pass" in {
    assert(Codec.encode(123.4) === "123.4")
    assert(Codec.decode[Double]("123.4") === 123.4)
    assert(Codec.encode[Box[Double]](Box(123.4)) === "123.4")
    assert(Codec.decode[Box[Double]]("123.4") === Box(123.4))
  }
end InvariantFlatSpec
