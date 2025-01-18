package com.github.nullptr7
package scalamongodbdocker
package db

import cats._
import cats.implicits._

import com.github.nullptr7.scalamongodbdocker.exceptions.DatabaseConnectionException
import org.typelevel.log4cats.Logger

abstract class Repository[F[_]: Logger, Client] {

  final protected[db] def logAndRaise(implicit
    ae: ApplicativeError[F, Throwable]
  ): PartialFunction[Throwable, F[Unit]] = { case t: Throwable =>
    Logger[F].error(t.getMessage) *> ae.raiseError[Unit](DatabaseConnectionException(t))

  }

  protected[db] def clientF: F[Client]

}
