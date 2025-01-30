package com.github.nullptr7
package scalamongodbdocker

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.IpLiteralSyntax
import db.HealthCheckMongoDbRepositoryImpl
import org.http4s.{Request, Response}
import server.HealthRoute
import org.http4s.ember.server.EmberServerBuilder
import org.mongodb.scala.MongoClient
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

object Main extends IOApp {

  implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]

  private val uri:         String                                 = "mongodb://localhost:27017";
  private val client:      MongoClient                            = MongoClient(uri);
  private val healthRoute: Kleisli[IO, Request[IO], Response[IO]] =
    HealthRoute[IO](HealthCheckMongoDbRepositoryImpl.make[IO](IO.pure(client), "testdb")).healthRoute.orNotFound

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(host"localhost")
      .withPort(port"8080")
      .withHttpApp(healthRoute)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
