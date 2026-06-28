package com.peknight.cats.demo.client

import scala.concurrent.Future

trait RealUptimeClient extends UptimeClient[Future]:
  def getUptime(hostname: String): Future[Int]
end RealUptimeClient