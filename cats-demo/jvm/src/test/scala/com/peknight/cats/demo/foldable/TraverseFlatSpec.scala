package com.peknight.cats.demo.foldable

import cats.data.Validated
import cats.data.Validated.Valid
import cats.syntax.applicative.*
import cats.syntax.apply.*
import cats.syntax.traverse.*
import cats.{Applicative, Traverse}
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.Future

class TraverseFlatSpec extends AsyncFlatSpec:
  type ErrorsOr[A] = Validated[List[String], A]

  def getUptime(hostname: String): Future[Int] = Future(hostname.length * 60)

  def oldCombine(accum: Future[List[Int]], host: String): Future[List[Int]] =
    val uptime = getUptime(host)
    for
      accum <- accum
      uptime <- uptime
    yield accum :+ uptime

  def newCombine(accum: Future[List[Int]], host: String): Future[List[Int]] =
    (accum, getUptime(host)).mapN(_ :+ _)

  def listTraverse[F[_] : Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum, func(item)).mapN(_ :+ _)
    }

  def listSequence[F[_] : Applicative, B](list: List[F[B]]): F[List[B]] = listTraverse(list)(identity)

  def processOption(inputs: List[Int]): Option[List[Int]] =
    listTraverse(inputs)(n => if n % 2 == 0 then Some(n) else None)

  def processValidated(inputs: List[Int]): ErrorsOr[List[Int]] = listTraverse(inputs) { n =>
    if n % 2 == 0 then Validated.valid(n)
    else Validated.invalid(List(s"$n is not even"))
  }

  val hostnames: List[String] = List("alpha.example.com", "beta.example.com", "gamma.demo.com")
  val allUptimesByFold: Future[List[Int]] = hostnames.foldLeft(Future(List.empty[Int])) { (accum, host) =>
    val uptime = getUptime(host)
    for
      accum <- accum
      uptime <- uptime
    yield accum :+ uptime
  }
  val allUptimes: Future[List[Int]] = Future.traverse(hostnames)(getUptime)
  val totalUptimeByListTraverse: Future[List[Int]] = listTraverse(hostnames)(getUptime)
  val totalUptime: Future[List[Int]] = Traverse[List].traverse(hostnames)(getUptime)
  val numbers: List[Future[Int]] = List(Future(1), Future(2), Future(3))
  val numbers2: Future[List[Int]] = Traverse[List].sequence(numbers)
  val hostnameUptimes: Future[List[Int]] = hostnames.traverse(getUptime)

  "Future Traverse" should "pass for all uptimes by fold" in {
    allUptimesByFold.map(i => assert(i === List(1020, 960, 840)))
  }

  "Future Traverse" should "pass for all uptimes" in {
    allUptimes.map(i => assert(i === List(1020, 960, 840)))
  }

  "Future Traverse" should "pass for total uptime by list traverse" in {
    totalUptimeByListTraverse.map(i => assert(i === List(1020, 960, 840)))
  }

  "Future Traverse" should "pass for total uptime" in {
    totalUptime.map(i => assert(i === List(1020, 960, 840)))
  }

  "Future Traverse" should "pass for numbers 2" in {
    numbers2.map(i => assert(i === List(1, 2, 3)))
  }

  "Future Traverse" should "pass for hostname uptimes" in {
    hostnameUptimes.map(i => assert(i === List(1020, 960, 840)))
  }

  "Future Traverse" should "pass for numbers sequence" in {
    // 这里要把泛型带上，否则编译不通过
    numbers.sequence[Future, Int].map(i => assert(i === List(1, 2, 3)))
  }

  "Traverse" should "pass" in {
    Future(List.empty[Int])
    List.empty[Int].pure[Future]
    assert(listSequence(List(Vector(1, 2), Vector(3, 4))) === Vector(List(1, 3), List(1, 4), List(2, 3), List(2, 4)))
    assert(listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6))) === Vector(
      List(1, 3, 5), List(1, 3, 6), List(1, 4, 5), List(1, 4, 6),
      List(2, 3, 5), List(2, 3, 6), List(2, 4, 5), List(2, 4, 6)
    ))
    assert(processOption(List(2, 4, 6)) === Some(List(2, 4, 6)))
    assert(processOption(List(1, 2, 3)) === None)
    assert(processValidated(List(2, 4, 6)) === Valid(List(2, 4, 6)))
    assert(processValidated(List(1, 2, 3)).isInvalid)
  }
end TraverseFlatSpec
