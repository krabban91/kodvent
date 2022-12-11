import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = groupsSeparatedByTwoNewlines(strings).map(_.strip())
    val ms = v.map(Monkey(_)).sortBy(_.id)
    // relief worry level /3
    val outMonkeys = (1 to 20).foldLeft(ms){ case (monkeys, i) =>
      var next = monkeys
      monkeys.map(_.id).foreach(id => {
        val monkey = next.find(_.id == id).get
        val items = monkey.inspect()
        val throwTo = items.groupBy(_._2)
        val other = next.filter(_.id != id)

        val next1 = (Seq(monkey.withItems(Seq()).incInspectCount(items.size)) ++ other.map(m => m.withItems(m.items ++ throwTo.getOrElse(m.id, Seq()).map(_._1)))).sortBy(_.id)
        next = next1
      })
      val v = next
      next
    }
    val sorted = outMonkeys.sortBy(_.inspectCount).reverse

    sorted.head.inspectCount * sorted.tail.head.inspectCount

  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  case class Monkey(id: Int, items: Seq[Int], operation: String, divisibleBy: Int, testTrue: Int, testFalse: Int, inspectCount: Int) {

    def inspect(): Seq[(Int, Int)] = {
      val operationAdd = """new = old + (\d+)""".r
      val operationMul = """new = old * (\d+)""".r
      val operationPow = """new = old * old""".r
      val targets = items.map(item => operation match {
        case operationAdd(v) => item + v.toInt
        case operationMul(v) => item * v.toInt
        case operationPow() => item * item
        case op =>
          val tokens = op.split(" ")
          if (tokens(3) == "+") {
            item + tokens.last.toInt
          } else {
            if (tokens.last == "old") {
              item * item
            } else {
              item * tokens.last.toInt
            }
          }
      }).map(_/3)
        .map(worry => {

          (worry,if (worry % divisibleBy == 0) { testTrue} else {testFalse})
        })
      targets
    }

    def withItems(items: Seq[Int]): Monkey = {
      Monkey(id, items, operation, divisibleBy, testTrue, testFalse, inspectCount)
    }

    def incInspectCount(count: Int): Monkey = {
      Monkey(id, items, operation, divisibleBy, testTrue, testFalse, inspectCount + count)
    }

  }

  object Monkey{

    def apply(string: String): Monkey = {
      val monkeyPattern =
        """Monkey (.+):.*""".r
      string match {
        case _ =>
          val lines = string.split("\n")
          val id = lines.head.split(" ").last.dropRight(1).toInt
          val items = lines.tail.head.split(": ").last.split(",").map(_.trim).map(_.toInt)
          val operation = lines.tail.tail.head.split(": ").last.trim
          val test = lines(3).split(": ").last.split("by ").last.toInt
          val testTrue = lines(4).split("monkey ").last.toInt
          val testFalse = lines.last.split("monkey ").last.toInt
          Monkey(id, items, operation, test, testTrue, testFalse, 0)
      }
    }
  }
}
