import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day09 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val preambleSize = if (strings.size == 20) 5 else 25 // test input is smaller
    val all = strings.map(_.toLong)
    all(all.slice(preambleSize, all.size - 1).indices.map(_ + preambleSize).find(!followsRule(all, _, preambleSize)).get)
  }

  def followsRule(all: Seq[Long], index: Int, preambleSize: Int): Boolean = {
    for (i <- Range(index - preambleSize, index); j <- Range(index - preambleSize, index)) {
      if (all(index) == all(i) + all(j)) {
        return true
      }
    }
    false
  }

  override def part2(strings: Seq[String]): Long = {
    val all = strings.map(_.toLong)
    encryptionWeakness(all, part1(strings))
  }

  def encryptionWeakness(all: Seq[Long], invalid: Long): Long = {
    val l = mutable.ListBuffer[Long]()
    val invalidIdx = all.indexOf(invalid)
    for (start <- Range(0, invalidIdx)) {
      l.clear()
      var idx = start
      while (l.sum < invalid) {
        l += all(idx)
        idx += 1
      }
      if (l.sum == invalid && l.size > 1) {
        return l.min + l.max
      }
    }
    -1
  }

}
