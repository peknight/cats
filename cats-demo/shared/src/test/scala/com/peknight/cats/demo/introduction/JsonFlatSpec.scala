package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.introduction.JsonSyntax.*
import com.peknight.cats.demo.introduction.JsonWriterInstances.given
import org.scalatest.flatspec.AnyFlatSpec

class JsonFlatSpec extends AnyFlatSpec:
  "Json" should "pass" in {
    println(Json.toJson(Person("Dave", "dave@example.com")))
    println(Person("Dave", "dave@example.com").toJson)
    println(summon[JsonWriter[String]])
    println(Json.toJson("A string!"))
    println(Json.toJson(Option("A string")))
  }
end JsonFlatSpec
