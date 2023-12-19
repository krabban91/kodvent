import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (workflows, ratings) = parse(strings)
    ratings.filter(_.evaluate(workflows))
      .map(_.sum).sum
  }


  override def part2(strings: Seq[String]): Long = {
    -1
  }


  private def parse(strings: Seq[String]) = {
    val gs = groupsSeparatedByTwoNewlines(strings)
    val workflows = gs.head.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(Workflow(_))
    val ratings = gs.last.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(Ratings(_))
    (workflows.map(w => (w.name, w)).toMap, ratings)
  }

  case class Workflow(name: String, conditions: Seq[Condition])

  case class Condition(expression: Option[Rule], thenDo: String)

  case class Rule(category: String, limit: Long, gt: Boolean)

  case class Ratings(x: Long, m: Long, a: Long, s: Long) {

    def sum: Long = x + m + a + s

    def evaluate(workflows: Map[String, Workflow]): Boolean = {
      var curr = "in"
      while (true) {
        if (curr == "A") {
          return true
        } else if (curr == "R") {
          return false
        } else {


          val workflow = workflows(curr)
          val out = workflow.conditions.foldLeft(Option.empty[Boolean]) { case (o, c) =>
            if (o.isDefined) {
              o
            } else {
              c match {
                case Condition(None, thenDo) => curr = thenDo;
                  None
                case Condition(Some(rule), thenDo) =>
                  if (this.test(rule)) {
                    curr = thenDo
                    Some(true)
                  } else {None}

              }
            }
          }
        }
      }
      false
    }

    def test(rule: Rule): Boolean = {
      if (rule.gt) {
        rule.category match {
          case "x" => x > rule.limit
          case "m" => m > rule.limit
          case "a" => a > rule.limit
          case "s" => s > rule.limit
        }
      } else {
        rule.category match {
          case "x" => x < rule.limit
          case "m" => m < rule.limit
          case "a" => a < rule.limit
          case "s" => s < rule.limit
        }
      }
    }
  }

  object Workflow {
    def apply(string: String): Workflow = {
      val s = string.split("\\{")
      val name = s.head
      val conds = s.last.stripSuffix("}").split(",").map(Condition(_))
      Workflow(name, conds)
    }
  }

  object Condition {
    def apply(string: String): Condition = {
      if (string.contains(":")) {
        val s = string.split(":")
        Condition(Some(Rule(s.head)), s.last)
      } else {
        Condition(None, string)
      }
    }
  }

  object Accept extends Condition(None, "A")

  object Reject extends Condition(None, "R")

  object Rule {
    def apply(string: String): Rule = {
      if (string.contains("<")) {
        val s = string.split("<")
        Rule(s.head, s.last.toLong, gt = false)
      } else {
        val s = string.split(">")
        Rule(s.head, s.last.toLong, gt = true)
      }
    }
  }

  object Ratings {
    def apply(string: String): Ratings = {
      val map = string.stripPrefix("{").stripSuffix("}").split(",")
        .foldLeft(Map[String, Long]()) { case (o, v) =>
          val s = v.split("=")
          o ++ Map(s.head -> s.last.toLong)
        }
      Ratings(map("x"), map("m"), map("a"), map("s"))

    }
  }
}
