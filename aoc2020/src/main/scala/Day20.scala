import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid
import krabban91.kodvent.kodvent.utilities.logging.{LogUtils, Loggable}

import scala.collection.mutable
import scala.jdk.CollectionConverters._


object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val tiles = initialData(strings)
    val valid = matchTiles(tiles)
    val side = math.sqrt(tiles.size).round.toInt
    valid.map(_.map(_.id)).map(s => s.head * s(side - 1) * s(tiles.size - 1) * s(tiles.size - (side))).getOrElse(-1)
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  def initialData(strings: Seq[String]): Seq[Tile] = {
    groupsSeparatedByTwoNewlines(strings)
      .map(s => s.split("\n").filterNot(_.isBlank))
      .map(Tile(_))
  }

  def matchTiles(tiles: Seq[Tile]): Option[Seq[Tile]] = {
    val side = math.sqrt(tiles.size).round.toInt
    val frontier = mutable.PriorityQueue[Seq[Tile]](Seq())(Ordering.by(s => s.size))
    var valid: Option[Seq[Tile]] = None
    while (frontier.nonEmpty) {
      val pop = frontier.dequeue()
      if (pop.size == tiles.size) {
        valid = Option(pop)
        frontier.clear()
      } else {
        val potential = tiles
          .filterNot(t => pop.map(_.id).contains(t.id))
          .flatMap(_.permutations)
          .map(t => pop ++ Seq(t))
          .toSet
        val next = potential.filter(s => Tile.isValidAlignment(s, side))
        frontier.addAll(next)
      }
    }
    valid
  }

  case class Tile(id: Long, grid: Grid[Mini]) {

    def flipHorizontally: Tile = Tile(id, this.grid.map((v, p) => this.grid.get(this.grid.width() - p.x - 1, p.y).get()))

    def flipVertically: Tile = Tile(id, this.grid.map((v, p) => this.grid.get(p.x, this.grid.height() - p.y - 1).get()))

    def rotate: Tile = Tile(id, this.grid.map((v, p) => this.grid.get(this.grid.height() - p.y - 1, p.x).get()))

    def fitsToTheRight(other: Tile): Boolean = Range(0, this.grid.height()).forall(i => this.grid.get(this.grid.width() - 1, i) == other.grid.get(0, i))

    def fitsToTheBottom(other: Tile): Boolean = Range(0, this.grid.width()).forall(i => this.grid.get(i, this.grid.height() - 1) == other.grid.get(i, 0))

    def rotations: Set[Tile] = Set(this, this.rotate, this.rotate.rotate, this.rotate.rotate.rotate)

    def flips: Set[Tile] = Set(this, this.flipVertically, this.flipHorizontally)

    def permutations: Set[Tile] = this.rotations.flatMap(_.flips)

    override def toString: String = s"Tile $id: \n${LogUtils.tiles(grid)}"
  }

  case class Mini(v: Char) extends Loggable {
    override def showTile(): String = v.toString
  }

  object Tile {
    def apply(input: Seq[String]): Tile = Tile(
      input.head.split(":")(0).split(" ")(1).trim.toLong,
      new Grid[Mini](input.tail
        .map(s => s
          .map(Mini(_))
          .asJava)
        .asJava))

    def isValidAlignment(alignment: Seq[Tile], side: Int): Boolean = alignment.indices.forall(i => {
      val r = if (i != alignment.size - 1 && i % side != side - 1) {
        alignment(i).fitsToTheRight(alignment(i + 1))
      } else true
      val d = if (i + side < alignment.size && i / side != side - 1) {
        alignment(i).fitsToTheBottom(alignment(i + side))
      } else true
      r && d
    })

  }

}
