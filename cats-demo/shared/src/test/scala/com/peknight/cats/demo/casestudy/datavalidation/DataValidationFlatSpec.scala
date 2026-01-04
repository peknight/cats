package com.peknight.cats.demo.casestudy.datavalidation

import cats.data.Kleisli
import cats.data.Validated.Valid
import cats.syntax.either.*
import cats.syntax.validated.*
import com.peknight.cats.demo.data.User
import com.peknight.cats.demo.typeclass.{CheckF, OldCheck}
import org.scalatest.flatspec.AnyFlatSpec

class DataValidationFlatSpec extends AnyFlatSpec:

  val aCheckF: CheckF[List[String], Int] = CheckF { v =>
    if v > 2 then v.asRight
    else List("Must be > 2").asLeft
  }

  val bCheckF: CheckF[List[String], Int] = CheckF { v =>
    if v < -2 then v.asRight
    else List("Must be < -2").asLeft
  }

  val checkF: CheckF[List[String], Int] = aCheckF.and(bCheckF)

  val a: OldCheck[List[String], Int] = OldCheck.pure { v =>
    if v > 2 then v.valid
    else List("Must be > 2").invalid
  }

  val b: OldCheck[List[String], Int] = OldCheck.pure { v =>
    if v < -2 then v.valid
    else List("Must be < -2").invalid
  }

  val check: OldCheck[List[String], Int] = a.and(b)

  val step1: Kleisli[List, Int, Int] = Kleisli(x => List(x + 1, x - 1))

  val step2: Kleisli[List, Int, Int] = Kleisli(x => List(x, -x))

  val step3: Kleisli[List, Int, Int] = Kleisli(x => List(x * 2, x / 2))

  val pipeline: Kleisli[List, Int, Int] = step1 andThen step2 andThen step3

  "Data Validation" should "pass" in {
    assert(checkF(5).isLeft)
    assert(checkF(0).isLeft)
    assert(check(5).isInvalid)
    assert(check(0).isInvalid)
    assert(User.createUser("Noel", "noel@underscore.io").isValid)
    assert(User.createUser("", "dave@underscore.io@io").isInvalid)
    assert(pipeline.run(20) === List(42, 10, -42, -10, 38, 9, -38, -9))
  }
end DataValidationFlatSpec
