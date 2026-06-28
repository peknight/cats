package com.peknight.cats.data

import cats.{Eq, Monad}

import scala.annotation.tailrec

enum Tree[T]:
  case Branch(left: Tree[T], right: Tree[T])
  case Leaf(elem: T)

  def toOptionList: List[Option[T]] =
    @tailrec def loop(open: List[Tree[T]], closed: List[Option[T]]): List[Option[T]] = open match
      case Branch(left, right) :: tail => loop(left :: right :: tail, None :: closed)
      case Leaf(elem) :: tail => loop(tail, Some(elem) :: closed)
      case Nil => closed
    loop(List(this), Nil)

  def toList: List[T] = toOptionList.foldLeft(List.empty[T])((l: List[T], o: Option[T]) => o.map(_ :: l).getOrElse(l))
end Tree

object Tree:
  def branch[T](left: Tree[T], right: Tree[T]): Tree[T] = Branch(left, right)
  def leaf[T](elem: T): Tree[T] = Leaf(elem)

  def fromOptionList[T](list: List[Option[T]]): Tree[T] =
    list.foldLeft(List.empty[Tree[T]]) { (acc: List[Tree[T]], maybe: Option[T]) =>
      maybe.map(Leaf(_) :: acc).getOrElse {
        val left :: right :: tail = acc: @unchecked
        Branch(left, right) :: tail
      }
    }.head

  given Monad[Tree] with
    def pure[A](x: A): Tree[A] = Leaf(x)
    def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = fromOptionList(fa.toOptionList.flatMap {
      case Some(a) => f(a).toOptionList
      case None => List(None)
    })
    /**
     * see: https://stackoverflow.com/questions/44504790/cats-non-tail-recursive-tailrecm-method-for-monads
     * 把树展开了再重构
     *
     * ↓ Branch(Leaf(a), Branch(Branch(Leaf(b), Leaf(c)), Leaf(d))) :: Nil && Nil
     * ↓ Leaf(a) :: Branch(Branch(Leaf(b), Leaf(c)), Leaf(d)) :: Nil && None :: Nil
     * ↓ Branch(Branch(Leaf(b), Leaf(c)), Leaf(d)) :: Nil && Some(a) :: None :: Nil
     * ↓ Branch(Leaf(b), Leaf(c)) :: Leaf(d) :: Nil && None :: Some(a) :: None :: Nil
     * ↓ Leaf(b) :: Leaf(c) :: Leaf(d) :: Nil && None :: None :: Some(a) :: None :: Nil
     * - Nil && Some(d) :: Some(c) :: Some(b) :: None :: None :: Some(a) :: None :: Nil
     * ↑ Leaf(b) :: Leaf(c) :: Leaf(d) :: Nil && None :: None :: Some(a) :: None :: Nil
     * ↑ Branch(Leaf(b), Leaf(c)) :: Leaf(d) :: Nil && None :: Some(a) :: None :: Nil
     * ↑ Branch(Branch(Leaf(b), Leaf(c)), Leaf(d)) :: Nil && Some(a) :: None :: Nil
     * ↑ Leaf(a) :: Branch(Branch(Leaf(b), Leaf(c)), Leaf(d)) :: Nil && None :: Nil
     * ↑ Branch(Leaf(a), Branch(Branch(Leaf(b), Leaf(c)), Leaf(d))) :: Nil && Nil
     */
    def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] =
      @tailrec def loop(open: List[Tree[Either[A, B]]], closed: List[Option[Tree[B]]]): List[Tree[B]] = open match
        case Branch(l, r) :: next => loop(l :: r :: next, None :: closed)
        case Leaf(Left(value)) :: next => loop(f(value) :: next, closed)
        case Leaf(Right(value)) :: next => loop(next, Some(pure(value)) :: closed)
        case Nil => closed.foldLeft(Nil: List[Tree[B]]) { (acc: List[Tree[B]], maybeTree: Option[Tree[B]]) =>
          maybeTree.map(_ :: acc).getOrElse {
            val left :: right :: tail = acc: @unchecked
            Tree.branch(left, right) :: tail
          }
        }
      loop(List(f(a)), Nil).head
  end given

  given [A: Eq]: Eq[Tree[A]] with
    def eqv(x: Tree[A], y: Tree[A]): Boolean = Eq[List[Option[A]]].eqv(x.toOptionList, y.toOptionList)
  end given
end Tree

