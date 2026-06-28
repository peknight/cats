package com.peknight.cats.demo.monad

import cats.syntax.either.*
import com.peknight.cats.demo.data.LoginError.{PasswordIncorrect, UnexpectedError, UserNotFound}
import com.peknight.cats.demo.data.{LoginError, LoginUser}
import org.scalatest.flatspec.AnyFlatSpec

class LoginFlatSpec extends AnyFlatSpec:
  "Login" should "pass" in {
    type LoginResult = Either[LoginError, LoginUser]

    def handleError(error: LoginError): Unit = error match
      case UserNotFound(u) => println(s"User not found: $u")
      case PasswordIncorrect(u) => println(s"Password incorrect: $u")
      case UnexpectedError => println(s"Unexpected error")

    val result1: LoginResult = LoginUser("dave", "passw0rd").asRight
    val result2: LoginResult = UserNotFound("dave").asLeft

    result1.fold(handleError, println)
    result2.fold(handleError, println)
  }
end LoginFlatSpec
