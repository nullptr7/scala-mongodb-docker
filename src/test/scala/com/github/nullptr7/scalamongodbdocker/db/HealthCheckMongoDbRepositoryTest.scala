package com.github.nullptr7
package scalamongodbdocker
package db

import org.mongodb.scala._
import org.mongodb.scala.bson.collection.immutable.Document

import cats.effect.IO

import munit.CatsEffectSuite
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class HealthCheckMongoDbRepositoryTest extends CatsEffectSuite with MockitoSugar {

  test("HealthCheckMongoDbRepository should be able to ping successfully") {

    implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

    val mockMongoClient: MongoClient   = mock[MongoClient]
    val mockDatabase:    MongoDatabase = mock[MongoDatabase]
    val pingResponse:    Document      = Document("ok" -> 1)

    when(mockMongoClient.getDatabase("admin")).thenReturn(mockDatabase)
    when(mockDatabase.runCommand[Document](any())(any(), any())).thenReturn(SingleObservable(pingResponse))

    val healthCheckMongoDbRepository = HealthCheckMongoDbRepositoryImpl.make[IO](IO.pure(mockMongoClient), "admin")

    assertIO(healthCheckMongoDbRepository.ping, ())
  }
}
