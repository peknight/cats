package com.peknight.cats.demo.data

import cats.data.{NonEmptyList, Validated}
import cats.syntax.apply.*
import cats.syntax.validated.*
import com.peknight.cats.demo.typeclass.{Check, Predicate}

case class User(id: Int, name: String, age: Int, email: String = "")
object User:
  def apply(name: String, email: String): User = User(0, name, 0, email)

  type Errors = NonEmptyList[String]

  def createUser(username: String, email: String): Validated[Errors, User] =
    (checkUsername(username), checkEmail(email)).mapN(User.apply)

  def error(s: String): NonEmptyList[String] = NonEmptyList(s, Nil)

  def longerThan(n: Int): Predicate[Errors, String] = Predicate.lift(
    error(s"Must be longer than $n characters"),
    _.length > n
  )

  val alphanumeric: Predicate[Errors, String] = Predicate.lift(
    error(s"Must be all alphanumeric characters"),
    _.forall(_.isLetterOrDigit)
  )

  def contains(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char"),
    _.contains(char)
  )

  def containsOnce(char: Char): Predicate[Errors, String] = Predicate.lift(
    error(s"Must contain the character $char only once"),
    str => str.count(_ == char) == 1
  )

  val checkUsername: Check[Errors, String, String] = Check(longerThan(3).and(alphanumeric))

  val splitEmail: Check[Errors, String, (String, String)] = Check { _.split('@') match
    case Array(name, domain) => (name, domain).validNel[String]
    case _ => "Must contain a single @ character".invalidNel[(String, String)]
  }

  val checkLeft: Check[Errors, String, String] = Check(longerThan(0))

  val checkRight: Check[Errors, String, String] = Check(longerThan(3).and(contains('.')))

  val joinEmail: Check[Errors, (String, String), String] = Check { case (l, r) =>
    (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
  }

  val checkEmail: Check[Errors, String, String] = splitEmail.andThen(joinEmail)
end User
