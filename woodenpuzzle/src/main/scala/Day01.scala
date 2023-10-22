import aoc.string.AoCPart1StringTest

import scala.collection.mutable


object Day01 extends App with AoCPart1StringTest {

  printResultPart1Test
  printResultPart1

  override def part1(strings: Seq[String]): String = {
    val startTime = System.nanoTime()
    var bestCoverage = 0
    var bestCount = 0
    val tiles = cleaned(strings)
    val pieces = tiles.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val startP = tiles.takeRight(1).map(s => Piece(s, 0, isRotated = false).rotated.input).map(Puzzle).head
    //val startP = tiles.takeRight(1).map(Puzzle).head
    startP.input.length
    println(s"Legend of all pieces below:\n\n${pieces.groupBy(_.covers.size).toSeq.sortBy(_._1).map(t => s"### Groups of size ${t._1}:\n ${t._2.sortBy(_.input).mkString("\n")}").mkString("\n\n")}")
    val frontier = mutable.PriorityQueue[(String, Seq[Piece], Puzzle, Int, Int)]()(Ordering.by { case (steps, _, _, covered, placed) => covered })
    val visited = mutable.HashSet[String]()
    val start = ("", pieces, startP, 0, 0)
    frontier.enqueue(start)
    while (frontier.nonEmpty) {
      val current@(steps, ps, puzzle, covered, placed) = frontier.dequeue()
      //println(s"Map State as following: \n$grid")
      if (covered > bestCoverage) {
        bestCoverage = covered
        //println(s"Best Coverage so far: $bestCoverage")
        //println(current)
      }
      val count = pieces.size - ps.size
      if (count > bestCount) {
        bestCount = count
        //println(s"Best Count so far: $bestCount")
        //println(current)
      }

      if (ps.isEmpty) {
        println("No more moves to do.")
        println(s"Pieces placed in order $steps")
        println(s"Map State as following: \n$puzzle")
        return steps
      }
      if (visited.add(steps)) {
        val nextIsCircle = puzzle.nextIsCirclePiece
        val candidates = if (nextIsCircle) ps.filter(_.isCirclePiece) ++ ps.filter(_.isRotatedCirclePiece).map(_.rotated) else ps.filterNot(_.isCirclePiece) ++ ps.filterNot(_.isRotatedCirclePiece).map(_.rotated)
        val viable = candidates.filter(c => puzzle.canPlace(c))
        if (visited.size % 1000 == 0) {
          val curSeconds = (System.nanoTime() - startTime).toDouble / 1e9
          println(puzzle)

          println(s"Visited.size=${visited.size},\tcovered=$covered,\tsteps=$steps\t(time elapsed: ${timeTakenString(curSeconds)})")
        }
        val next = viable
          .map { piece =>
            val puzzle1 = puzzle.place(piece)
            (steps + piece.pieceName, ps.filterNot(_.index == piece.index), puzzle1, covered + piece.covers.size, placed + 1)
          }
          .filter { case (_, pcs, nextPz, _, _) => nextPz.hasSolution(pcs)}
        //print(next)
        frontier.addAll(next)
      }
    }
    "failed"
  }

  private def timeTakenString(curSeconds: Double) = {
    s"${curSeconds.toInt / 3600}h${(curSeconds.toInt / 60) % 60}m${(curSeconds % 60).toInt}s"
  }

  def cleaned(input: Seq[String]): Seq[String] = {
    groupsSeparatedByTwoNewlines(input).filter(_.nonEmpty).map(_.strip())
  }
}
