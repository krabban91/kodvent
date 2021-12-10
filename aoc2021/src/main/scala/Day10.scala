import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.map(syntaxCheck).map(_._1).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val syntax = strings.map(syntaxCheck)
    val m = Map(')' -> 1, ']' -> 2, '}' -> 3, '>' -> 4)
    val scores = syntax.filter(t => t._1 == 0).map(_._2)
      .map(l => l.foldLeft(0L)((out, n) => out * 5 + m(n)))
      .sorted
    scores(scores.size / 2)
  }

  def syntaxCheck(string: String): (Long, Seq[Char]) = {
    val m = Map(')' -> 3, ']' -> 57, '}' -> 1197, '>' -> 25137)
    var corrupted = 0
    val expected = string.foldLeft(mutable.Stack[Char]())((stack, c) => {
      c match {
        case '<' =>
          stack.push('>')
        case '(' =>
          stack.push(')')
        case '[' =>
          stack.push(']')
        case '{' =>
          stack.push('}')
        case _ =>
          val exp = stack.pop()
          if (corrupted == 0 && exp != c) {
            corrupted = m(c)
          }
      }
      stack
    })
    (corrupted, expected.toSeq)
  }

}
