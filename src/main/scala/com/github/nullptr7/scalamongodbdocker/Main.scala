package com.github.nullptr7
package scalamongodbdocker

import org.mongodb.scala.MongoClient

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

import com.comcast.ip4s.IpLiteralSyntax

import org.http4s.ember.server.EmberServerBuilder

import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

import db.{EmployeeMongoDbRepository, HealthCheckMongoDbRepository}
import server.{EmployeeRoutes, HealthRoute}

object Main extends IOApp {

  implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]

  private val uri: String = "mongodb://localhost:27017"

  override def run(args: List[String]): IO[ExitCode] =
    for {
      mongoClient  <- IO.pure(MongoClient(uri))
      healthRepo   <- HealthCheckMongoDbRepository.make[IO](mongoClient, "testdb")
      employeeRepo <- EmployeeMongoDbRepository.make[IO]("testdb1", "employee", mongoClient)

      allRoutes = HealthRoute[IO].make(healthRepo) <+> EmployeeRoutes[IO].make(employeeRepo)

      exitCode <- EmberServerBuilder
                    .default[IO]
                    .withHost(host"localhost")
                    .withPort(port"8080")
                    .withHttpApp(allRoutes.orNotFound)
                    .build
                    .use(_ => IO.never)
                    .as(ExitCode.Success)
    } yield exitCode

}
