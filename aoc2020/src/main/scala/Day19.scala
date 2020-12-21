import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day19 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val (ruleMap, messages) = initialData(strings)
    messages.count(s => ruleMap(0).fits(s, ruleMap))
  }

  override def part2(strings: Seq[String]): Long = {
    val (ruleMap, messages) = initialData(strings)
    val editable = mutable.Map[Int, Rule]() ++ ruleMap
    editable(8) = Rule("8: 42 | 42 8")
    editable(11) = Rule("11: 42 31 | 42 11 31")
    messages.count(s => ruleMap(0).fits(s, editable.toMap))
  }

  def initialData(strings: Seq[String]): (Map[Int, Rule], Seq[String]) = {
    val input = groupsSeparatedByTwoNewlines(strings)
    (Rule.map(input.head.split("\n")), input.last.split("\n").filterNot(_.isBlank))
  }

  trait Rule {
    def fits(string: String, allRules: Map[Int, Rule]): Boolean

    def matching(string: String, allRules: Map[Int, Rule]): Set[String]

    def id: Int
  }

  case class Exact(id: Int, rule: String) extends Rule {

    override def fits(string: String, allRules: Map[Int, Rule]): Boolean = string == rule

    override def matching(string: String, allRules: Map[Int, Rule]): Set[String] = {
      if (string.startsWith(rule)) Set(string.replaceFirst(rule, "")) else Set()
    }
  }

  case class Multiple(id: Int, rules: Seq[Seq[Int]]) extends Rule {

    override def fits(string: String, allRules: Map[Int, Rule]): Boolean = this.matching(string, allRules).contains("")

    override def matching(string: String, allRules: Map[Int, Rule]): Set[String] = rules
      .map(l => l.map(allRules(_)))
      .flatMap(r => r.foldLeft(Set(string))((v, r) => v.flatMap(s => r.matching(s, allRules))))
      .toSet
  }

  object Rule {
    def map(strings: Seq[String]): Map[Int, Rule] = {
      val rules = strings.filterNot(_.isBlank).map(Rule(_))
      rules.map(r => r.id -> r).toMap
    }

    def apply(string: String): Rule = {
      val id = string.split(":")(0).trim.toInt
      val ss = string.split(":")(1).trim
      ss match {
        case "\"a\"" => Exact(id, "a")
        case "\"b\"" => Exact(id, "b")
        case _ => Multiple(id, ss.split("\\|")
          .map(_.trim).map(s => s.split(" ").map(_.toInt).toSeq).toSeq)
      }
    }
  }

}
