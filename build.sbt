import com.peknight.build.gav
import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val cats = (project in file("."))
  .settings(name := "cats")
  .aggregate(catsCore.projectRefs *)
  .aggregate(catsScodecBits.projectRefs *)
  .aggregate(catsScalaCheck.projectRefs *)
  .aggregate(catsDemo.projectRefs *)

lazy val catsCore = (projectMatrix in file("cats-core"))
  .settings(name := "cats-core")
  .settings(libraryDependencies ++= dependencies(typelevel.cats))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
  .nativePlatform(scalaVersions = Seq(scala.scala3.version))

lazy val catsScodecBits = (projectMatrix in file("cats-scodec-bits"))
  .settings(name := "cats-scodec-bits")
  .settings(libraryDependencies ++= dependencies(
    typelevel.cats,
    scodec.bits
  ))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
  .nativePlatform(scalaVersions = Seq(scala.scala3.version))

lazy val catsScalaCheck = (projectMatrix in file("cats-scalacheck"))
  .dependsOn(catsCore)
  .settings(name := "cats-scalacheck")
  .settings(libraryDependencies ++= dependencies(
    typelevel.cats,
    peknight.scalaCheck
  ))
  .settings(libraryDependencies ++= testDependencies(typelevel.cats.laws))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))

lazy val catsDemo = (projectMatrix in file("cats-demo"))
  .dependsOn(catsCore)
  .settings(name := "cats-demo")
  .settings(libraryDependencies ++= dependencies(typelevel.cats))
  .settings(libraryDependencies ++= testDependencies(scalaTest))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
