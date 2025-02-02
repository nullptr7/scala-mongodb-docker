package com.github.nullptr7
package scalamongodbdocker
package db

import cats._
import cats.implicits._

import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}

import exceptions.DatabaseException

abstract class Repository[F[_]: LoggerFactory, Client] {

  final private val logger: SelfAwareStructuredLogger[F] = LoggerFactory[F].getLogger

  final def logAndRaise[A](databaseException: DatabaseException)(implicit
    ae:                                    ApplicativeError[F, Throwable]
  ): F[A] =
    logger.error(databaseException)("Repository error was raised") *> ae.raiseError[A](databaseException)

  protected[db] def dbClient: Client

}
