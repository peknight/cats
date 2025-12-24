package com.peknight.cats.demo.casestudy.testasync

trait UptimeClient[F[_]]:
  def getUptime(hostname: String): F[Int]
