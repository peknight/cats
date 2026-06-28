package com.peknight.cats.demo.data

sealed abstract class HttpError

object HttpError:
  final case class NotFound(item: String) extends HttpError
  final case class BadRequest(msg: String) extends HttpError
end HttpError
