import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import implicits.Tuples._

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (workflows, ratings) = parse(strings)
    ratings.map(_.map(evaluate(_, workflows)._1).sum).sum
  }

  def limits: (Long, Long) = (0L, 4000L)

  override def part2(strings: Seq[String]): Long = {
    val (workflows, ratings) = parse(strings)
    evaluate(RatingRange(limits, limits, limits, limits, 1), workflows)._2
  }

  def evaluate(ratingRange: RatingRange, workflows: Map[String, Workflow], curr: String = "in"): (Long, Long) = {
      if (curr == "A") {
        return (ratingRange.sum, ratingRange.permutations)
      } else if (curr == "R") {
        return (0L, 0L)
      } else {
        val workflow = workflows(curr)
        workflow.conditions.foldLeft(((0L, 0L), Option(ratingRange))) { case ((o, range), c) =>
          if (range.isDefined) {
            val r = range.get
            val res = c match {
              case Condition(None, thenDo) => (evaluate(r, workflows, thenDo), None);
              case Condition(Some(rule), thenDo) =>
                val ranges = ratingRange.test(rule)
                (ranges._1.map(evaluate(_, workflows, thenDo)).getOrElse((0L,0L)), ranges._2)
            }
            (o + res._1, res._2)
          } else {
            (o, None)
          }
        }._1
    }
  }


  private def parse(strings: Seq[String]) = {
    val gs = groupsSeparatedByTwoNewlines(strings)
    val workflows = gs.head.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(Workflow(_))
    val ratings = gs.last.split("\n").map(_.strip()).filter(_.nonEmpty)
      .map(Ratings(_))
      .map { case Ratings(x, m, a, s) => Seq(RatingRange((x, x), (m, m), (a, a), (s, s), 1)) }
    (workflows.map(w => (w.name, w)).toMap, ratings)
  }

  case class Workflow(name: String, conditions: Seq[Condition])

  case class Condition(expression: Option[Rule], thenDo: String)

  case class Rule(category: String, limit: Long, gt: Boolean)

  case class RatingRange(x: (Long, Long), m: (Long, Long), a: (Long, Long), s: (Long, Long), multiplier: Long) {
    def sum: Long = (x._1 + m._1 + a._1 + s._1) * multiplier
    def permutations: Long = (x._2 - x._1 + 1) * (m._2 - m._1 + 1) * (a._2 - a._1 + 1) * (s._2 - s._1 + 1)

    def test(rule: Rule): (Option[RatingRange],Option[RatingRange]) = {
      if (rule.gt) {
        //first matches, seconds doesn't
        //(50, 100)
        // rule: gt < 45 =>

        val split = (v: (Long, Long)) => Seq(Some((math.max(rule.limit + 1, v._1), v._2)), Some((v._1, math.min(rule.limit, v._2))))
          .map(_.filter { case (l, r) => r >= l })
        rule.category match {
          case "x" =>
            val pair = split(x)
              .map(_.map(nx => RatingRange(nx, m, a, s, multiplier)))
            (pair.head, pair.last)
          case "m" =>
            val pair = split(m)
              .map(_.map(nm => RatingRange(x, nm, a, s, multiplier)))
            (pair.head, pair.last)
          case "a" =>
            val pair = split(a)
              .map(_.map(na => RatingRange(x, m, na, s, multiplier)))
            (pair.head, pair.last)
          case "s" =>
            val pair = split(s)
              .map(_.map(ns => RatingRange(x, m, a, ns, multiplier)))
            (pair.head, pair.last)
        }
      } else {
        //first matches, seconds doesn't
        val split = (v: (Long, Long)) => Seq(Some((v._1, math.min(rule.limit -1 , v._2))), Some((math.max(rule.limit, v._1), v._2)))
          .map(_.filter { case (l, r) => r >= l })
        rule.category match {
          case "x" =>
            val pair = split(x)
              .map(_.map(nx => RatingRange(nx, m, a, s, multiplier)))
            (pair.head, pair.last)
          case "m" =>
            val pair = split(m)
              .map(_.map(nm => RatingRange(x, nm, a, s, multiplier)))
            (pair.head, pair.last)
          case "a" =>
            val pair = split(a)
              .map(_.map(na => RatingRange(x, m, na, s, multiplier)))
            (pair.head, pair.last)
          case "s" =>
            val pair = split(s)
              .map(_.map(ns => RatingRange(x, m, a, ns, multiplier)))
            (pair.head, pair.last)
        }
      }
    }
  }

  case class Ratings(x: Long, m: Long, a: Long, s: Long) {

    def sum: Long = x + m + a + s

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
