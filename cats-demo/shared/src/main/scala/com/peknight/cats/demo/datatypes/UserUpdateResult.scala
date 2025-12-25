package com.peknight.cats.demo.datatypes

sealed abstract class UserUpdateResult derives CanEqual
object UserUpdateResult:
  case class Succeeded(updatedUserId: Int) extends UserUpdateResult
  case object Failed extends UserUpdateResult
end UserUpdateResult
