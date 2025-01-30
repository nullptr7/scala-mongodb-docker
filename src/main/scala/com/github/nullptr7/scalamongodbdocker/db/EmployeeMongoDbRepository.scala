package com.github.nullptr7
package scalamongodbdocker
package db

import org.mongodb.scala.MongoClient
import cats.data.EitherT
import cats.effect.kernel.Async
import exceptions.EmployeeDetailsFetchException
import models.Employee
import org.typelevel.log4cats.LoggerFactory

abstract class EmployeeMongoDbRepository[F[_]: LoggerFactory](override val clientF: F[MongoClient]) extends Repository[F, MongoClient] {
  def getAllEmployees: EitherT[F, EmployeeDetailsFetchException, List[Employee]]

}

object EmployeeMongoDbRepositoryImpl {

  def make[F[_]: Async: LoggerFactory](clientF: F[MongoClient]): EmployeeMongoDbRepository[F] =
    new EmployeeMongoDbRepository[F](clientF) {
      def getAllEmployees: EitherT[F, EmployeeDetailsFetchException, List[Employee]] =
        EitherT.leftT[F, List[Employee]](EmployeeDetailsFetchException(new NotImplementedError("Implement this code!")))
    }

}
