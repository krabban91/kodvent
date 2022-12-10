import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    val trees: Seq[Seq[Tree]] = Tree.parse(strings)
    val transposed = trees.head.indices.map(x => trees.map(_ (x)))
    trees.flatten
      .count(tree => tree.isVisible(transposed(tree.point._1), trees(tree.point._2)))
  }

  override def part2(strings: Seq[String]): Long = {
    val trees: Seq[Seq[Tree]] = Tree.parse(strings)
    val transposed = trees.head.indices.map(x => trees.map(_ (x)))
    trees.flatten
      .map(tree => tree.visibilityScore(transposed(tree.point._1), trees(tree.point._2)))
      .max
  }

  case class Tree(height: Int, point: (Int, Int)) {

    def isVisible(column: Seq[Tree], row: Seq[Tree]): Boolean = directions(column, row)
      .exists(_.forall(_.height < height))

    def visibilityScore(column: Seq[Tree], row: Seq[Tree]): Int = directions(column, row)
      .map(visibleTrees).product

    private def isRightOf(other: Tree): Boolean = other.point._1 < point._1

    private def isLeftOf(other: Tree): Boolean = other.point._1 > point._1

    private def isBelow(other: Tree): Boolean = other.point._2 < point._2

    private def isAbove(other: Tree): Boolean = other.point._2 > point._2

    private def visibleTrees(other: Seq[Tree]): Int = {
      val filtered = other.takeWhile(_.height < height).size
      filtered + math.signum(other.size - filtered)
    }

    private def directions(column: Seq[Tree], row: Seq[Tree]): Seq[Seq[Tree]] = {
      Seq(row.filter(_.isLeftOf(this)).reverse,
        row.filter(_.isRightOf(this)),
        column.filter(_.isAbove(this)).reverse,
        column.filter(_.isBelow(this)))
    }

  }

  object Tree {
    def parse(strings: Seq[String]): Seq[Seq[Tree]] = strings.map(_.map(_.toInt).map(_ - 48))
      .zipWithIndex.map { case (l, y) => l.zipWithIndex.map { case (height, x) => Tree(height, (x, y)) } }
  }

}
