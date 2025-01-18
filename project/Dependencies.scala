import sbt._

object Dependencies {

  object com {

    object eed3si9n {

      object expecty {
        val expecty =
          "com.eed3si9n.expecty" %% "expecty" % "0.16.0"
      }

    }

    object github {

      object ghostdogpr {
        val caliban =
          "com.github.ghostdogpr" %% "caliban" % "2.9.0"

        val `caliban-quick` =
          "com.github.ghostdogpr" %% "caliban-quick" % "2.9.0"

      }

    }

  }

  object org {

    object scalacheck {
      val scalacheck =
        "org.scalacheck" %% "scalacheck" % "1.18.1"
    }

    object scalamock {
      val scalamock =
        "org.scalamock" %% "scalamock" % "5.1.0"
    }

    object scalatest {

      val scalatest =
        "org.scalatest" %% "scalatest" % "3.1.0"
    }

    object scalatestplus {
      val mockito45 =
        "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0"
    }

    object scalameta {
      val munit =
        "org.scalameta" %% "munit" % "1.0.2"

      val `munit-scalacheck` =
        moduleId("munit-scalacheck")

      private def moduleId(artifact: String): ModuleID =
        "org.scalameta" %% artifact % "1.0.0"
    }

    object mockito {
      val `mockito-scala` =
        "org.mockito" %% "mockito-scala" % "1.17.37"
    }

    object typelevel {
      val `discipline-munit` =
        "org.typelevel" %% "discipline-munit" % "2.0.0"

      val `log4cats-slf4j` =
        "org.typelevel" %% "log4cats-slf4j" % "2.7.0"

      val `log4cats-noop` =
        "org.typelevel" %% "log4cats-noop" % "2.7.0"

      val `munit-cats-effect-3` =
        "org.typelevel" %% "munit-cats-effect" % "2.0.0"
    }

    object http4s {
      private[this] val http4sVersion =
        "0.23.18"

      val `http4s-ember-client` =
        "org.http4s" %% "http4s-ember-client" % http4sVersion

      val `http4s-ember-server` =
        "org.http4s" %% "http4s-ember-server" % http4sVersion

      val `http4s-dsl` =
        "org.http4s" %% "http4s-dsl" % http4sVersion
    }

    object mongodb {

      object scala {
        val `mongo-scala-driver` =
          "org.mongodb.scala" %% "mongo-scala-driver" % "5.2.0"
      }

    }

  }

  object is {

    object cir {
      val ciris =
        "is.cir" %% "ciris" % "3.6.0"
    }

  }

}
