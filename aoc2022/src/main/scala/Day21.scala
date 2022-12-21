import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(v => Expression(v)).map(v => (v.name, v)).toMap
    v("root").value(v)
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }


  trait Expression {
    def name: String

    def value(lookup: Map[String, Expression]): Long
  }

  case class Operation(name: String, l: String, op: String, r: String) extends Expression {
    override def value(lookup: Map[String, Expression]): Long = {
      val left = lookup.getOrElse(l, Value(l, l.toLong)).value(lookup)
      val right = lookup.getOrElse(r, Value(r, r.toLong)).value(lookup)

      op match {
        case "+" => left + right
        case "-" => left - right
        case "*" => left * right
        case "/" => left / right
      }
    }
  }

  case class Value(name: String, l: Long) extends Expression {
    override def value(lookup: Map[String, Expression]): Long = l
  }

  object Expression {
    private val valPattern = """(.+): (-?\d+)""".r
    private val expPattern = """(.+): (.+) (.+) (.+)""".r

    def apply(string: String): Expression = {
      string match {
        case valPattern(name, v) => Value(name, v.toLong)
        case expPattern(name, l, op, r) => Operation(name, l, op, r)
      }
    }
  }
}
