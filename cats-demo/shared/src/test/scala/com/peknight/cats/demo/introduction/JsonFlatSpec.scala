package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.data.{Json, Person}
import com.peknight.cats.demo.syntax.json.*
import com.peknight.cats.demo.typeclass.JsonWriter
import org.scalatest.flatspec.AnyFlatSpec

class JsonFlatSpec extends AnyFlatSpec:
  "Json" should "pass" in {
    summon[JsonWriter[String]]
    println(Json.toJson(Person("Dave", "dave@example.com")))
    println(Person("Dave", "dave@example.com").toJson)
    println(Json.toJson("A string!"))
    println(Json.toJson(Option("A string")))
  }
end JsonFlatSpec
