import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

object dmm4sBuild extends Build {
  val appName = "dmm4s"
  val appOrganization = "com.github.daiksy"
  val appVersion  = "0.1-SNAPSHOT"
  val appScalaVersion = "2.10.3"

  val main = Project(
    appName,
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      organization := appOrganization,
      version := appVersion,
      scalaVersion := appScalaVersion,
      libraryDependencies ++= Seq(
        "org.slf4j" % "slf4j-api" % "1.7.2",
        "org.scalaj" %% "scalaj-http" % "0.3.12"
      ) ++ testDependencies,
      resolvers ++= Seq(
        "releases" at "http://oss.sonatype.org/content/repositories/releases",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
      )
    ) ++  formatSettings ++ coverallsSetting
  ).settings(SbtScalariform.scalariformSettings: _*).settings(appPublishSettings: _*)

  lazy val testDependencies = Seq(
	  "junit" % "junit" % "4.7" % "test",
	  "org.scalaz" %% "scalaz-core" % "7.0.0" % "test",
	  "org.specs2" %% "specs2" % "1.13" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test"
  )

  lazy val formatSettings = Seq(
    ScalariformKeys.preferences := FormattingPreferences()
    .setPreference(IndentWithTabs, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true)
  )

  lazy val coverallsSetting = Seq(
    CoverallsPlugin.singleProject: _*
  )

  lazy val appPublishSettings = Seq(
    // version is defined in version.sbt in order to support sbt-release
    organization := appOrganization,
    publishMavenStyle := true,
    credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT")) {
        Some("snapshots" at nexus + "content/repositories/snapshots")
      } else {
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      }
    },
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/daiksy/dmm4s</url>
        <licenses>
          <license>
            <name>Apache License, Version 2.0</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:daiksy/dmm4s.git</url>
          <connection>scm:git:git@github.com:daiksy/dmm4s.git</connection>
        </scm>
        <developers>
          <developer>
            <id>daiksy</id>
            <name>daiksy</name>
            <url>https://github.com/daiksy</url>
          </developer>
        </developers>
      )
  )
}
