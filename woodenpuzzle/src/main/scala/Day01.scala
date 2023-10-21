import aoc.string.AoCPart1StringTest

import scala.collection.mutable


object Day01 extends App with AoCPart1StringTest {

  printResultPart1Test
  printResultPart1

  override def part1(strings: Seq[String]): String = {
    val startTime = System.nanoTime()
    val tiles = groupsSeparatedByTwoNewlines(strings).filter(_.nonEmpty).map(_.strip())
    val pieces = tiles.dropRight(1).zipWithIndex.map { case (s, i) => Piece(s, i, isRotated = false) }
    val startP = tiles.takeRight(1).map(s => Piece(s, 0, false).rotated.input).map(Puzzle).head
    startP.input.length
    println(s"Legend of all pieces below:\n\n${pieces.mkString("\n")}")
    val frontier = mutable.PriorityQueue[(String, Seq[Piece], Puzzle, Int)]()(Ordering.by { case (steps, _, _, covered) => covered })
    val visited = mutable.HashSet[String]()
    val start = ("", pieces, startP, 0)
    frontier.enqueue(start)
    while (frontier.nonEmpty) {
      val current@(steps, ps, puzzle, covered) = frontier.dequeue()
      //println(s"Map State as following: \n$grid")

      if (ps.isEmpty) {
        println("No more moves to do.")
        println(s"Pieces placed in order $steps")
        println(s"Map State as following: \n$puzzle")
        return steps
      }
      if (visited.add(steps)) {
        if (visited.size % 10000 == 0) {
          val curSeconds = (System.nanoTime() - startTime).toDouble / 1e9
          println(s"Visited.size=${visited.size},\tcovered=$covered,\tsteps=$steps\t(time elapsed: ${timeTakenString(curSeconds)})")
        }
        val nextIsCircle = puzzle.nextIsCirclePiece
        val candidates = if (nextIsCircle) ps.filter(_.isCirclePiece) ++ ps.filter(_.isRotatedCirclePiece).map(_.rotated) else ps.filterNot(_.isCirclePiece) ++ ps.filterNot(_.isRotatedCirclePiece).map(_.rotated)
        val viable = candidates.filter(c => puzzle.canPlace(c))
        val next = viable
          .map { piece =>
            val puzzle1 = puzzle.place(piece)
            (steps + piece.pieceName, ps.filterNot(_.index == piece.index), puzzle1, covered + piece.covers.size)
          }
        //print(next)
        frontier.addAll(next)
      }
    }
    "failed"
  }

  private def timeTakenString(curSeconds: Double) = {
    s"${curSeconds.toInt / 3600}h${(curSeconds.toInt / 60) % 60}m${(curSeconds % 60).toInt}s"
  }
}
