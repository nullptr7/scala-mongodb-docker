package com.github.nullptr7
package scalamongodbdocker
package server

import cats.effect._
import cats.effect.implicits.genTemporalOps
import cats.implicits._
import exceptions.DatabaseConnectionException
import db.HealthCheckMongoDbRepository
import org.http4s._

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

final class HealthRoute[F[_]: Async] private (private val healthcheckRepo: HealthCheckMongoDbRepository[F]) extends Http4sDslHelper[F] {

  import dsl._

  val healthRoute: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root / "hello" =>
    healthcheckRepo
      .ping
      .timeout(FiniteDuration(1000, MILLISECONDS))
      .flatMap(_ => Ok("database connectivity is okay..."))
      .orRaise(DatabaseConnectionException())

  }

}

object HealthRoute {

  def apply[F[_]: Async](repository: HealthCheckMongoDbRepository[F]): HealthRoute[F] =
    new HealthRoute[F](repository)
}
