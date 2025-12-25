package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.data.{Box, Cat}
import com.peknight.cats.demo.syntax.printable.*
import com.peknight.cats.demo.typeclass.Printable
import org.scalatest.flatspec.AnyFlatSpec

class PrintableFlatSpec extends AnyFlatSpec:
  "Printable" should "pass for print" in {
    val cat = Cat.java
    Printable.print(cat)
    cat.print
  }

  "Printable" should "pass for format" in {
    assert(Printable.format("hello") === "hello")
    assert(Printable.format(true) === "yes")
    assert(Printable.format(Box("hello world")) === "hello world")
    assert(Printable.format(Box(true)) === "yes")
    assert(Printable.format(Box(123)) === "123")
  }
end PrintableFlatSpec
