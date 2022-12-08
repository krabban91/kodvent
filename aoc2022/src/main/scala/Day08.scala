import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {


    val v = strings.map(_.map(_.toInt).map(_ - 48).map(Tree))
    val zipped = v.zipWithIndex.map(t => (t._1.zipWithIndex, t._2))
    val visible = zipped.map { case (l, y) => l.filter { case (tree, x) =>
      val left = l.zipWithIndex.filter(_._2 < x).map(_._1).map(_._1)
      val right = l.zipWithIndex.filter(_._2 > x).map(_._1).map(_._1)
      val mutUp = mutable.ListBuffer[Tree]()
      val mutDown = mutable.ListBuffer[Tree]()
      zipped.foreach(t => {
        val o = t._1(x)._1
        if (t._2 < y) {
          mutUp.append(o)
        } else if (t._2 > y) {
          mutDown.append(o)
        }
      })
      Seq(left, right, mutUp, mutDown).exists(other => other.forall(p => p.height < tree.height))
    }
    }
    visible.map(_.count(_ => true)).sum
    // 1300 is wrong
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.map(_.map(_.toInt).map(_ - 48).map(Tree))
    val zipped = v.zipWithIndex.map(t => (t._1.zipWithIndex, t._2))
    val view = zipped.map { case (l, y) => l.map { case (tree, x) =>
      val left = l.filter(_._2 < x).map(_._1)
      val right = l.filter(_._2 > x).map(_._1)
      val mutUp = mutable.ListBuffer[Tree]()
      val mutDown = mutable.ListBuffer[Tree]()
      zipped.foreach(t => {
        val o = t._1(x)._1
        if (t._2 < y) {
          mutUp.append(o)
        } else if (t._2 > y) {
          mutDown.append(o)
        }
      })
      val leftVisible = left.reverse.takeWhile(_.height < tree.height).size
      val rightVisible = right.takeWhile(_.height < tree.height).size
      val upVisible = mutUp.reverse.takeWhile(_.height < tree.height).size
      val downVisible = mutDown.takeWhile(_.height < tree.height).size
      val lScore = (leftVisible + (if (leftVisible == left.size) 0 else 1))
      val rScore = rightVisible + (if (rightVisible == right.size) 0 else 1)
      val uScore = upVisible + (if (upVisible == mutUp.size) 0 else 1)
      val dScore = downVisible + (if (downVisible == mutDown.size) 0 else 1)
      lScore * rScore * uScore * dScore

    }

    }
    view.map(_.max).max
  }

  case class Tree(height: Int)
}
