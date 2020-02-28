ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.2-SNAPSHOT"
ThisBuild / organization     := "com.github.oen9"
ThisBuild / organizationName := "oen9"
maintainer                   := "oen.ult@gmail.com"

val zioVersion = "1.0.0-RC17"

lazy val root = (project in file("."))
  .settings(
    name := "tray-backlight-control",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-macros-core" % "0.6.2",
      "dev.zio" %% "zio-macros-test" % "0.6.2",
      "com.formdev" % "flatlaf" % "0.27",

      "dev.zio" %% "zio-test" % zioVersion % Test,
      "dev.zio" %% "zio-test-sbt" % zioVersion % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    scalacOptions ++= Seq(
      "-Xlint",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:higherKinds",
      "-Ymacro-annotations"
    )
  )
  .enablePlugins(JavaAppPackaging)
