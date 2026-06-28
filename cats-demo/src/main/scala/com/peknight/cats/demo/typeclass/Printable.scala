package com.peknight.cats.demo.typeclass

trait Printable[A]:
  def format(value: A): String
  // 3.5.5 Contravariant
  def contramap[B](func: B => A): Printable[B] = (b: B) => format(func(b))
end Printable
object Printable:
  def format[A](value: A)(using p: Printable[A]): String = p.format(value)
  def print[A](value: A)(using Printable[A]): Unit = println(format(value))

  given Printable[String] with
    def format(value: String): String = value
  end given

  given Printable[Int] with
    def format(value: Int): String = value.toString
  end given

  given Printable[Boolean] with
    def format(value: Boolean): String = if value then "yes" else "no"
  end given
end Printable
