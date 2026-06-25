package com.peknight.cats.instances

import cats.Show

trait ClassInstances:
  given [A]: Show[Class[A]] with
    def show(t: Class[A]): String =
      t.getSimpleName.replaceAll("\\$", "")
  end given
end ClassInstances
object ClassInstances extends ClassInstances
