package com.peknight.cats.demo.data

import com.peknight.cats.demo.typeclass.JsonWriter

sealed trait Json
object Json:
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  case object JsNull extends Json
  def toJson[A](value: A)(using w: JsonWriter[A]): Json = w.write(value)
end Json

