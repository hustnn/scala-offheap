import org.scalatest.FunSuite
import regions._

class CombinatorSuite extends FunSuite {
  test("empty isEmpty") {
    assert(Ref.empty[Int].isEmpty)
  }

  test("empty nonEmpty") {
    assert(!Ref.empty[Int].nonEmpty)
  }

  test("nonempty isEmpty") {
    Region { r => assert(!Ref(1)(r).isEmpty) }
  }

  test("nonempty nonEmpty") {
    Region { r => assert(Ref(1)(r).nonEmpty) }
  }

  test("empty get") {
    intercept[EmptyRefException.type] {
      Ref.empty[Int].get
    }
  }

  test("empty set") {
    intercept[EmptyRefException.type] {
      Ref.empty[Int].set(42)
    }
  }
}