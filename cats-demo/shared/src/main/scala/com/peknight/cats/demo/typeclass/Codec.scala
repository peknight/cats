package com.peknight.cats.demo.typeclass

trait Codec[A]:
  self =>
  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B]:
    def encode(value: B): String = self.encode(enc(value))
    def decode(value: String): B = dec(self.decode(value))
end Codec
object Codec:
  def encode[A](value: A)(using c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(using c: Codec[A]): A = c.decode(value)

  given stringCodec: Codec[String] with
    def encode(value: String): String = value
    def decode(value: String): String = value
  end stringCodec

  given intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)

  given booleanCodec: Codec[Boolean] = stringCodec.imap(_.toBoolean, _.toString)

  given doubleCodec: Codec[Double] = stringCodec.imap(_.toDouble, _.toString)
end Codec
