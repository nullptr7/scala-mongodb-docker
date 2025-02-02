package com.github.nullptr7
package scalamongodbdocker
package server

import cats.effect.kernel.Async
import cats.implicits._

import io.circe.generic.auto._
import io.circe.syntax._

import org.http4s.HttpRoutes
import org.http4s.circe._

import db.EmployeeMongoDbRepository

sealed abstract class EmployeeRoutes[F[_]] extends Http4sDslHelper[F] {

  def make(employeeRepo: EmployeeMongoDbRepository[F]): HttpRoutes[F]

}

object EmployeeRoutes {

  def apply[F[_]: EmployeeRoutes]: EmployeeRoutes[F] = implicitly

  implicit def employeeRoutesForAsync[F[_]: Async]: EmployeeRoutes[F] =
    new EmployeeRoutes[F] {

      override def make(employeeRepo: EmployeeMongoDbRepository[F]): HttpRoutes[F] = {
        import dsl._
        HttpRoutes.of[F] { case GET -> Root / "employees" / "all" =>
          employeeRepo
            .getAllEmployees
            .flatMap(employees => Ok(employees.asJson))

        }
      }

    }

}
