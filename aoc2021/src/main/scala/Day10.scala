import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    strings.map(syntaxCheck).sum
  }

  override def part2(strings: Seq[String]): Long = -1

  def syntaxCheck(string: String): Long = {
    val m = Map(')' -> 3, ']' -> 57, '}' -> 1197, '>' -> 25137)
    var corrupted = 0
    string.foldLeft(mutable.Stack[Char]())((stack, c) => {
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
    corrupted
  }

}
