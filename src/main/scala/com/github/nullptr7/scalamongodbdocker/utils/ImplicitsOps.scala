package com.github.nullptr7
package scalamongodbdocker
package utils

import scala.concurrent.Future

import org.mongodb.scala._

import cats.Applicative
import cats.effect.kernel.Async
import cats.implicits._

object ImplicitsOps {

  implicit class FromFutureToAsyncOps[A](singleObservable: SingleObservable[A]) {
    def as[F[_]: Async]: F[Future[A]] =
      Async[F].delay(singleObservable.toFuture())

  }

  implicit class FToUnitOps[F[_]: Applicative, A](fa: F[A]) {
    def unit: F[Unit] =
      fa *> Applicative[F].pure(())

  }

}
