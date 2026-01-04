package com.peknight.cats.demo.client

import cats.Id

class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Id]:
  def getUptime(hostname: String): Int = hosts.getOrElse(hostname, 0)
end TestUptimeClient
