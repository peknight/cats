package com.peknight.cats.demo.introduction

object JsonSyntax:
  extension [A] (value: A)
    def toJson(using w: JsonWriter[A]): Json = w.write(value)
  end extension
end JsonSyntax
