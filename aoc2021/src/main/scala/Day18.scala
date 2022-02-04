import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.util.parsing.combinator.RegexParsers

object Day18 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    SnailExp(strings.head)
    val vv: Seq[SnailExp] = strings.map(SnailExp(_))
    val added = vv.head.addAll(vv.tail)

    added.magnitude
  }

  override def part2(strings: Seq[String]): Long = {
    SnailExp(strings.head)
    val vv: Seq[SnailExp] = strings.map(SnailExp(_))

    val inputs = vv.combinations(2)
    inputs.foldLeft(-1L)((max, inp) => {
      val l = inp.head.add(inp.tail.head)
      val r = inp.tail.head.add(inp.head)
      math.max(math.max(l.magnitude, r.magnitude), max)
    })
  }

  trait SnailExp {
    def magnitude: Long

    def add(snailPair: SnailExp): SnailExp = Tree(this.reduceAll, snailPair.reduceAll).reduceAll

    def addAll(snails: Seq[SnailExp]): SnailExp = snails.foldLeft(this)((sp, o) => sp.add(o))

    def reduceAll: SnailExp

    def reduce: Option[SnailExp]

    def explode(level: Int): Option[(SnailExp, (Option[Long], Option[Long]))]

    def passRight(value: Long): Option[SnailExp]

    def passLeft(value: Long): Option[SnailExp]

    def split: Option[SnailExp]

    def asLeaf: Option[Leaf]
  }

  case class Leaf(value: Long) extends SnailExp {

    override def toString: String = value.toString

    override def magnitude: Long = value

    override def reduceAll: SnailExp = this

    override def reduce: Option[SnailExp] = None

    override def explode(level: Int): Option[(SnailExp, (Option[Long], Option[Long]))] = None

    override def passRight(value: Long): Option[SnailExp] = Option(Leaf(this.value + value))

    override def passLeft(value: Long): Option[SnailExp] = Option(Leaf(this.value + value))

    override def split: Option[SnailExp] = {
      Option(Tree(Leaf(value / 2), Leaf(value / 2 + value % 2)))
        .filter(_ => value > 9)
    }

    override def asLeaf: Option[Leaf] = Option(this)
  }

  case class Tree(left: SnailExp, right: SnailExp) extends SnailExp {
    override def toString: String = s"[${left},${right}]"

    override def magnitude: Long = 3 * left.magnitude + 2 * right.magnitude

    override def reduceAll: SnailExp = {
      var curr: Option[SnailExp] = Option(this)
      while (curr.isDefined) {
        val next = curr.get.reduce
        if (next.isEmpty)
          return curr.get
        curr = next
      }
      this
    }

    override def reduce: Option[SnailExp] = {
      explode(0).map(_._1).orElse(split)
    }

    override def explode(level: Int): Option[(SnailExp, (Option[Long], Option[Long]))] = {
      Option(Leaf(0), (left.asLeaf.map(_.value), right.asLeaf.map(_.value)))
        .filter(v => level >= 4 & v._2._1.isDefined && v._2._2.isDefined)
        .orElse(left.explode(level + 1)
          .map(l => l._2._2.flatMap(right.passRight)
            .map(passed => (Tree(l._1, passed), (l._2._1, None)))
            .getOrElse((Tree(l._1, right), l._2)))
          .orElse(right.explode(level + 1)
            .map(r => r._2._1.flatMap(left.passLeft)
              .map(passed => (Tree(passed, r._1), (None, r._2._2)))
              .getOrElse((Tree(left, r._1), r._2)))))
    }

    override def passRight(value: Long): Option[SnailExp] = {
      left.passRight(value)
        .map(Tree(_, right))
        .orElse(right.passRight(value)
          .map(Tree(left, _)))
    }

    override def passLeft(value: Long): Option[SnailExp] = {
      right.passLeft(value)
        .map(Tree(left, _))
        .orElse(left.passLeft(value)
          .map(Tree(_, right)))
    }


    override def split: Option[SnailExp] = {
      left.split.map(Tree(_, right))
        .orElse(right.split
          .map(Tree(left, _)))
    }

    override def asLeaf: Option[Leaf] = None
  }

  object SnailExp extends RegexParsers {
    def leaf: Parser[SnailExp] = """\d+""".r.map(_.toLong).map(Leaf)

    def tree: Parser[SnailExp] = "[" ~> snail ~ "," ~ snail <~ "]" ^^ { case l ~ _ ~ r => Tree(l, r) }

    def snail: Parser[SnailExp] = leaf | tree

    def apply(string: String): SnailExp = {
      parseAll(snail, string).get
    }
  }
}
