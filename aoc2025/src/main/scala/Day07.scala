import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (_, splits) = splitBeams(strings)
    splits
  }

  override def part2(strings: Seq[String]): Long = {
    val (out, _) = splitBeams(strings)
    out.values.sum
  }

  private def splitBeams(strings: Seq[String]): (Map[Int, Long], Long) = {
    val beamIndices = Map[Int, Long](strings.head.indexOf("S") -> 1L)
    strings.tail.foldLeft((beamIndices, 0L)) { case ((indices, splits), row) =>
      val nextBeams = mutable.HashMap[Int, Long]()
      var dSplits = 0L
      indices.foreach { case (ix, times) =>
        if (row(ix) == '^') {
          dSplits += 1L
          val (left, right) = (ix - 1, ix + 1)
          nextBeams.put(left, nextBeams.getOrElse(left, 0L) + times)
          nextBeams.put(right, nextBeams.getOrElse(right, 0L) + times)
        } else {
          nextBeams.put(ix, nextBeams.getOrElse(ix, 0L) + times)
        }
      }
      (nextBeams.toMap, splits + dSplits)
    }
  }
}
