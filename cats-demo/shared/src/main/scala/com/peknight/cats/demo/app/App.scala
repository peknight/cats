package com.peknight.cats.demo.app

import cats.data.Kleisli
import com.peknight.cats.demo.api.Service
import com.peknight.cats.demo.config.AppConfig
import com.peknight.cats.demo.data.Db

class App(db: Db, service: Service)
object App:
  def appFromAppConfig: Kleisli[Option, AppConfig, App] =
    for
      db <- Db.fromDbConfig.local[AppConfig](_.dbConfig)
      sv <- Service.fromServiceConfig.local[AppConfig](_.serviceConfig)
    yield new App(db, sv)
end App
