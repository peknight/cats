package com.peknight.cats.demo.introduction

object PrintableSyntax:
  extension [A] (value: A)
    def format(using p: Printable[A]): String = Printable.format(value)
    def print(using p: Printable[A]): Unit = Printable.print(value)
  end extension
end PrintableSyntax
