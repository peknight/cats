package com.peknight.cats.demo.syntax

import com.peknight.cats.demo.data.Json
import com.peknight.cats.demo.typeclass.JsonWriter

trait JsonSyntax:
  extension [A] (value: A)
    def toJson(using w: JsonWriter[A]): Json = w.write(value)
  end extension
end JsonSyntax
object JsonSyntax extends JsonSyntax
