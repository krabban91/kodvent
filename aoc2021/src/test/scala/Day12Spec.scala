import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day12Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day12.part1TestResult shouldEqual 10
  }
  "Part1" should "be correct" in {
    Day12.part1Result shouldEqual 5958
  }

  "Part2 Test" should "be correct" in {
    Day12.part2TestResult shouldEqual 36
  }
  "Part2" should "be correct" in {
    Day12.part2Result shouldEqual 150426
  }

  "graph" should "not have any paths to start" in {
    val map = Day12.graph(Day12.getInputTest)
    assert(!map.values.exists(_.exists(_=="start")))
    val map2 = Day12.graph(Day12.getInput)
    assert(!map2.values.exists(_.exists(_=="start")))
  }
  "graph" should "have nodes that lead to end" in {
    val map = Day12.graph(Day12.getInputTest)
    assert(map.values.exists(_.exists(_=="end")))
    val map2 = Day12.graph(Day12.getInput)
    assert(map2.values.exists(_.exists(_=="end")))
  }

  "paths" should "work for example 1 part 1" in {
    val paths = Day12.paths(Day12.graph(Day12.getInputTest), part2 = false)
    val expected = """start,A,b,A,c,A,end
                     |start,A,b,A,end
                     |start,A,b,end
                     |start,A,c,A,b,A,end
                     |start,A,c,A,b,end
                     |start,A,c,A,end
                     |start,A,end
                     |start,b,A,c,A,end
                     |start,b,A,end
                     |start,b,end""".stripMargin.split("\n").map(s=>s.split(",").toList).toSet
    paths shouldBe expected
  }

  "paths" should "work for example 1 part 2" in {
    val paths = Day12.paths(Day12.graph(Day12.getInputTest), part2 = true)
    val expected = """start,A,b,A,b,A,c,A,end
                     |start,A,b,A,b,A,end
                     |start,A,b,A,b,end
                     |start,A,b,A,c,A,b,A,end
                     |start,A,b,A,c,A,b,end
                     |start,A,b,A,c,A,c,A,end
                     |start,A,b,A,c,A,end
                     |start,A,b,A,end
                     |start,A,b,d,b,A,c,A,end
                     |start,A,b,d,b,A,end
                     |start,A,b,d,b,end
                     |start,A,b,end
                     |start,A,c,A,b,A,b,A,end
                     |start,A,c,A,b,A,b,end
                     |start,A,c,A,b,A,c,A,end
                     |start,A,c,A,b,A,end
                     |start,A,c,A,b,d,b,A,end
                     |start,A,c,A,b,d,b,end
                     |start,A,c,A,b,end
                     |start,A,c,A,c,A,b,A,end
                     |start,A,c,A,c,A,b,end
                     |start,A,c,A,c,A,end
                     |start,A,c,A,end
                     |start,A,end
                     |start,b,A,b,A,c,A,end
                     |start,b,A,b,A,end
                     |start,b,A,b,end
                     |start,b,A,c,A,b,A,end
                     |start,b,A,c,A,b,end
                     |start,b,A,c,A,c,A,end
                     |start,b,A,c,A,end
                     |start,b,A,end
                     |start,b,d,b,A,c,A,end
                     |start,b,d,b,A,end
                     |start,b,d,b,end
                     |start,b,end""".stripMargin.split("\n").map(s=>s.split(",").toList).toSet
    paths shouldBe expected
  }

}
