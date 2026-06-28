package com.peknight.cats.demo.data

import com.peknight.cats.demo.data.Json.{JsObject, JsString}
import com.peknight.cats.demo.typeclass.JsonWriter

final case class Person(name: String, email: String)
object Person:
  given JsonWriter[Person] with
    def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
  end given
end Person
