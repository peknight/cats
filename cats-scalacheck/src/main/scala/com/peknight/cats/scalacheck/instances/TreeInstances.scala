package com.peknight.cats.scalacheck.instances

import com.peknight.cats.data.Tree
import org.scalacheck.{Arbitrary, Gen}

trait TreeInstances:
  private def gen[T](genT: Gen[T], maxSize: Int): Gen[Tree[T]] =
    require(maxSize > 0)
    Gen.choose(1, maxSize).flatMap(size => Gen.tailRecM[(List[Int], List[Option[T]]), Tree[T]]((List(size), Nil)) {
      case (Nil, acc) => Gen.const(Right(Tree.fromOptionList(acc)))
      case (1 :: restSize, acc) => genT.map(elem => Left((restSize, Some(elem) :: acc)))
      case (sizeHead :: sizeTail, acc) => Gen.choose(1, sizeHead - 1).map(leftSize => Left((
        leftSize :: sizeHead - leftSize :: sizeTail,
        None :: acc
      )))
    })
  given treeArbitrary[A: Arbitrary]: Arbitrary[Tree[A]] = Arbitrary(gen(Arbitrary.arbitrary[A], 16))
end TreeInstances
object TreeInstances extends TreeInstances
