package com.peknight.cats.scodec.bits.instances

import cats.Monoid
import com.peknight.cats.scodec.bits.instances
import scodec.bits.ByteVector

trait ByteVectorInstances:
  given Monoid[ByteVector] with
    def empty: ByteVector = ByteVector.empty
    def combine(x: ByteVector, y: ByteVector): ByteVector = x ++ y
  end given
end ByteVectorInstances
object ByteVectorInstances extends ByteVectorInstances
