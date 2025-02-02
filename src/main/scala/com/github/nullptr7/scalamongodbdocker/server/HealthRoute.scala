package com.github.nullptr7
package scalamongodbdocker
package server

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

import cats.effect._
import cats.effect.implicits.genTemporalOps
import cats.implicits._

import org.http4s._

import db.HealthCheckMongoDbRepository
import exceptions.DatabaseConnectionException

sealed abstract class HealthRoute[F[_]: Async] extends Http4sDslHelper[F] {

  def make(healthCheckMongoDbRepository: HealthCheckMongoDbRepository[F]): HttpRoutes[F]

}

object HealthRoute {

  def apply[F[_]: HealthRoute]: HealthRoute[F] = implicitly

  implicit def healthRouteForAsync[F[_]: Async]: HealthRoute[F] =
    new HealthRoute[F]() {
      import dsl._

      override def make(healthcheckRepo: HealthCheckMongoDbRepository[F]): HttpRoutes[F] =
        HttpRoutes.of[F] { case GET -> Root / "hello" =>
          healthcheckRepo
            .ping
            .timeout(FiniteDuration(1000, MILLISECONDS))
            .flatMap(_ => Ok("database connectivity is okay..."))
            .orRaise(DatabaseConnectionException())
        }

    }

}
