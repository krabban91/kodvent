import aoc.numeric.{AoCPart1Test, AoCPart2Test}


object Day21 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val v = strings.map(v => Expression(v)).map(v => (v.name, v)).toMap
    v("root").value(v).toLong
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(v => Expression(v)).map(v => (v.name, v)).toMap

    var test = 0L
    var stepSize = (1e12).toLong
    var result = tryYelling(0L, v)
    val direction = math.signum(result - tryYelling(1L, v)).toLong
    while (result != 0) {
      while (result < 0) {
        test -= stepSize * direction
        result = tryYelling(test, v)
      }
      while (result > 0) {
        test += stepSize * direction
        result = tryYelling(test, v)
      }
      stepSize /= 10
    }
    test
  }

  def tryYelling(number: Long, original: Map[String, Expression]): Double = {
    val me = "humn"
    val exps = original ++ Map(me -> Value(me, number))
    val root = exps("root").asInstanceOf[Operation]
    val l = exps(root.l).value(exps)
    val r = exps(root.r).value(exps)
    (r - l)
  }


  trait Expression {
    def name: String

    def value(lookup: Map[String, Expression]): Double
  }

  case class Operation(name: String, l: String, r: String, op: String) extends Expression {
    override def value(lookup: Map[String, Expression]): Double = {
      val left = lookup(l).value(lookup)
      val right = lookup(r).value(lookup)

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
        case expPattern(name, l, op, r) => Operation(name, l, r, op)
      }
    }
  }
}
