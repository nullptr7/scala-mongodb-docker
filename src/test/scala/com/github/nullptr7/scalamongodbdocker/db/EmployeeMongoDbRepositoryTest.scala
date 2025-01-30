package com.github.nullptr7
package scalamongodbdocker
package db

import cats.effect._
import munit.CatsEffectSuite
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mongodb.scala._
import org.mongodb.scala.bson.collection.immutable.Document
import org.scalatestplus.mockito.MockitoSugar
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.Slf4jFactory

class EmployeeMongoDbRepositoryTest extends CatsEffectSuite with MockitoSugar {

  test("EmployeeMongoDbRepository should be initialized") {

    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]

    val mockMongoClient: MongoClient   = mock[MongoClient]
    val mockDatabase:    MongoDatabase = mock[MongoDatabase]
    val pingResponse:    Document      = Document("ok" -> 1)

    when(mockMongoClient.getDatabase("admin")).thenReturn(mockDatabase)
    when(mockDatabase.runCommand[Document](any[Document]())(any(), any())).thenReturn(SingleObservable(pingResponse))

    val employeeMongoDbRepository = EmployeeMongoDbRepositoryImpl.make[IO](IO.pure(mockMongoClient))

    employeeMongoDbRepository
      .getAllEmployees
      .value
      .map { fail =>
        assert(fail.isLeft)
      }
  }
}
