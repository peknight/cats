package com.peknight.cats.demo.typeclass

import cats.Id

trait DemoMonad[F[_]]:
  def pure[A](a: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
  def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)((a: A) => pure(func(a)))
end DemoMonad
object DemoMonad:
  val idDemoMonad: DemoMonad[Id] = new DemoMonad[Id]:
    def pure[A](a: A): Id[A] = a
    override def map[A, B](value: Id[A])(func: A => B): Id[B] = func(value)
    def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] = f(value)
end DemoMonad

