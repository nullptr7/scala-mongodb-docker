package com.github.nullptr7
package scalamongodbdocker
package db

import cats._
import cats.implicits._
import exceptions.DatabaseException
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}

abstract class Repository[F[_]: LoggerFactory, Client] {

  final private val logger: SelfAwareStructuredLogger[F] = LoggerFactory[F].getLogger

  final def logAndRaise(databaseException: DatabaseException)(implicit
    ae:                                    ApplicativeError[F, Throwable]
  ): F[Unit] =
    logger.error(databaseException)("Repository error was raised") *> ae.raiseError[Unit](databaseException)

  protected[db] def clientF: F[Client]

}
