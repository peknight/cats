package com.peknight.cats.demo.casestudy.testasync

import scala.concurrent.Future

trait RealUptimeClinet extends UptimeClient[Future]:
  def getUptime(hostname: String): Future[Int]