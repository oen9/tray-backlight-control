ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1"
ThisBuild / organization     := "com.github.oen9"
ThisBuild / organizationName := "oen9"

lazy val root = (project in file("."))
  .settings(
    name := "tray-backlight-control",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC17",
      "dev.zio" %% "zio-macros-core" % "0.6.2",
      "dev.zio" %% "zio-macros-test" % "0.6.2",
      "com.formdev" % "flatlaf" % "0.27",
    ),
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
