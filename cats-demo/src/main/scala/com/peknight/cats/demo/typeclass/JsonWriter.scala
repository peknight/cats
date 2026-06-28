package com.peknight.cats.demo.typeclass

import com.peknight.cats.demo.data.Json
import com.peknight.cats.demo.data.Json.{JsNull, JsString}

trait JsonWriter[A]:
  def write(value: A): Json
end JsonWriter
object JsonWriter:
  given JsonWriter[String] with
    def write(value: String): Json = JsString(value)
  end given
  given optionWriter[A](using writer: JsonWriter[A]): JsonWriter[Option[A]] with
    def write(option: Option[A]): Json = option match
      case Some(aValue) => writer.write(aValue)
      case None => JsNull
  end optionWriter
end JsonWriter
