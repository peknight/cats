package com.peknight.cats.demo.data

import cats.data.Kleisli
import com.peknight.cats.demo.config.DbConfig

case class Db(usernames: Map[Int, String], passwords: Map[String, String])
object Db:
  val fromDbConfig: Kleisli[Option, DbConfig, Db] = Kleisli(config => None)
end Db
