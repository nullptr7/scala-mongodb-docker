import Dependencies.*

import scala.util.Try

ThisBuild / organization := "com.github.nullptr7"
ThisBuild / scalaVersion := "2.13.16"

lazy val `scala-mongodb-docker` =
  project
    .in(file("."))
    .settings(name := "scala-mongodb-docker")
    .settings(scalafmtOnCompile := true)
    .settings(commonSettings)
    .settings(autoImportSettings)
    .settings(dependencies)

val checkFormatting = taskKey[Unit]("Lists all projects in the build")

checkFormatting := {
  val taskKeyName = "scalafmtCheckAll"
  val structure   = buildStructure.value
  val state       = Keys.state.value
  val extracted   = Project.extract(state)
  val allProjects = buildStructure.value.allProjectRefs
  val logger      = streams.value.log

  allProjects.foreach { projectRef =>
    logger.info(s"Running for ${projectRef.project}")
    Try(extracted.runTask(projectRef / TaskKey[Unit](taskKeyName), state))
      .foreach(_ => ()) // discard any outcome.
  }

}

lazy val commonSettings = {
  lazy val commonScalacOptions =
    Seq(
      Compile / console / scalacOptions := {
        (Compile / console / scalacOptions)
          .value
          .filterNot(_.contains("wartremover"))
          .filterNot(Scalac.Lint.toSet)
          .filterNot(Scalac.FatalWarnings.toSet) :+ "-Wconf:any:silent"
      },
      Test / console / scalacOptions :=
        (Compile / console / scalacOptions).value
    )

  lazy val otherCommonSettings =
    Seq(
      update / evictionWarningOptions := EvictionWarningOptions.empty
      // cs launch scalac:3.3.1 -- -Wconf:help
      // src is not yet available for Scala3
      // scalacOptions += s"-Wconf:src=${target.value}/.*:s",
    )

  Seq(
    commonScalacOptions,
    otherCommonSettings
  ).reduceLeft(_ ++ _)
}

lazy val autoImportSettings =
  Seq(
    scalacOptions +=
      Seq(
        "java.lang",
        "scala",
        "scala.Predef",
        "scala.annotation",
        "scala.util.chaining"
      ).mkString(start = "-Yimports:", sep = ",", end = ""),
    Test / scalacOptions +=
      Seq(
        "org.scalacheck",
        "org.scalacheck.Prop"
      ).mkString(start = "-Yimports:", sep = ",", end = "")
  )

lazy val dependencies =
  Seq(
    libraryDependencies ++= Seq(
      is.cir.ciris,
      org.http4s.`http4s-dsl`,
      org.http4s.`http4s-circe`,
      org.http4s.`http4s-ember-client`,
      org.http4s.`http4s-ember-server`,
      org.mongodb.scala.`mongo-scala-driver`,
      org.slf4j.`slf4j-simple`,
      org.typelevel.`log4cats-slf4j`,
      org.typelevel.`cats-effect`,
      org.typelevel.`cats-core`,
      Dependencies.`io`.circe.`circe-generic`
//      com.github.ghostdogpr.caliban,
//      com.github.ghostdogpr.`caliban-quick`
    ),
    libraryDependencies ++= Seq(
      com.eed3si9n.expecty.expecty,
      org.scalacheck.scalacheck,
      org.typelevel.`discipline-munit`,
      org.scalatestplus.mockito45,
      org.scalatest.scalatest,
      org.typelevel.`munit-cats-effect-3`
    ).map(_ % Test)
  )
