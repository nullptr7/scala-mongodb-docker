package com.github.nullptr7
package scalamongodbdocker
package db

import cats.effect._
import cats.effect.unsafe.implicits.global
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{MongoClient, MongoDatabase, SingleObservable}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

class HealthCheckMongoDbRepositoryTest extends AnyFlatSpec with MockitoSugar {

  "HealthCheckMongoDbRepository" should "be able to ping the database successfully" in {

    implicit def async:  Async[IO]  = IO.asyncForIO
    implicit def logger: Logger[IO] = Slf4jLogger.getLogger[IO]

    val mockMongoClient: MongoClient   = mock[MongoClient]
    val mockDatabase:    MongoDatabase = mock[MongoDatabase]
    val pingResponse:    Document      = Document("ok" -> 1)

    when(mockMongoClient.getDatabase("testDatabaseName")).thenReturn(mockDatabase)
    when(mockDatabase.runCommand[Document](any())(any(), any())).thenReturn(SingleObservable(pingResponse))

    val health = HealthCheckMongoDbRepositoryImpl.make[IO](IO.pure(mockMongoClient), "testDatabaseName")

    assert(health.ping.unsafeRunSync() == ())
  }

}
