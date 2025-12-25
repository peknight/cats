package com.peknight.cats.demo.functor

import cats.Functor

import scala.concurrent.{ExecutionContext, Future}

object FunctorInstances:
  given Functor[Box] with
    def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
  end given

  val optionFunctor: Functor[Option] = new Functor[Option]:
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)

  def futureFunctor(using ExecutionContext): Functor[Future] = new Functor[Future]:
    def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa.map(f)
end FunctorInstances
