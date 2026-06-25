package com.peknight.cats.demo.monad

import org.scalatest.flatspec.AnyFlatSpec

import cats.data.Reader
import com.peknight.cats.demo.data.{Cat, Db}
import cats.syntax.applicative.*

class ReaderFlatSpec extends AnyFlatSpec:
  "Reader" should "pass" in {
    type DbReader[A] = Reader[Db, A]

    def findUsername(userId: Int): DbReader[Option[String]] = Reader(_.usernames.get(userId))
    def checkPassword(username: String, password: String): DbReader[Boolean] =
      Reader(_.passwords.get(username).contains(password))
    def checkLogin(userId: Int, password: String): DbReader[Boolean] =
      for
        usernameOption <- findUsername(userId)
        passwordOk <- usernameOption
          .map(username => checkPassword(username, password))
          .getOrElse(false.pure[DbReader])
      yield passwordOk

    val catName: Reader[Cat, String] = Reader(_.name)
    val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello $name")
    val feedKitty: Reader[Cat, String] = Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")
    val greetAndFeed: Reader[Cat, String] =
      for
        greet <- greetKitty
        feed <- feedKitty
      yield s"$greet. $feed."
    val users = Map(1 -> "dade", 2 -> "kate", 3 -> "margo")
    val passwords = Map("dade" -> "zerocool", "kate" -> "acidburn", "margo" -> "secret")
    val db = Db(users, passwords)

    assert(catName.run(Cat.garfield) === Cat.garfield.name)
    assert(greetKitty.run(Cat.garfield) === s"Hello ${Cat.garfield.name}")
    assert(greetAndFeed(Cat.garfield) === s"Hello ${Cat.garfield.name}. Have a nice bowl of ${Cat.garfield.favoriteFood}.")
    assert(greetAndFeed(Cat.heathcliff) === s"Hello ${Cat.heathcliff.name}. Have a nice bowl of ${Cat.heathcliff.favoriteFood}.")
    assert(checkLogin(1, "zerocool").run(db))
    assert(!checkLogin(4, "davinci").run(db))
  }
end ReaderFlatSpec
