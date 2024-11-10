package com.github.nullptr7
package scalamongodbdocker
package db

import exceptions.DatabaseConnectionException

import cats._
import cats.implicits._

import org.typelevel.log4cats.Logger

abstract class Repository[F[_]: Logger, Client] {

  final protected[db] def logAndRaise(implicit
    ae: ApplicativeError[F, Throwable]
  ): PartialFunction[Throwable, F[Unit]] = { case t: Throwable =>
    Logger[F].error(t.getMessage) *> ae.raiseError[Unit](DatabaseConnectionException(t))

  }

  protected[db] def clientF: F[Client]

}
