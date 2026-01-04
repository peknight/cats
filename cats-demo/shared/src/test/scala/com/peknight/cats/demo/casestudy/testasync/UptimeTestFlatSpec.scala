package com.peknight.cats.demo.casestudy.testasync

import com.peknight.cats.demo.api.UptimeService
import com.peknight.cats.demo.client.TestUptimeClient
import org.scalatest.flatspec.AnyFlatSpec

class UptimeTestFlatSpec extends AnyFlatSpec:
  "Total uptime test" should "pass" in {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    assert(actual === expected)
  }
end UptimeTestFlatSpec
