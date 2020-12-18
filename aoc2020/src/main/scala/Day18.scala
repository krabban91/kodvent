import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = strings.map(Expression.part1).map(_.evaluate).sum

  override def part2(strings: Seq[String]): Long = strings.map(Expression.part2).map(_.evaluate).sum

  trait Expression {
    def evaluate: Long
    def reconstruct: String
  }

  case class Value(v: Long) extends Expression {
    override def evaluate: Long = v

    override def reconstruct: String = v.toString
  }

  case class Multiplication(l: Expression, r: Expression) extends Expression {
    override def evaluate: Long = l.evaluate * r.evaluate

    override def reconstruct: String = s"${l.reconstruct} * ${r.reconstruct}"
  }

  case class Addition(l: Expression, r: Expression) extends Expression {
    override def evaluate: Long = l.evaluate + r.evaluate

    override def reconstruct: String = s"${l.reconstruct} + ${r.reconstruct}"
  }

  case class Parenthesis(expr: Expression) extends Expression {
    override def evaluate: Long = expr.evaluate

    override def reconstruct: String = s"(${expr.reconstruct})"
  }

  object Expression {
    def part1(string: String): Expression = {
      val levels = useableIndices(string)
      string.indices.filter(i => levels(i) == 0 && string.charAt(i) != ')') match {
        case Seq() => Parenthesis(part1(string.substring(0 + 1, string.length - 1).trim))
        case reachable => reachable.filter(i => levels(i) == 0 && Seq('*', '+').contains(string.charAt(i))).reverse match {
          case op +: _ => string(op) match {
            case '+' => Addition(
              part1(string.substring(0, op).trim),
              part1(string.substring(op + 1).trim))
            case '*' => Multiplication(
              part1(string.substring(0, op).trim),
              part1(string.substring(op + 1).trim))
            case _ => Value(-1)
          }
          case _ => Value(string.toLong)
        }
      }
    }

    def part2(string: String): Expression = {
      val levels = useableIndices(string)
      string.indices.filter(i => levels(i) == 0 && string.charAt(i) != ')') match {
        case Seq() => Parenthesis(part2(string.substring(0 + 1, string.length - 1).trim))
        case reachable => reachable.filter(i => levels(i) == 0 && string.charAt(i) == '*') match {
          case m +: _ => Multiplication(
            part2(string.substring(0, m).trim),
            part2(string.substring(m + 1).trim))
          case _ => reachable.filter(i => levels(i) == 0 && string.charAt(i) == '+') match {
            case a +: _ => Addition(
              part2(string.substring(0, a).trim),
              part2(string.substring(a + 1).trim))
            case _ => Value(string.toLong)
          }
        }
      }
    }

    private def useableIndices(string: String): Seq[Int] = {
      var level: Int = 0
      Range(0, string.length).map(i => {
        level += (if (string.charAt(i) == '(') 1 else if (string.charAt(i) == ')') -1 else 0)
        level
      })
    }
  }

}
