package com.github.nullptr7
package scalamongodbdocker
package db

import org.mongodb.scala.MongoClient

import cats.data.EitherT
import cats.effect.kernel.Async

import com.github.nullptr7.scalamongodbdocker.exceptions.EmployeeDetailsFetchException
import com.github.nullptr7.scalamongodbdocker.models.Employee
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
