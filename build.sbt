ThisBuild / version := "1.0.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

ThisBuild / organization := "io.psilicon"
ThisBuild / organizationName := "psilicon"
ThisBuild / organizationHomepage := Some(url("http://github.com/psilicon"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/psilicon/avro2s"),
    "scm:git@github.com:psilicon/avro2s.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "psilicon",
    name = "Psilicon",
    email = "hello@psilicon.io",
    url = url("http://github.com/psilicon")
  )
)

ThisBuild / description := "Avro to Scala case class generator"
ThisBuild / licenses := List(
  "Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
ThisBuild / homepage := Some(url("https://github.com/psilicon/avro2s"))

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

pgpPassphrase := sys.env.get("GPG_PASSPHRASE").map(_.toArray)

lazy val root = (project in file("."))
  .aggregate(avro2s.projectRefs ++ sbtAvro2s.projectRefs: _*)
  .settings(
    publish / skip := true
  )

lazy val avro2s = (projectMatrix in file("avro2s"))
  .settings(
    name := "avro2s",
    libraryDependencies ++= Seq(
      "org.apache.avro" % "avro" % versions.avro,
      "org.apache.avro" % "avro-compiler" % versions.avro,
      "io.circe" %% "circe-core" % "0.14.13",
      "io.circe" %% "circe-parser" % "0.14.13",
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.13.0",
      "org.scalameta" %% "scalameta" % "4.12.7",
      ("org.scalameta" %% "scalafmt-core" % "3.8.6")
        .cross(CrossVersion.for3Use2_13)
        .exclude("org.scalameta", "scalameta_2.13")
        .exclude("org.scalameta", "parsers_2.13")
        .exclude("org.scala-lang.modules", "scala-collection-compat_2.13"),
      // Test
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.slf4j" % "slf4j-api" % "1.7.36" % Test,
      "ch.qos.logback" % "logback-classic" % "1.2.10" % Test,
    ) ++ (
      if (scalaVersion.value.startsWith("2")) {
        Seq(
          "com.chuusai" %% "shapeless" % "2.3.13" % Test
        )
      } else {
        Seq()
      }
    ),
    Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
    // scalafmt uses scala.meta.transversers which scalameta only bundles into parsers_2.13 (not _3).
    // Add parsers_2.13 as unmanaged jar to avoid cross-version suffix conflict with parsers_3.
    Compile / unmanagedJars ++= {
      if (scalaVersion.value.startsWith("3")) {
        val jar = csrCacheDirectory.value / "https" / "repo1.maven.org" / "maven2" /
          "org" / "scalameta" / "parsers_2.13" / "4.12.7" / "parsers_2.13-4.12.7.jar"
        if (jar.exists()) Seq(Attributed.blank(jar))
        else {
          streams.value.log.warn("parsers_2.13-4.12.7.jar not found in Coursier cache — run `cs fetch org.scalameta:parsers_2.13:4.12.7`")
          Nil
        }
      } else Nil
    },
  ).jvmPlatform(scalaVersions = Seq("2.12.20", "2.13.16", "3.3.6"))

lazy val sbtAvro2s = (projectMatrix in file("sbt-avro2s"))
  .settings(
    name := "sbt-avro2s",
    scalaVersion := "2.12.20",
    crossSbtVersions := Seq(sbtVersion.value),
    crossPaths := true,
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    // Disable plain sbt-avro2s artifacts
    publishArtifact := false,
    Compile / publishArtifact := false,
    Test / publishArtifact := false
  ).jvmPlatform(scalaVersions = Seq("2.12.20")).dependsOn(avro2s)
  .enablePlugins(SbtPlugin)

lazy val versions = new {
  val avro = "1.12.1"
}
