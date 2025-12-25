package com.peknight.cats.demo.introduction

import cats.Show
import cats.syntax.show.*
import org.scalatest.flatspec.AnyFlatSpec

import java.util.Date

class ShowFlatSpec extends AnyFlatSpec:

  // 2.2.0版本后的cats不再需要`import cats.instances`，见https://github.com/typelevel/cats/releases/tag/v2.2.0
  // import cats.instances.int._
  // import cats.instances.string._
  "Show" should "pass for Int and String" in {
    val showInt = Show[Int]
    val showString = Show[String]
    val intAsString: String = showInt.show(123)
    val stringAsString: String = showString.show("abc")
    assert(intAsString == "123")
    assert(stringAsString == "abc")
    assert(123.show == "123")
    assert("abc".show == "abc")
  }

  "Show" should "pass for Date" in {
    import com.peknight.cats.demo.introduction.ShowInstances.given
    val dateShow1: Show[Date] = (date: Date) => s"${date.getTime}ms since the epoch."
    val dataShow2: Show[Date] = Show.show(date => s"${date.getTime}ms since the epoch.")
    given Show[Date] = Show.fromToString
    println(new Date().show)
    println(Cat("Java", 3, "yellow").show)
  }
end ShowFlatSpec
