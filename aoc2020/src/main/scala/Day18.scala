import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val in = strings.map(s => Expression(s, true))
    in.map(_.evaluate).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val in = strings.map(s => Expression(s, false))
    in.map(_.evaluate).sum
  }

  trait Expression{
    def evaluate: Long
  }

  case class Value(v: Long) extends Expression{
    override def evaluate: Long = v
  }
  case class Multiplication(l: Expression, r: Expression) extends Expression{
    override def evaluate: Long = l.evaluate*r.evaluate
  }
  case class Addition(l: Expression, r: Expression) extends Expression{
    override def evaluate: Long = l.evaluate+r.evaluate

  }
  case class Parentesis(expr: Expression) extends Expression{
    override def evaluate: Long = expr.evaluate
  }

  object Expression{
    def apply(string: String, rightToLefts: Boolean): Expression = {
      if (rightToLefts){
        rightToLeft(string)
      } else {
        addBeforeMul(string)
      }
    }

    private def addBeforeMul(string: String) = {
      val rev = string.reverse
      val m = string.reverse.indexOf("*")
      val a = string.reverse.indexOf("+")
      val pr = string.reverse.indexOf(")")
      var counta = 0

      val accessible = Range(0, string.length).map(i => {
        if (string.charAt(i) == '(') {
          counta += 1
        }
        if (string.charAt(i) == ')') {
          counta -= 1
        }
        counta
      })
      val reachable = string.indices.filter(i => accessible(i) == 0 && string.charAt(i) != ')')
      val muls = reachable.filter(i => accessible(i) == 0 && string.charAt(i) == '*')
      val adds = reachable.filter(i => accessible(i) == 0 && string.charAt(i) == '+')
      if (muls.nonEmpty) {
        Multiplication(Expression(string.substring(0, muls.head).trim, false), Expression(string.substring(muls.head + 1).trim, false))
      } else if (adds.nonEmpty) {
        Addition(Expression(string.substring(0, adds.head).trim, false), Expression(string.substring(adds.head + 1).trim, false))
      } else if (reachable.nonEmpty) {
        Value(string.toLong)
      } else {
        Parentesis(Expression(string.substring(0 + 1, string.length - 1).trim, false))
      }
    }

    private def rightToLeft(string: String) = {
      val rev = string.reverse
      val m = string.reverse.indexOf("*")
      val a = string.reverse.indexOf("+")
      val pr = string.reverse.indexOf(")")
      val pl = string.indexOf("(")
      (m, a, pr) match {
        case (-1, -1, -1) => Value(string.trim.toLong)
        case (_, _, v3) if v3 == 0 => {
          var count = 0
          var indx = -1
          for (i <- Range(0, string.length).reverse) {
            if (string.charAt(i) == ')') {
              count += 1
            }
            if (string.charAt(i) == '(') {
              count -= 1
            }
            if (indx == -1 && count == 0) {
              indx = i
            }
          }
          val r = Parentesis(Expression(string.substring(indx + 1, string.length - 1).trim, true))
          if (indx == 0) {
            r
          } else {
            string.charAt(indx - 2) match {
              case '*' => Multiplication(Expression(string.substring(0, indx - 2).trim, true), r)
              case '+' => Addition(Expression(string.substring(0, indx - 2).trim, true), r)
              case _ =>
                println("missed again")
                Value(-1)
            }
          }

        }
        case (v1, v2, _) if v1 >= 0 && (v2 < 0 || v1 < v2) => Multiplication(Expression(string.reverse.split("\\*", 2)(0).reverse.trim, true), Expression(string.reverse.split("\\*", 2)(1).reverse.trim, true))
        case (v1, v2, _) if v1 < 0 || v2 < v1 => Addition(Expression(string.reverse.split("\\+", 2)(0).reverse.trim, true), Expression(string.reverse.split("\\+", 2)(1).reverse.trim, true))
        case _ =>
          println("missed one")
          Expression(string, true)
      }
    }
  }
}
