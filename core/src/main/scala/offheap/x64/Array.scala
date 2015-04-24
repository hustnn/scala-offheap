package offheap
package x64

import scala.language.experimental.{macros => CanMacro}
import offheap.internal.macros

private[x64] trait ArrayOps[A] extends Any {
  def isEmpty: Boolean                                = macro macros.Array.isEmpty
  def nonEmpty: Boolean                               = macro macros.Array.nonEmpty
  def size: Size                                      = macro macros.Array.size
  def length: Size                                    = macro macros.Array.size
  def apply(index: Addr): A                           = macro macros.Array.apply
  def update(index: Addr, value: A): Unit             = macro macros.Array.update
  def foreach(f: A => Unit): Unit                     = macro macros.Array.foreach
  def map[B](f: A => B)(implicit m: Memory): Array[B] = macro macros.Array.map[B]
}

private[x64] trait ArrayCompanionOps extends Any {
  def uninit[T](n: Size)(implicit m: Memory): Array[T]       = macro macros.Array.uninit[T]
  def apply[T](values: T*)(implicit m: Memory): Array[T]     = macro macros.Array.vararg[T]
  def fill[T](n: Size)(elem: => T)
             (implicit m: Memory): Array[T]                  = macro macros.Array.fill[T]
  def copy[T](from: Array[T], fromIndex: Addr,
              to: Array[T], toIndex: Addr, size: Size): Unit = macro macros.Array.copy[T]
}

final class Array[A] private (val $ref: Ref) extends AnyVal with ArrayOps[A] {
  override def toString =
    if (isEmpty) s"offheap.x64.Array.empty"
    else s"offheap.x64.Array@${$ref.addr}"
}
object Array extends ArrayCompanionOps {
  def empty[T]: Array[T]              = new Array[T](null)
  def fromRepr[T](ref: Ref): Array[T] = new Array[T](ref)
  def toRepr[T](arr: Array[T]): Ref   = arr.$ref
}

final class UncheckedArray[A] private (val $ref: Addr) extends AnyVal with ArrayOps[A] {
  override def toString =
    if (isEmpty) s"offheap.x64.UncheckedArray.empty"
    else s"offheap.x64.UncheckedArray@${$ref}"
}
object UncheckedArray extends ArrayCompanionOps {
  def empty[T]: UncheckedArray[T]               = new UncheckedArray[T](0)
  def fromRepr[T](ref: Addr): UncheckedArray[T] = new UncheckedArray[T](ref)
  def toRepr[T](arr: UncheckedArray[T]): Addr   = arr.$ref
}
