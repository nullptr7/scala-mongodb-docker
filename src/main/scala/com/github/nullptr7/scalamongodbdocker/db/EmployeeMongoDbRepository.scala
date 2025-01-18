package com.github.nullptr7
package scalamongodbdocker
package db

import cats.data.EitherT
import cats.effect.kernel.Async
import exceptions.EmployeeDetailsFetchException
import models.Employee
import org.mongodb.scala.MongoClient
import org.typelevel.log4cats.Logger

abstract class EmployeeMongoDbRepository[F[_]: Logger](override val clientF: F[MongoClient]) extends Repository[F, MongoClient] {
  def getAllEmployees: EitherT[F, EmployeeDetailsFetchException, List[Employee]]

}

object EmployeeMongoDbRepositoryImpl {

  def make[F[_]: Async: Logger](clientF: F[MongoClient]): EmployeeMongoDbRepository[F] =
    new EmployeeMongoDbRepository[F](clientF) {
      def getAllEmployees: EitherT[F, EmployeeDetailsFetchException, List[Employee]] =
        EitherT.leftT[F, List[Employee]](EmployeeDetailsFetchException(new NotImplementedError("Implement this code!")))
    }

}
