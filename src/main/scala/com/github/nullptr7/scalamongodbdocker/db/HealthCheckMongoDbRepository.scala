package com.github.nullptr7
package scalamongodbdocker
package db

import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.Document

import cats.effect.kernel.Async
import cats.implicits._

import org.typelevel.log4cats.LoggerFactory

import utils.ImplicitsOps.FromFutureToAsyncOps

sealed abstract class HealthCheckMongoDbRepository[F[_]: Async: LoggerFactory](dbName: String) extends Repository[F, MongoClient] {

  final def ping: F[Unit] =
    for {
      _ <-
        Async[F]
          .fromFuture[Document] {
            dbClient
              .getDatabase(dbName)
              .runCommand(Document("ping" -> 1))
              .as[F]
          }
    } yield ()

}

object HealthCheckMongoDbRepository {

  def make[F[_]: Async: LoggerFactory](mongoClient: MongoClient, dbName: String): F[HealthCheckMongoDbRepository[F]] =
    new HealthCheckMongoDbRepository[F](dbName) {
      override protected[db] val dbClient: MongoClient = mongoClient

    }.pure[F]

}
