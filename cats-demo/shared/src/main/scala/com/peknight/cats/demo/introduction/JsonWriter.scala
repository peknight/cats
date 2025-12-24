package com.peknight.cats.demo.introduction

trait JsonWriter[A]:
  def write(value: A): Json
end JsonWriter
