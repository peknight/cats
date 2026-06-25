package com.peknight.cats.demo.data

import cats.syntax.eq.*
import cats.syntax.show.*
import cats.{Eq, Show}
import com.peknight.cats.demo.typeclass.Printable

final case class Cat(name: String, age: Int, color: String, favoriteFoods: List[String]):
  def favoriteFood: String = favoriteFoods.headOption.getOrElse("Nothing")
  def yearOfBirth: Int = 2025 - age;
end Cat
object Cat:
  val garfield: Cat = Cat("Garfield", 38, "Orange and black", List("Lasagne"))
  val heathcliff: Cat = Cat("Heathcliff", 33, "Orange and black", List("Junk Food"))
  val java: Cat = Cat("Java", 12, "Yellow", List("Java"))

  given catEq: Eq[Cat] = Eq.instance[Cat] { (cat1, cat2) =>
    (cat1.name === cat2.name) &&
      (cat1.age === cat2.age) &&
      (cat1.color === cat2.color)
  }
  given Show[Cat] = Show.show[Cat] {
    cat =>
      val name = cat.name.show
      val age = cat.age.show
      val color = cat.color.show
      s"$name is a $age year-old $color cat."
  }
  given Printable[Cat] with
    def format(cat: Cat): String =
      val name = Printable.format(cat.name)
      val age = Printable.format(cat.age)
      val color = Printable.format(cat.color)
      s"$name is a $age year-old $color cat."
  end given
end Cat
