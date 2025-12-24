package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.introduction.Json.{JsNull, JsObject, JsString}

object JsonWriterInstances:
  given JsonWriter[String] with
    def write(value: String): Json = JsString(value)
  end given

  given JsonWriter[Person] with
    def write(value: Person): Json = JsObject(Map(
      "name" -> JsString(value.name),
      "email" -> JsString(value.email)
    ))
  end given

  given optionWriter[A](using writer: JsonWriter[A]): JsonWriter[Option[A]] with
    def write(option: Option[A]): Json = option match
      case Some(aValue) => writer.write(aValue)
      case None => JsNull
  end optionWriter
end JsonWriterInstances
