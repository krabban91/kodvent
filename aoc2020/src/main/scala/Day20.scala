import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid
import krabban91.kodvent.kodvent.utilities.logging.{LogUtils, Loggable}

import scala.collection.mutable
import scala.jdk.CollectionConverters._


object Day20 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val tiles = initialData(strings)
    val valid = matchTiles(tiles)
    val side = math.sqrt(tiles.size).round.toInt
    valid.map(_.map(_.id)).map(s => s.head * s(side - 1) * s(tiles.size - 1) * s(tiles.size - (side))).getOrElse(-1)
  }

  override def part2(strings: Seq[String]): Long = {
    val image = Tile.fromTiles(matchTiles(initialData(strings)).get)
    val seaMonster = Day20.Tile(Day20.read("day20$_seamonster.txt"))
    image.permutations
      .find(i => i.sweepSeaMonster(seaMonster) != i)
      .map(_.sweepSeaMonster(seaMonster))
      .get.grid.sum(_.roughness)
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
          .filter(t => Tile.fitsAtEnd(pop, t, side))
          .map(t => pop ++ Seq(t))
          .toSet
        frontier.addAll(potential)
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

    def sweepSeaMonster(seaMonster: Tile): Tile = {
      var output = this
      val smHeight = seaMonster.grid.height()
      val smWidth = seaMonster.grid.width()
      for (y <- Range(0, this.grid.height() - smHeight); x <- Range(0, this.grid.width() - smWidth)) {
        if (Range(0, smHeight).forall(dy => Range(0, smWidth).forall(dx => {
          val mask = seaMonster.grid.get(dx, dy).get.v
          val loc = output.grid.get(x + dx, y + dy).get.v
          mask == '.' || loc == 'O' || loc == '#'
        }))) {
          output = Tile(id, output.grid.map((m, p) => {
            if (p.x >= x && p.x <= x + smWidth && p.y >= y && p.y <= y + smHeight) {
              seaMonster.grid.get(p.x - x, p.y - y).map(a => {
                if (a.v == '#')
                  Mini('O')
                else m
              }).orElse(m)
            } else m
          }))
        }
      }
      output
    }
  }

  case class Mini(v: Char) extends Loggable {
    override def showTile(): String = v.toString

    def roughness: Long = if (v == '#') 1 else 0
  }

  object Tile {
    def apply(input: Seq[String]): Tile = Tile(
      input.head.split(":")(0).split(" ")(1).trim.toLong,
      new Grid[Mini](input.tail
        .map(s => s
          .map(Mini(_))
          .asJava)
        .asJava))

    def fromTiles(tiles: Seq[Tile]): Tile = {
      val side = math.sqrt(tiles.size).round.toInt
      val sb = new StringBuilder()
      sb.append("Tile -1:\n")
      for (tY <- Range(0, side)) {
        val tileRow = tiles.slice(tY * side, (tY + 1) * side)
        for (y <- Range(1, tileRow.head.grid.height() - 1)) {
          tileRow.foreach(t => sb.append(t.grid.getRaw.get(y).subList(1, t.grid.width() - 1).asScala.map(_.showTile()).reduce((l, r) => l + r)))
          sb.append("\n")
        }
      }
      Tile(sb.toString().split("\n"))
    }

    def fitsAtEnd(current: Seq[Tile], tile: Tile, side: Int): Boolean = ((current.size % side == 0) || current.last.fitsToTheRight(tile)) &&
      ((current.size - side < 0) || current(current.size - side).fitsToTheBottom(tile))
  }

}
