package com.github.nullptr7
package scalamongodbdocker
package db

import cats.effect.kernel.Async
import cats.implicits._

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider

import org.typelevel.log4cats.LoggerFactory

import scala.concurrent.Future

import exceptions.EmployeeDetailsFetchException
import models.Employee

abstract class EmployeeMongoDbRepository[F[_]: Async: LoggerFactory](dbName: String, collectionName: String) extends Repository[F, MongoClient] {

  private val async: Async[F] = implicitly[Async[F]]

  final def getAllEmployees: F[Seq[Employee]] =
    for {
      employee <- async
                    .fromFuture(getAllEmployeesFromFuture) // this one returns default to empty Seq if table does not exist
                    .map(_ => Seq(Employee("Scott", "Tiger", 100L))) //TODO: should have real DB call returning real data
                    .adaptErr(toEmployeeDetailsFetchException)

    } yield employee

  final private def getAllEmployeesFromFuture: F[Future[Seq[Employee]]] =
    async.delay {
      dbClient
        .getDatabase(dbName)
        .withCodecRegistry(fromRegistries(fromProviders(classOf[Employee]), MongoClient.DEFAULT_CODEC_REGISTRY))
        .getCollection[Employee](collectionName)
        .find()
        .toFuture()
    }

  final private def toEmployeeDetailsFetchException: PartialFunction[Throwable, EmployeeDetailsFetchException] = { case t: Throwable =>
    EmployeeDetailsFetchException(t)
  }

}

object EmployeeMongoDbRepository {

  def make[F[_]: Async: LoggerFactory](dbName: String, collectionName: String, mongoClient: MongoClient): F[EmployeeMongoDbRepository[F]] =
    new EmployeeMongoDbRepository[F](dbName, collectionName) {
      override protected[db] val dbClient: MongoClient = mongoClient
    }.pure[F]

}
