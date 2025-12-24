package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.introduction.PrintableInstances.given
import com.peknight.cats.demo.introduction.PrintableSyntax.*
import org.scalatest.flatspec.AnyFlatSpec

class PrintableFlatSpec extends AnyFlatSpec:
  "Printable" should "succeed" in {
    val cat = Cat("Java", 12, "Yellow")
    Printable.print(cat)
    cat.print
  }
end PrintableFlatSpec
