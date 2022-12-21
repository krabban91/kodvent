import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  //printResultPart1Test
  //printResultPart2Test
  //printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(v => Expression(v)).map(v => (v.name, v)).toMap
    v("root").value(v).toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(v => Expression(v)).map(v => (v.name, v)).toMap

    val me = "humn"
    var lookup = v ++ Map(me -> Value(me, 0))
    val root = lookup("root").asInstanceOf[Operation]

    val out = (3 * 1e12).toLong + (3 * 1e11).toLong + (4 * 1e10).toLong + (9 * 1e9).toLong +
      (1 * 1e8).toLong + (3 * 1e7).toLong + (6 * 1e6).toLong +
      (3 * 1e5).toLong + (8 * 1e4).toLong + (4 * 1e3).toLong +
      (4 * 1e2).toLong + (4 * 1e1).toLong + 1L
    lookup = v ++ Map(me -> Value(me, out))
    val l = lookup(root.l).value(lookup)
    val r = lookup(root.r).value(lookup)
    val diff = r - l
    println(s"diff: $diff")
    out

  }


  trait Expression {
    def name: String

    def value(lookup: Map[String, Expression]): Double
  }

  case class Operation(name: String, l: String, op: String, r: String) extends Expression {
    override def value(lookup: Map[String, Expression]): Double = {
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
    override def value(lookup: Map[String, Expression]): Double = l
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
