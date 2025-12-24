package com.peknight.cats.demo.introduction

import com.peknight.cats.demo.functor.Box

object PrintableInstances:
  given Printable[String] with
    def format(value: String): String = value
  end given

  given Printable[Int] with
    def format(value: Int): String = value.toString
  end given

  given Printable[Boolean] with
    def format(value: Boolean): String = if value then "yes" else "no"
  end given

  given Printable[Cat] with
    def format(cat: Cat): String =
      val name = Printable.format(cat.name)
      val age = Printable.format(cat.age)
      val color = Printable.format(cat.color)
      s"$name is a $age year-old $color cat."
  end given

  given boxPrintable[A](using p: Printable[A]): Printable[Box[A]] = p.contramap(_.value)
end PrintableInstances
