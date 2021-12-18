import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day18Spec extends AnyFlatSpec with Matchers {

  behavior of "SnailExp(string)"
  it should "parse to same e1" in {
    val i = "[[[[[9,8],1],2],3],4]"
    Day18.SnailExp(i).toString shouldBe i
  }
  it should "parse to same e2" in {
    val i = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
    Day18.SnailExp(i).toString shouldBe i
  }
  it should "parse to same e3" in {
    val i = "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"
    Day18.SnailExp(i).toString shouldBe i
  }

  behavior of "explode"
  it should "work e1" in {
    val i = "[[[[[9,8],1],2],3],4]"
    val e = "[[[[0,9],2],3],4]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.explode(0)
    res.isDefined shouldBe true
    res.get._1 shouldBe expected
  }

  it should "work e2" in {
    val i = "[7,[6,[5,[4,[3,2]]]]]"
    val e = "[7,[6,[5,[7,0]]]]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.explode(0)
    res.isDefined shouldBe true
    res.get._1 shouldBe expected
  }
  it should "work e3" in {
    val i = "[[6,[5,[4,[3,2]]]],1]"
    val e = "[[6,[5,[7,0]]],3]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.explode(0)
    res.isDefined shouldBe true
    res.get._1 shouldBe expected
  }
  it should "work e4" in {
    val i = "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"
    val e = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.explode(0)
    res.isDefined shouldBe true
    res.get._1 shouldBe expected
  }
  it should "work e5" in {
    val i = "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"
    val e = "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.explode(0)
    res.isDefined shouldBe true
    res.get._1 shouldBe expected
  }

  behavior of "split"
  it should "work e1" in {
    val i = "[[[[0,7],4],[15,[0,13]]],[1,1]]"
    val e = "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.split
    res.isDefined shouldBe true
    res.get shouldBe expected
  }

  behavior of "magnitude"
  it should "work e1" in {
    val i = "[9,1]"
    val input = Day18.SnailExp(i)
    val expected = 29
    input.magnitude shouldBe expected
  }

  behavior of "reduceAll"
  it should "work e1" in {
    val i = "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"
    val e = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    val input = Day18.SnailExp(i)
    val expected = Day18.SnailExp(e)
    val res = input.reduceAll
    res shouldBe expected
  }

  behavior of "add"
  it should "work e1" in {
    val i = "[[[[4,3],4],4],[7,[[8,4],9]]]"
    val add = "[1,1]"
    val e = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"
    val input = Day18.SnailExp(i)
    val added = Day18.SnailExp(add)
    val expected = Day18.SnailExp(e)
    val res = input.add(added)
    res shouldBe expected
  }
  it should "work e2" in {
    val i = "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"
    val add = "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"
    val e = "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"
    val input = Day18.SnailExp(i)
    val added = Day18.SnailExp(add)
    val expected = Day18.SnailExp(e)
    val res = input.add(added)
    res shouldBe expected
  }

  behavior of "addAll"
  it should "work e1" in {
    val i = """[1,1]
              |[2,2]
              |[3,3]
              |[4,4]""".stripMargin.split("\n")
    val e = "[[[[1,1],[2,2]],[3,3]],[4,4]]"

    val inputs = i.map(Day18.SnailExp(_))
    val expected = Day18.SnailExp(e)
    val res = inputs.head.addAll(inputs.tail)
    res shouldBe expected
  }
  it should "work e2" in {
    val i = """[1,1]
              |[2,2]
              |[3,3]
              |[4,4]
              |[5,5]""".stripMargin.split("\n")
    val e = "[[[[3,0],[5,3]],[4,4]],[5,5]]"

    val inputs = i.map(Day18.SnailExp(_))
    val expected = Day18.SnailExp(e)
    val res = inputs.head.addAll(inputs.tail)
    res shouldBe expected
  }
  it should "work e3" in {
    val i = """[1,1]
              |[2,2]
              |[3,3]
              |[4,4]
              |[5,5]
              |[6,6]""".stripMargin.split("\n")
    val e = "[[[[5,0],[7,4]],[5,5]],[6,6]]"

    val inputs = i.map(Day18.SnailExp(_))
    val expected = Day18.SnailExp(e)
    val res = inputs.head.addAll(inputs.tail)
    res shouldBe expected
  }
  it should "work e4" in {
    val i = """[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
              |[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
              |[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
              |[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
              |[7,[5,[[3,8],[1,4]]]]
              |[[2,[2,2]],[8,[8,1]]]
              |[2,9]
              |[1,[[[9,3],9],[[9,0],[0,7]]]]
              |[[[5,[7,4]],7],1]
              |[[[[4,2],2],6],[8,7]]""".stripMargin.split("\n")
    val e = "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"

    val inputs = i.map(Day18.SnailExp(_))
    val expected = Day18.SnailExp(e)
    val res = inputs.head.addAll(inputs.tail)
    res shouldBe expected
  }

  behavior of "Part 1"
  it should "test input" in {
    Day18.part1TestResult shouldEqual 4140
  }
  it should "real input" in {
    Day18.part1Result shouldEqual 4480
  }
  behavior of "Part 2"
  it should "test input" in {
    Day18.part2TestResult shouldEqual 3993
  }
  it should "real input" in {
    Day18.part2Result shouldEqual 4676
  }
}
