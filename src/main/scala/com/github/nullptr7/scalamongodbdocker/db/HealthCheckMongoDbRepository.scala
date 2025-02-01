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
      client <- clientF
      _      <-
        Async[F]
          .fromFuture[Document] {
            client
              .getDatabase(dbName)
              .runCommand(Document("ping" -> 1))
              .as[F]
          }
    } yield ()

}

object HealthCheckMongoDbRepositoryImpl {

  def make[F[_]: Async: LoggerFactory](cf: F[MongoClient], dbName: String): HealthCheckMongoDbRepository[F] =
    new HealthCheckMongoDbRepository[F](dbName) {
      override protected[db] val clientF: F[MongoClient] = cf

    }

}
