package com.peknight.cats.demo.api

import cats.Applicative
import cats.syntax.functor.*
import cats.syntax.traverse.*
import com.peknight.cats.demo.client.UptimeClient

class UptimeService[F[_]: Applicative](client: UptimeClient[F]):
  def getTotalUptime(hostnames: List[String]): F[Int] =
    hostnames.traverse(client.getUptime).map(_.sum)
end UptimeService
