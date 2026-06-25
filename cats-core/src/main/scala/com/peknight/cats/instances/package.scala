package com.peknight.cats

package object instances:
  object applicative extends ApplicativeInstances
  object eitherT extends EitherTInstances
  object eq extends EqInstances
  object optionEitherT extends OptionEitherTInstances

  object clazz extends ClassInstances

  object tuple extends TupleInstances

  object instant extends InstantInstances
  object localDate extends LocalDateInstances
  object year extends YearInstances
end instances
