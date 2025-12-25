package com.peknight.cats.demo.introduction

import cats.Eq
import cats.syntax.eq.*
import cats.syntax.option.*
import com.peknight.cats.demo.data.Cat
import org.scalatest.flatspec.AnyFlatSpec

import java.util.Date

class EqFlatSpec extends AnyFlatSpec:

  "Eq" should "pass for Int" in {
    val eqInt = Eq[Int]
    assert(eqInt.eqv(123, 123))
    assert(!eqInt.eqv(123, 234))
    assert(123 === 123)
    assert(!(1.some === none[Int]))
    assert(1.some =!= none[Int])
  }

  "Eq" should "pass for Date" in {
    given Eq[Date] = Eq.instance[Date] { (date1, date2) =>
      date1.getTime === date2.getTime
    }
    val now = System.currentTimeMillis()
    val x = new Date(now)
    val y = new Date(now + 1)
    assert(x === x)
    assert(!(x === y))
  }

  "Eq" should "pass for Cat" in {
    val cat1 = Cat.garfield
    val cat2 = Cat.heathcliff

    assert(!(cat1 === cat2))
    assert(cat1 =!= cat2)

    val optionCat1 = cat1.some
    val optionCat2 = none[Cat]

    assert(!(optionCat1 === optionCat2))
    assert(optionCat1 =!= optionCat2)
  }
end EqFlatSpec
