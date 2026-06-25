package com.peknight.cats.demo.client

trait UptimeClient[F[_]]:
  def getUptime(hostname: String): F[Int]
end UptimeClient
