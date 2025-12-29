package com.peknight.cats.demo.api

import cats.data.Kleisli
import com.peknight.cats.demo.config.ServiceConfig

trait Service
object Service:
  val fromServiceConfig: Kleisli[Option, ServiceConfig, Service] = ???
end Service
