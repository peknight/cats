import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val cats = (project in file("."))
  .settings(name := "cats")
  .aggregate(
    catsCore.jvm,
    catsCore.js,
    catsCore.native,
    catsScodecBits.jvm,
    catsScodecBits.js,
    catsScodecBits.native,
    catsScalaCheck.jvm,
    catsScalaCheck.js,
    catsScalaCheck.native,
    catsDemo.jvm,
    catsDemo.js,
    catsDemo.native,
  )

lazy val catsCore = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("cats-core"))
  .settings(name := "cats-core")
  .settings(crossDependencies(typelevel.cats))

lazy val catsScodecBits = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("cats-scodec-bits"))
  .settings(name := "cats-scodec-bits")
  .settings(crossDependencies(
    typelevel.cats,
    scodec.bits
  ))

lazy val catsScalaCheck = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("cats-scalacheck"))
  .dependsOn(catsCore)
  .settings(name := "cats-scalacheck")
  .settings(crossDependencies(
    typelevel.cats,
    peknight.scalaCheck
  ))
  .settings(crossTestDependencies(typelevel.cats.laws))

lazy val catsDemo = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("cats-demo"))
  .dependsOn(catsCore)
  .settings(name := "cats-demo")
  .settings(crossDependencies(typelevel.cats))
  .settings(crossTestDependencies(
    typelevel.cats.laws,
    scalaTest,
  ))
