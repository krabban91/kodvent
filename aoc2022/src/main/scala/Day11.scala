import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.MathUtils.LCM

object Day11 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val sorted: Seq[Int] = monkeyAround(strings, 20, worry = false).map(_.inspectCount)
    sorted.head.toLong * sorted.tail.head.toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val sorted: Seq[Int] = monkeyAround(strings, 10000, worry = true).map(_.inspectCount)
    sorted.head.toLong * sorted.tail.head.toLong
  }

  private def monkeyAround(strings: Seq[String], times: Int, worry: Boolean): Seq[Monkey] = {
    val ms = groupsSeparatedByTwoNewlines(strings).map(_.strip()).map(Monkey(_)).sortBy(_.id)
    val lcm = ms.tail.foldLeft(ms.head.divisibleBy) { case (lcm, other) => LCM(lcm, other.divisibleBy) }
    (1 to times).foldLeft(ms) { case (monkeys, i) => Monkey.playWithItems(monkeys, worry, lcm) }
      .sortBy(_.inspectCount).reverse
  }

  case class Monkey(id: Int, items: Seq[Long], operation: Operation, divisibleBy: Long, testTrue: Int, testFalse: Int, inspectCount: Int) {

    def inspect(worry: Boolean): Seq[(Long, Int)] = {
      val targets = items.map(operation.calculate)
        .map(level => if (worry) level else level / 3)
        .map(worryLevel => (worryLevel, if (worryLevel % divisibleBy == 0) testTrue else testFalse))
      targets
    }

    def withItems(items: Seq[Long]): Monkey = {
      Monkey(id, items, operation, divisibleBy, testTrue, testFalse, inspectCount)
    }

    def incInspectCount(count: Int): Monkey = {
      Monkey(id, items, operation, divisibleBy, testTrue, testFalse, inspectCount + count)
    }

  }

  trait Operation {
    def calculate(old: Long): Long
  }

  object Operation {

    def apply(string: String): Operation = {
      val operationAdd = """new = old \+ (\d+)""".r
      val operationMul = """new = old \* (\d+)""".r
      val operationPow = """new = old \* old""".r
      string match {
        case operationAdd(v) => Add(v.toInt)
        case operationMul(v) => Mul(v.toInt)
        case operationPow() => Pow2()
      }
    }
  }

  case class Add(value: Int) extends Operation {
    override def calculate(old: Long): Long = old + value
  }

  case class Mul(value: Int) extends Operation {
    override def calculate(old: Long): Long = old * value
  }

  case class Pow2() extends Operation {
    override def calculate(old: Long): Long = old * old
  }

  object Monkey {

    def apply(string: String): Monkey = {
      val monkeyPattern =
        ("""Monkey (\d+):[\s]+""" +
          """Starting items: (.*)[\s]+""" +
          """Operation: (.*)[\s]+""" +
          """Test: divisible by (\d+)[\s]+""" +
          """If true: throw to monkey (\d+)[\s]+""" +
          """If false: throw to monkey (\d+)""").r
      string match {
        case monkeyPattern(id, items, operation, divBy, testTrue, testFalse) =>
          val itemList = items.split(",").map(_.trim.toLong)
          Monkey(id.toInt, itemList, Operation(operation), divBy.toLong, testTrue.toInt, testFalse.toInt, 0)
      }
    }

    def playWithItems(monkeys: Seq[Monkey], worry: Boolean, lcm: Long): Seq[Monkey] = {
      val next = monkeys.map(_.id).foldLeft(monkeys) { case (next, id) =>
        val monkey = next.find(_.id == id).get
        val items = monkey.inspect(worry)
        val throwTo = items.groupBy(_._2).map { case (k, l) => (k, l.map(_._1)) }
        val other = next.filter(_.id != id)
          .map(m => m.withItems(m.items ++ throwTo.getOrElse(m.id, Seq())))

        (Seq(monkey.withItems(Seq()).incInspectCount(items.size)) ++ other).sortBy(_.id)
      }

      next.map(m => m.withItems(m.items.map(wl => wl % lcm)))
    }
  }
}
